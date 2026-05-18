package dk.salon.salon.controller;

import dk.salon.salon.model.Service;
import dk.salon.salon.service.SalonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final SalonService salonService;

    public ServiceController(SalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping
    public List<Service> getAllServices() {
        return salonService.getAllServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getById(@PathVariable Long id) {
        return salonService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Service> create(@RequestBody Service service) {
        return ResponseEntity.status(201).body(salonService.saveService(service));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> update(@PathVariable Long id, @RequestBody Service service) {
        return salonService.getServiceById(id)
                .map(existing -> {
                    service.setId(id);
                    return ResponseEntity.ok(salonService.saveService(service));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salonService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }
}
