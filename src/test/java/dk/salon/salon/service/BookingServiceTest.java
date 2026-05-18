package dk.salon.salon.service;

import dk.salon.salon.model.*;
import dk.salon.salon.repository.BookingRepository;
import dk.salon.salon.repository.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private PricingClient pricingClient;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void test_Get_Bookings_By_Customer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUsername("testuser");

        Booking booking1 = new Booking();
        booking1.setCustomer(customer);
        booking1.setTotalPrice(new BigDecimal("350.00"));

        Booking booking2 = new Booking();
        booking2.setCustomer(customer);
        booking2.setTotalPrice(new BigDecimal("750.00"));

        when(bookingRepository.findByCustomer(customer)).thenReturn(List.of(booking1, booking2));

        List<Booking> result = bookingService.getBookingsByCustomer(customer);

        assertEquals(2, result.size());
    }

    @Test
    void test_IsWithin24Hours_Returns_True() {
        Slot slot = new Slot();
        slot.setStartTime(LocalDateTime.now().plusHours(10));

        Booking booking = new Booking();
        booking.setSlot(slot);

        assertTrue(bookingService.isWithin24Hours(booking));
    }

    @Test
    void test_IsWithin24Hours_Returns_False() {
        Slot slot = new Slot();
        slot.setStartTime(LocalDateTime.now().plusDays(3));

        Booking booking = new Booking();
        booking.setSlot(slot);

        assertFalse(bookingService.isWithin24Hours(booking));
    }

    @Test
    void test_Get_All_Bookings() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();

        when(bookingRepository.findAll()).thenReturn(List.of(booking1, booking2));

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(2, result.size());
    }
}