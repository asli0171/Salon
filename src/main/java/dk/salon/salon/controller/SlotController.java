package dk.salon.salon.controller;

import dk.salon.salon.model.Slot;
import dk.salon.salon.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    public List<Slot> getAvailableSlots(
            @RequestParam(required = false) Long hairdresserId,
            @RequestParam(required = false) Long serviceId) {

        if (hairdresserId != null) {
            return slotService.getAvailableSlotsByHairdresser(hairdresserId);
        }
        if (serviceId != null) {
            return slotService.getAvailableSlotsByService(serviceId);
        }
        return slotService.getAvailableSlots();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Slot> getById(@PathVariable Long id) {
        return slotService.getSlotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Slot> create(@RequestBody Slot slot) {
        return ResponseEntity.status(201).body(slotService.saveSlot(slot));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Slot> update(@PathVariable Long id, @RequestBody Slot slot) {
        return slotService.getSlotById(id)
                .map(existing -> {
                    slot.setId(id);
                    return ResponseEntity.ok(slotService.saveSlot(slot));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        slotService.deleteSlotById(id);
        return ResponseEntity.noContent().build();
    }
}
