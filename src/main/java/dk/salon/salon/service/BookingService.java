package dk.salon.salon.service;

import dk.salon.salon.model.*;
import dk.salon.salon.repository.BookingRepository;
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
    private final PricingClient pricingClient;

    public BookingService(BookingRepository bookingRepository,
                          SlotRepository slotRepository,
                          PricingClient pricingClient) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.pricingClient = pricingClient;
    }

    @Transactional
    public Booking createBooking(Long slotId, Customer customer) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot ikke fundet: " + slotId));

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

        Booking booking = new Booking(customer, slot, BigDecimal.valueOf(price));
        return bookingRepository.save(booking);
    }

    public boolean isWithin24Hours(Booking booking) {
        LocalDateTime slotStart = booking.getSlot().getStartTime();
        return LocalDateTime.now().isAfter(slotStart.minusHours(24));
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, Customer customer) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking ikke fundet: " + bookingId));

        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Du kan kun aflyse dine egne bookinger");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking er allerede aflyst");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getSlot().setAvailable(true);
        slotRepository.save(booking.getSlot());

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBooking(Long bookingId, Long newSlotId, Customer customer) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking ikke fundet: " + bookingId));

        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Du kan kun ændre dine egne bookinger");
        }

        if (isWithin24Hours(booking)) {
            throw new RuntimeException("Kan ikke ændre booking inden for 24 timer");
        }

        Slot newSlot = slotRepository.findById(newSlotId)
                .orElseThrow(() -> new RuntimeException("Nyt slot ikke fundet: " + newSlotId));

        if (!newSlot.isAvailable()) {
            throw new RuntimeException("Det valgte slot er ikke ledigt");
        }

        booking.getSlot().setAvailable(true);
        slotRepository.save(booking.getSlot());

        newSlot.setAvailable(false);
        slotRepository.save(newSlot);

        booking.setSlot(newSlot);
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }
    public List<Booking> getBookingsByCustomer(Customer customer) {
        return bookingRepository.findByCustomer(customer);
    }
    public Optional<Booking> getBookingById(Long id) { return bookingRepository.findById(id); }

    @Transactional
    public void deleteBookingById(Long id) { bookingRepository.deleteById(id); }
}
