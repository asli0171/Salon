package dk.salon.salon.service;

import dk.salon.salon.model.Service;
import dk.salon.salon.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalonServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private SalonService salonService;

    @Test
    void test_Get_All_Services() {
        Service klip = new Service("Klip", "Professionel klipning", 45, new BigDecimal("350.00"));
        Service farve = new Service("Farve", "Hårfarve", 90, new BigDecimal("750.00"));
        List<Service> services = List.of(klip, farve);

        when(serviceRepository.findAll()).thenReturn(services);

        List<Service> result = salonService.getAllServices();

        assertEquals(2, result.size());
    }
}