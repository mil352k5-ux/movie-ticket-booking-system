package cinema;

public final class StandardTier implements PricingTier {
    @Override
    public long calculatePrice(long basePrice) {
        return basePrice;
    }

    @Override
    public String name() {
        return "Standard";
    }
}
