package dk.salon.salon.service;

import dk.salon.salon.model.Slot;
import dk.salon.salon.repository.SlotRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public Slot saveSlot(Slot slot) { return slotRepository.save(slot); }
    public List<Slot> getAllSlots() { return slotRepository.findAll(); }
    public List<Slot> getAvailableSlots() { return slotRepository.findByIsAvailableTrue(); }
    public List<Slot> getAvailableSlotsByHairdresser(Long hairdresserId) {
        return slotRepository.findByHairdresserIdAndIsAvailableTrue(hairdresserId);
    }
    public List<Slot> getAvailableSlotsByService(Long serviceId) {
        return slotRepository.findByServiceIdAndIsAvailableTrue(serviceId);
    }
    public Optional<Slot> getSlotById(Long id) { return slotRepository.findById(id); }
    public void deleteSlotById(Long id) { slotRepository.deleteById(id); }
}
