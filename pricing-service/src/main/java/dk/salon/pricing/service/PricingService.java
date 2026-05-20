package dk.salon.pricing.service;

import dk.salon.pricing.model.PricingRequest;
import dk.salon.pricing.model.PricingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private final double premiumSurcharge;
    private final double colourSurcharge;
    private final double treatmentSurcharge;
    private final int longDurationThreshold;
    private final double longDurationSurcharge;

    public PricingService(
            @Value("${pricing.premium.surcharge:0.20}") double premiumSurcharge,
            @Value("${pricing.colour.surcharge:50.0}") double colourSurcharge,
            @Value("${pricing.treatment.surcharge:30.0}") double treatmentSurcharge,
            @Value("${pricing.long.duration.threshold:75}") int longDurationThreshold,
            @Value("${pricing.long.duration.surcharge:50.0}") double longDurationSurcharge) {
        this.premiumSurcharge = premiumSurcharge;
        this.colourSurcharge = colourSurcharge;
        this.treatmentSurcharge = treatmentSurcharge;
        this.longDurationThreshold = longDurationThreshold;
        this.longDurationSurcharge = longDurationSurcharge;
    }

    public PricingResponse calculate(PricingRequest request) {
        double price = request.getBasePrice();
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("Basispris: ").append(price).append(" kr");

        if (request.isPremiumHairdresser()) {
            double surcharge = Math.round(price * premiumSurcharge);
            price += surcharge;
            breakdown.append(" + premium tillæg: ").append(surcharge).append(" kr");
        }



        if (request.getServiceType() != null) {
            String type = request.getServiceType().toLowerCase();

            if (type.contains("farve") || type.contains("colour") || type.contains("color")) {
                price += colourSurcharge;
                breakdown.append(" + farvetillæg: ").append(colourSurcharge).append(" kr");
            }

            if (type.contains("behandling") || type.contains("treatment")) {
                price += treatmentSurcharge;
                breakdown.append(" + behandlingstillæg: ").append(treatmentSurcharge).append(" kr");
            }
        }

        if (request.getDurationMinutes() > longDurationThreshold) {
            price += longDurationSurcharge;
            breakdown.append(" + lang behandling: ").append(longDurationSurcharge).append(" kr");
        }

        double finalPrice = Math.round(price);
        breakdown.append(" = ").append(finalPrice).append(" kr");

        return new PricingResponse(finalPrice, breakdown.toString());
    }
}