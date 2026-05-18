package dk.salon.salon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PricingClient {

    private final RestClient restClient;
    private final JwtService jwtService;

    public PricingClient(@Value("${pricing.service.url}") String pricingServiceUrl,
                         JwtService jwtService) {
        this.restClient = RestClient.builder()
                .baseUrl(pricingServiceUrl)
                .build();
        this.jwtService = jwtService;
    }

    public double calculatePrice(double basePrice, String serviceType,
                                 int durationMinutes, boolean isPremiumHairdresser) {
        PricingRequest request = new PricingRequest(
                basePrice, serviceType, durationMinutes, isPremiumHairdresser);

        return restClient.post()
                .uri("/api/pricing/calculate")
                .header("Authorization", "Bearer " + jwtService.generateToken())
                .body(request)
                .retrieve()
                .body(Double.class);
    }

    record PricingRequest(double basePrice, String serviceType,
                          int durationMinutes, boolean isPremiumHairdresser) {}
}
