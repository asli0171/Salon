package dk.salon.salon.config;

import dk.salon.salon.model.Hairdresser;
import dk.salon.salon.model.Service;
import dk.salon.salon.model.Slot;
import dk.salon.salon.repository.HairdresserRepository;
import dk.salon.salon.repository.ServiceRepository;
import dk.salon.salon.repository.SlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner generateSlots(
            HairdresserRepository hairdresserRepository,
            ServiceRepository serviceRepository,
            SlotRepository slotRepository) {

        return args -> {
            List<Hairdresser> hairdressers = hairdresserRepository.findAll();
            List<Service> services = serviceRepository.findAll();

            if (hairdressers.isEmpty() || services.isEmpty()) return;

            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusMonths(2);

            LocalTime morningStart = LocalTime.of(10, 0);
            LocalTime weekdayEnd = LocalTime.of(16, 0);
            LocalTime saturdayEnd = LocalTime.of(15, 0);

            for (Hairdresser hairdresser : hairdressers) {
                for (LocalDate date = today; date.isBefore(endDate); date = date.plusDays(1)) {
                    int dayOfWeek = date.getDayOfWeek().getValue();
                    if (dayOfWeek == 7) continue;

                    LocalTime endTime = dayOfWeek == 6 ? saturdayEnd : weekdayEnd;

                    for (Service service : services) {
                        int duration = service.getDurationMinutes();
                        LocalTime slotStart = morningStart;

                        while (slotStart.plusMinutes(duration).compareTo(endTime) <= 0) {
                            LocalDateTime start = LocalDateTime.of(date, slotStart);
                            LocalDateTime end = start.plusMinutes(duration);

                            boolean exists = slotRepository.existsByHairdresserAndStartTime(hairdresser, start);
                            if (!exists) {
                                Slot slot = new Slot();
                                slot.setHairdresser(hairdresser);
                                slot.setService(service);
                                slot.setStartTime(start);
                                slot.setEndTime(end);
                                slot.setAvailable(true);
                                slotRepository.save(slot);
                            }

                            slotStart = slotStart.plusMinutes(duration + 15);
                        }
                    }
                }
            }
        };
    }
}