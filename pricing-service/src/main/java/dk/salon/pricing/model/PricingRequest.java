package dk.salon.pricing.model;

public class PricingRequest {

    private double basePrice;
    private String serviceType;
    private int durationMinutes;
    private boolean isPremiumHairdresser;

    public PricingRequest() {}

    public PricingRequest(double basePrice, String serviceType,
                          int durationMinutes, boolean isPremiumHairdresser) {
        this.basePrice = basePrice;
        this.serviceType = serviceType;
        this.durationMinutes = durationMinutes;
        this.isPremiumHairdresser = isPremiumHairdresser;
    }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isPremiumHairdresser() { return isPremiumHairdresser; }
    public void setPremiumHairdresser(boolean premiumHairdresser) { isPremiumHairdresser = premiumHairdresser; }
}
