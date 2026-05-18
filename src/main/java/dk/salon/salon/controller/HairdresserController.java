package dk.salon.salon.controller;

import dk.salon.salon.model.Hairdresser;
import dk.salon.salon.service.HairdresserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hairdressers")
public class HairdresserController {

    private final HairdresserService hairdresserService;

    public HairdresserController(HairdresserService hairdresserService) {
        this.hairdresserService = hairdresserService;
    }

    @GetMapping
    public List<Hairdresser> getActiveHairdressers() {
        return hairdresserService.getActiveHairdressers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hairdresser> getById(@PathVariable Long id) {
        return hairdresserService.getHairdresserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin")
    public ResponseEntity<Hairdresser> create(@RequestBody Hairdresser hairdresser) {
        return ResponseEntity.status(201).body(hairdresserService.saveHairdresser(hairdresser));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Hairdresser> update(@PathVariable Long id,
                                              @RequestBody Hairdresser hairdresser) {
        return hairdresserService.getHairdresserById(id)
                .map(existing -> {
                    hairdresser.setId(id);
                    return ResponseEntity.ok(hairdresserService.saveHairdresser(hairdresser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hairdresserService.deleteHairdresserById(id);
        return ResponseEntity.noContent().build();
    }
}
