package dk.salon.pricing.controller;

import dk.salon.pricing.model.PricingRequest;
import dk.salon.pricing.model.PricingResponse;
import dk.salon.pricing.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Double> calculate(@RequestBody PricingRequest request) {
        PricingResponse response = pricingService.calculate(request);
        return ResponseEntity.ok(response.getPrice());
    }

    @PostMapping("/calculate/detailed")
    public ResponseEntity<PricingResponse> calculateDetailed(@RequestBody PricingRequest request) {
        return ResponseEntity.ok(pricingService.calculate(request));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Pricing service kører");
    }
}
