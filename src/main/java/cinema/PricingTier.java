package cinema;

public interface PricingTier {
    double calculatePrice(double basePrice);

    String name();
}
