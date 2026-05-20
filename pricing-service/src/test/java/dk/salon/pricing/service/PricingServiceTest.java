package dk.salon.pricing.service;

import dk.salon.pricing.model.PricingRequest;
import dk.salon.pricing.service.PricingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest {

    PricingService pricingService = new PricingService(0.20, 50.0, 30.0, 75, 50.0);

    @Test
    void should_return_base_price_for_standard_service() {
        PricingRequest request = new PricingRequest(350.0, "Klip", 45, false);

        double result = pricingService.calculate(request).getPrice();

        assertEquals(350.0, result);
    }

    @Test
    void should_add_premium_surcharge() {
        PricingRequest request = new PricingRequest(350.0, "Klip", 45, true);

        double result = pricingService.calculate(request).getPrice();

        assertEquals(420.0, result);
    }

    @Test
    void should_add_colour_surcharge() {
        PricingRequest request = new PricingRequest(750.0, "Farve", 90, false);

        double result = pricingService.calculate(request).getPrice();

        assertEquals(850.0, result);
    }

    @Test
    void should_add_long_duration_surcharge() {
        PricingRequest request = new PricingRequest(550.0, "Behandling", 90, false);

        double result = pricingService.calculate(request).getPrice();

        assertEquals(630.0, result);
    }

}