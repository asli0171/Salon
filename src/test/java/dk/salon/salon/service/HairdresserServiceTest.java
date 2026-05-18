package dk.salon.salon.service;

import dk.salon.salon.model.Hairdresser;
import dk.salon.salon.repository.HairdresserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HairdresserServiceTest {

    @Mock
    private HairdresserRepository hairdresserRepository;

    @InjectMocks
    private HairdresserService hairdresserService;

    @Test
    void test_Get_All_Hairdressers() {
        Hairdresser anna = new Hairdresser("Anna Nielsen", "Specialist i farve");
        Hairdresser mads = new Hairdresser("Mads Christensen", "Herreklip");
        List<Hairdresser> hairdressers = List.of(anna, mads);

        when(hairdresserRepository.findAll()).thenReturn(hairdressers);

        List<Hairdresser> result = hairdresserService.getAllHairdressers();

        assertEquals(2, result.size());
    }

    @Test
    void test_Get_Active_Hairdressers() {
        Hairdresser anna = new Hairdresser("Anna Nielsen", "Specialist i farve");
        anna.setActive(true);

        when(hairdresserRepository.findByActiveTrue()).thenReturn(List.of(anna));

        List<Hairdresser> result = hairdresserService.getActiveHairdressers();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }
}