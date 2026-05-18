package dk.salon.salon.repository;

import dk.salon.salon.model.Hairdresser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HairdresserRepository extends JpaRepository<Hairdresser, Long> {
    List<Hairdresser> findByActiveTrue();
}
