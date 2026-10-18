package cinema;

public final class LuxuryTier implements PricingTier {
    private static final long PERCENT_BASE = 100L;
    private static final long PRICE_PERCENT = 175L;

    @Override
    public long calculatePrice(long basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price must not be negative.");
        }

        long wholeHundreds = basePrice / PERCENT_BASE;
        long remainder = basePrice % PERCENT_BASE;
        if (wholeHundreds > Long.MAX_VALUE / PRICE_PERCENT) {
            throw new ArithmeticException("Luxury price overflow.");
        }

        long wholePrice = wholeHundreds * PRICE_PERCENT;
        long remainderPrice = (remainder * PRICE_PERCENT) / PERCENT_BASE;
        return Math.addExact(wholePrice, remainderPrice);
    }

    @Override
    public String name() {
        return "Luxury";
    }
}
