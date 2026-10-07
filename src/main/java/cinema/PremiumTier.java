package cinema;

public final class PremiumTier implements PricingTier {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * 1.25;
    }

    @Override
    public String name() {
        return "Premium";
    }
}
