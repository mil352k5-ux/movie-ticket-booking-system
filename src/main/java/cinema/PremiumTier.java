package cinema;

public final class PremiumTier implements PricingTier {
    @Override
    public long calculatePrice(long basePrice) {
        return Math.multiplyExact(basePrice, 125) / 100;
    }

    @Override
    public String name() {
        return "Premium";
    }
}
