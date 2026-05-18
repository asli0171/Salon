package dk.salon.salon.service;

import dk.salon.salon.model.Service;
import dk.salon.salon.repository.ServiceRepository;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class SalonService {

    private final ServiceRepository serviceRepository;

    public SalonService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Service saveService(Service service) { return serviceRepository.save(service); }
    public List<Service> getAllServices() { return serviceRepository.findAll(); }
    public Optional<Service> getServiceById(Long id) { return serviceRepository.findById(id); }
    public void deleteServiceById(Long id) { serviceRepository.deleteById(id); }
}
