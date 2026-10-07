package cinema;

public final class StandardTier implements PricingTier {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }

    @Override
    public String name() {
        return "Standard";
    }
}
