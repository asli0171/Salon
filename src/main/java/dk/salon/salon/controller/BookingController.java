package dk.salon.salon.controller;

import dk.salon.salon.dto.BookingRequestDTO;
import dk.salon.salon.model.Booking;
import dk.salon.salon.model.BookingStatus;
import dk.salon.salon.repository.CustomerRepository;
import dk.salon.salon.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CustomerRepository customerRepository;

    public BookingController(BookingService bookingService,
                             CustomerRepository customerRepository) {
        this.bookingService = bookingService;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO request) {
        if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
            return ResponseEntity.badRequest().body("Navn er påkrævet");
        }
        if (request.getCustomerEmail() == null || request.getCustomerEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email er påkrævet");
        }
        if (request.getCustomerPhone() == null || request.getCustomerPhone().isBlank()) {
            return ResponseEntity.badRequest().body("Telefonnummer er påkrævet");
        }
        try {
            Booking booking = bookingService.createBooking(request);
            return ResponseEntity.status(201).body(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find")
    public ResponseEntity<?> findBooking(@RequestParam String reference,
                                         @RequestParam String email) {
        return bookingService.findByReferenceAndEmail(reference, email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(bookingService.getAllBookings());
        }
        return customerRepository.findByUsername(auth.getName())
                .map(customer -> ResponseEntity.ok(bookingService.getBookingsByCustomer(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication auth) {
        return bookingService.getBookingById(id)
                .map(booking -> {
                    boolean isAdmin = auth != null && auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    boolean isOwner = auth != null && booking.getCustomer() != null &&
                            booking.getCustomer().getUsername().equals(auth.getName());
                    if (isAdmin || isOwner) return ResponseEntity.ok(booking);
                    return ResponseEntity.status(403).<Booking>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication auth) {
        return bookingService.getBookingById(id)
                .map(booking -> {
                    boolean isAdmin = auth != null && auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    boolean isOwner = auth != null && booking.getCustomer() != null &&
                            booking.getCustomer().getUsername().equals(auth.getName());
                    if (!isAdmin && !isOwner) {
                        return ResponseEntity.status(403).body(Map.of("error", "Ingen adgang"));
                    }
                    try {
                        boolean within24h = bookingService.isWithin24Hours(booking);
                        Booking cancelled = bookingService.cancelBooking(id);
                        return ResponseEntity.ok(Map.of(
                                "booking", cancelled,
                                "chargeApplied", within24h,
                                "message", within24h
                                        ? "Booking aflyst. 50% gebyr opkræves."
                                        : "Booking aflyst gratis."
                        ));
                    } catch (RuntimeException e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/no-show")
    public ResponseEntity<?> markNoShow(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body(Map.of("error", "Kun admin"));

        return bookingService.getBookingById(id)
                .map(booking -> {
                    if (booking.getStatus() != BookingStatus.CONFIRMED) {
                        return ResponseEntity.badRequest().body(Map.of("error", "Kun bekræftede bookinger"));
                    }
                    Booking updated = bookingService.markAsNoShow(id);
                    return ResponseEntity.ok(Map.of("booking", updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}