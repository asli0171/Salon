package dk.salon.salon.service;

import dk.salon.salon.model.Hairdresser;
import dk.salon.salon.repository.HairdresserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HairdresserService {

    private final HairdresserRepository hairdresserRepository;

    public HairdresserService(HairdresserRepository hairdresserRepository) {
        this.hairdresserRepository = hairdresserRepository;
    }

    public Hairdresser saveHairdresser(Hairdresser hairdresser) {
        return hairdresserRepository.save(hairdresser);
    }

    public List<Hairdresser> getAllHairdressers() { return hairdresserRepository.findAll(); }
    public List<Hairdresser> getActiveHairdressers() { return hairdresserRepository.findByActiveTrue(); }
    public Optional<Hairdresser> getHairdresserById(Long id) { return hairdresserRepository.findById(id); }
    public void deleteHairdresserById(Long id) { hairdresserRepository.deleteById(id); }
}
