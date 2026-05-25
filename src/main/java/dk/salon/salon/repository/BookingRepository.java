package dk.salon.salon.repository;

import dk.salon.salon.model.Booking;
import dk.salon.salon.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(Customer customer);
    List<Booking> findByCustomerEmail(String email);
    Optional<Booking> findByBookingReference(String bookingReference);
    Optional<Booking> findByBookingReferenceAndCustomerEmail(String bookingReference, String customerEmail);
}