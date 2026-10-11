package cinema;

public final class Seat {
    private final String seatId;
    private final PricingTier pricingTier;

    public Seat(String seatId, PricingTier pricingTier) {
        if (seatId == null || seatId.isBlank()) {
            throw new IllegalArgumentException("Seat ID must not be blank.");
        }
        if (pricingTier == null) {
            throw new IllegalArgumentException("Pricing tier must not be null.");
        }
        this.seatId = seatId;
        this.pricingTier = pricingTier;
    }

    public String getSeatId() {
        return seatId;
    }

    public PricingTier getPricingTier() {
        return pricingTier;
    }

    public long price(long basePrice) {
        return pricingTier.calculatePrice(basePrice);
    }
}
