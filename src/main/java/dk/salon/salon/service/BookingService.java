package dk.salon.salon.service;

import dk.salon.salon.dto.BookingRequestDTO;
import dk.salon.salon.model.*;
import dk.salon.salon.repository.BookingRepository;
import dk.salon.salon.repository.CustomerRepository;
import dk.salon.salon.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final CustomerRepository customerRepository;
    private final PricingClient pricingClient;

    public BookingService(BookingRepository bookingRepository,
                          SlotRepository slotRepository,
                          CustomerRepository customerRepository,
                          PricingClient pricingClient) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.customerRepository = customerRepository;
        this.pricingClient = pricingClient;
    }

    @Transactional
    public Booking createBooking(BookingRequestDTO request) {
        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot ikke fundet: " + request.getSlotId()));

        if (!slot.isAvailable()) {
            throw new RuntimeException("Slot er allerede booket");
        }

        double price = pricingClient.calculatePrice(
                slot.getService().getBasePrice().doubleValue(),
                slot.getService().getName(),
                slot.getService().getDurationMinutes(),
                false
        );

        slot.setAvailable(false);
        slotRepository.save(slot);

        Customer customer = customerRepository.findByEmail(request.getCustomerEmail()).orElse(null);

        Booking booking = new Booking(
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getCustomerPhone(),
                customer,
                slot,
                BigDecimal.valueOf(price)
        );

        return bookingRepository.save(booking);
    }

    public Optional<Booking> findByReference(String reference) {
        return bookingRepository.findByBookingReference(reference);
    }

    public Optional<Booking> findByReferenceAndEmail(String reference, String email) {
        return bookingRepository.findByBookingReferenceAndCustomerEmail(reference, email);
    }

    public boolean isWithin24Hours(Booking booking) {
        LocalDateTime slotStart = booking.getSlot().getStartTime();
        return LocalDateTime.now().isAfter(slotStart.minusHours(24));
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking ikke fundet: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking er allerede aflyst");
        }

        if (isWithin24Hours(booking)) {
            booking.setTotalPrice(booking.getTotalPrice().multiply(BigDecimal.valueOf(0.5)));
        } else {
            booking.setTotalPrice(BigDecimal.ZERO);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getSlot().setAvailable(true);
        slotRepository.save(booking.getSlot());

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }

    public List<Booking> getBookingsByCustomer(Customer customer) {
        return bookingRepository.findByCustomer(customer);
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByCustomerEmail(email);
    }

    public Optional<Booking> getBookingById(Long id) { return bookingRepository.findById(id); }

    @Transactional
    public Booking markAsNoShow(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking ikke fundet: " + bookingId));
        booking.setStatus(BookingStatus.NO_SHOW);
        return bookingRepository.save(booking);
    }
}