package dk.salon.salon.controller;

import dk.salon.salon.dto.BookingRequestDTO;
import dk.salon.salon.model.Booking;
import dk.salon.salon.model.BookingStatus;
import dk.salon.salon.model.Customer;
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

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(Authentication auth) {
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
                    boolean isAdmin = auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    boolean isOwner = booking.getCustomer() != null &&
                            booking.getCustomer().getUsername().equals(auth.getName());
                    if (isAdmin || isOwner) {
                        return ResponseEntity.ok(booking);
                    }
                    return ResponseEntity.status(403).<Booking>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO request,
                                           Authentication auth) {
        Customer customer = customerRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Kunde ikke fundet"));

        try {
            Booking booking = bookingService.createBooking(request.getSlotId(), customer);
            return ResponseEntity.status(201).body(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication auth) {
        Customer customer = customerRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Kunde ikke fundet"));

        return bookingService.getBookingById(id)
                .map(booking -> {
                    boolean within24h = bookingService.isWithin24Hours(booking);
                    try {
                        Booking cancelled = bookingService.cancelBooking(id, customer);
                        if (within24h) {
                            return ResponseEntity.ok(Map.of(
                                    "booking", cancelled,
                                    "chargeApplied", true,
                                    "message", "Booking aflyst. 50% gebyr opkræves da aflysning er inden for 24 timer.",
                                    "amount", cancelled.getTotalPrice()
                            ));
                        }
                        return ResponseEntity.ok(Map.of(
                                "booking", cancelled,
                                "chargeApplied", false,
                                "message", "Booking aflyst gratis."
                        ));
                    } catch (RuntimeException e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id,
                                           @RequestBody BookingRequestDTO request,
                                           Authentication auth) {
        Customer customer = customerRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Kunde ikke fundet"));

        try {
            Booking updated = bookingService.updateBooking(id, request.getSlotId(), customer);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/no-show")
    public ResponseEntity<?> markNoShow(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body(Map.of("error", "Kun admin kan markere no-show"));
        }

        return bookingService.getBookingById(id)
                .map(booking -> {
                    if (booking.getStatus() != BookingStatus.CONFIRMED) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error", "Kun bekræftede bookinger kan markeres som ikke mødt op"));
                    }
                    try {
                        Booking updated = bookingService.markAsNoShow(id);
                        return ResponseEntity.ok(Map.of(
                                "booking", updated,
                                "message", "Kunden er markeret som ikke mødt op. Fuld pris opkræves.",
                                "amount", updated.getTotalPrice()
                        ));
                    } catch (RuntimeException e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}