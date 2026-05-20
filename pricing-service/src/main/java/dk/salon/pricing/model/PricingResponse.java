package dk.salon.pricing.model;

public class PricingResponse {

    private double price;
    private String breakdown;

    public PricingResponse() {}

    public PricingResponse(double price, String breakdown) {
        this.price = price;
        this.breakdown = breakdown;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getBreakdown() { return breakdown; }
    public void setBreakdown(String breakdown) { this.breakdown = breakdown; }
}
