package cinema;

import java.util.Objects;

public final class Seat {
    private final String seatId;
    private final PricingTier pricingTier;

    public Seat(String seatId, PricingTier pricingTier) {
        if (seatId == null || seatId.isBlank()) {
            throw new IllegalArgumentException("Seat ID must not be blank.");
        }
        this.seatId = seatId;
        this.pricingTier = Objects.requireNonNull(pricingTier, "Pricing tier must not be null.");
    }

    public String getSeatId() {
        return seatId;
    }

    public PricingTier getPricingTier() {
        return pricingTier;
    }

    public double price(double basePrice) {
        return pricingTier.calculatePrice(basePrice);
    }
}
