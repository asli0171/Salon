package dk.salon.salon.service;

import dk.salon.salon.model.Admin;
import dk.salon.salon.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin saveAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() { return adminRepository.findAll(); }
    public Optional<Admin> getAdminById(Long id) { return adminRepository.findById(id); }
    public void deleteAdminById(Long id) { adminRepository.deleteById(id); }
}
