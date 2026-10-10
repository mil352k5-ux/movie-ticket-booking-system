package cinema;

public interface PricingTier {
    long calculatePrice(long basePrice);

    String name();
}
