package dk.salon.salon.service;

import dk.salon.salon.model.Service;
import dk.salon.salon.repository.ServiceRepository;
import dk.salon.salon.repository.SlotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ServiceIntegrationTest {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SalonService salonService;

    @Test
    void test_Get_Services_From_Database() {
        slotRepository.deleteAll();
        serviceRepository.deleteAll();

        Service service = new Service();
        service.setName("Klip");
        service.setDescription("Professionel klipning");
        service.setDurationMinutes(45);
        service.setBasePrice(new BigDecimal("350.00"));
        serviceRepository.save(service);

        List<Service> result = salonService.getAllServices();

        assertEquals(1, result.size());
        assertEquals("Klip", result.get(0).getName());
    }
}