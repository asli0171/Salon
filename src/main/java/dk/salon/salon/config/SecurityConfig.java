package dk.salon.salon.config;

import dk.salon.salon.repository.CustomerRepository;
import dk.salon.salon.service.AdminDetailsService;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminDetailsService adminDetailsService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(AdminDetailsService adminDetailsService,
                          CustomerRepository customerRepository,
                          PasswordEncoder passwordEncoder) {
        this.adminDetailsService = adminDetailsService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider adminProvider = new DaoAuthenticationProvider();
        adminProvider.setUserDetailsService(adminDetailsService);
        adminProvider.setPasswordEncoder(passwordEncoder);

        UserDetailsService customerDetailsService = username ->
                customerRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Customer not found: " + username));

        DaoAuthenticationProvider customerProvider = new DaoAuthenticationProvider();
        customerProvider.setUserDetailsService(customerDetailsService);
        customerProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(List.of(adminProvider, customerProvider));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationManager(authenticationManager())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/booking.html",
                                "/login.html", "/confirm.html", "/register.html",
                                "/mypage.html", "/om.html", "/frisoer.html", "/kosmetolog.html").permitAll()
                        .requestMatchers("/**.css", "/**.js").permitAll()
                        .requestMatchers("/api/hairdressers").permitAll()
                        .requestMatchers("/api/hairdressers/{id}").permitAll()
                        .requestMatchers("/api/services").permitAll()
                        .requestMatchers("/api/services/{id}").permitAll()
                        .requestMatchers("/api/slots").permitAll()
                        .requestMatchers("/api/slots/{id}").permitAll()
                        .requestMatchers("/api/customers/register").permitAll()
                        .requestMatchers("/api/customers/me").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/bookings").permitAll()
                        .requestMatchers("/api/bookings/find").permitAll()
                        .requestMatchers("/api/bookings/**").authenticated()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/hairdressers/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/services/**").hasRole("ADMIN")
                        .requestMatchers("/api/slots/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .loginPage("/login.html")
                        .failureUrl("/login.html?error=true")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                            response.sendRedirect(isAdmin ? "/admin.html" : "/mypage.html");
                        })
                )
                .logout(logout -> logout.logoutSuccessUrl("/login.html"))
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }

    @Bean
    public ServletContextInitializer sessionConfig() {
        return servletContext -> {
            servletContext.getSessionCookieConfig().setHttpOnly(true);
            servletContext.getSessionCookieConfig().setMaxAge(3600);
            servletContext.getSessionCookieConfig().setName("SALON_SESSION");
        };
    }
}