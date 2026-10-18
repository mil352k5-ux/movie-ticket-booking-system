package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;

class LuxuryTierTest {
    private static final long BASE_PRICE = 100_000L;
    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    @Test
    void calculates175PercentForTheWeek4Example() {
        assertEquals(175_000L, new LuxuryTier().calculatePrice(BASE_PRICE));
    }

    @Test
    void roundsDownForSmallAndNonRoundBasePrices() {
        LuxuryTier tier = new LuxuryTier();

        assertEquals(0L, tier.calculatePrice(0L));
        assertEquals(1L, tier.calculatePrice(1L));
        assertEquals(99L, tier.calculatePrice(57L));
        assertEquals(173L, tier.calculatePrice(99L));
        assertEquals(176L, tier.calculatePrice(101L));
        assertEquals(187L, tier.calculatePrice(107L));
    }

    @Test
    void supportsTheLargestBasePriceWhoseResultFitsInLong() {
        BigInteger largestBase = LONG_MAX.multiply(BigInteger.valueOf(100L))
                .add(BigInteger.valueOf(99L))
                .divide(BigInteger.valueOf(175L));
        long basePrice = largestBase.longValueExact();
        long expected = largestBase.multiply(BigInteger.valueOf(175L))
                .divide(BigInteger.valueOf(100L))
                .longValueExact();

        assertEquals(expected, new LuxuryTier().calculatePrice(basePrice));
    }

    @Test
    void rejectsOverflowAndNegativeBasePricesClearly() {
        BigInteger largestBase = LONG_MAX.multiply(BigInteger.valueOf(100L))
                .add(BigInteger.valueOf(99L))
                .divide(BigInteger.valueOf(175L));

        assertThrows(
                ArithmeticException.class,
                () -> new LuxuryTier().calculatePrice(largestBase.longValueExact() + 1L));
        assertThrows(IllegalArgumentException.class, () -> new LuxuryTier().calculatePrice(-1L));
    }

    @Test
    void allSeatTiersUseTheSamePricingTierContract() {
        List<PricingTier> tiers = List.of(
                new StandardTier(), new PremiumTier(), new ReclinerTier(), new LuxuryTier());
        List<Long> expectedPrices = List.of(100_000L, 125_000L, 150_000L, 175_000L);

        for (int index = 0; index < tiers.size(); index++) {
            Seat seat = new Seat("W4-" + index, tiers.get(index));
            assertEquals(expectedPrices.get(index).longValue(), seat.price(BASE_PRICE));
        }
    }
}
