package dk.salon.salon.repository;

import dk.salon.salon.model.Hairdresser;
import dk.salon.salon.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByHairdresserIdAndIsAvailableTrue(Long hairdresserId);
    List<Slot> findByServiceIdAndIsAvailableTrue(Long serviceId);
    List<Slot> findByIsAvailableTrue();
    boolean existsByHairdresserAndStartTime(Hairdresser hairdresser, java.time.LocalDateTime startTime);
}
