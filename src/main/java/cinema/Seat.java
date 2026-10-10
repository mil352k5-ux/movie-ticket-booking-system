package cinema;

public final class Seat {
    private final String seatId;
    private final PricingTier pricingTier;

    public Seat(String seatId, PricingTier pricingTier) {
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
