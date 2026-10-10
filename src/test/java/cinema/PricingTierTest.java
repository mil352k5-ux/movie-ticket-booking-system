package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class PricingTierTest {
    private static final long BASE_PRICE = 100_000L;

    @Test
    void calculatesPriceForEverySupportedTier() {
        List<Seat> seats = List.of(
                new Seat("A1", new StandardTier()),
                new Seat("A2", new PremiumTier()),
                new Seat("A3", new ReclinerTier()));
        List<Long> expectedPrices = List.of(100_000L, 125_000L, 150_000L);

        for (int index = 0; index < seats.size(); index++) {
            assertEquals(expectedPrices.get(index).longValue(), seats.get(index).price(BASE_PRICE));
        }
    }

    @Test
    void acceptsANewPricingTierThroughTheSameContract() {
        PricingTier discountedTier = new PricingTier() {
            @Override
            public long calculatePrice(long basePrice) {
                return Math.multiplyExact(basePrice, 80) / 100;
            }

            @Override
            public String name() {
                return "Discounted tier";
            }
        };

        Seat seat = new Seat("B1", discountedTier);

        assertEquals(80_000L, seat.price(BASE_PRICE));
    }
}
