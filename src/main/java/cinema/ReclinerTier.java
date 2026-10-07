package cinema;

public final class ReclinerTier implements PricingTier {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * 1.50;
    }

    @Override
    public String name() {
        return "Recliner";
    }
}
