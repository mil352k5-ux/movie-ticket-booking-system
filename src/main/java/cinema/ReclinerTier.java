package cinema;

public final class ReclinerTier implements PricingTier {
    @Override
    public long calculatePrice(long basePrice) {
        return Math.multiplyExact(basePrice, 150) / 100;
    }

    @Override
    public String name() {
        return "Recliner";
    }
}
