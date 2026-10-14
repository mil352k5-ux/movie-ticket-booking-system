package cinema;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MemberDiscountPolicyTest {
    private final DiscountPolicy policy = new MemberDiscountPolicy();

    @Test
    void takesFifteenPercentRegardlessOfGroupSize() {
        assertAll(
                () -> assertEquals(33_750L, policy.calculateDiscount(225_000L, 1)),
                () -> assertEquals(33_750L, policy.calculateDiscount(225_000L, 2)),
                () -> assertEquals(33_750L, policy.calculateDiscount(225_000L, 5)));
    }

    @Test
    void givesNoDiscountForZeroSubtotal() {
        assertEquals(0L, policy.calculateDiscount(0L, 0));
    }

    @Test
    void roundsDownToWholeVndWithoutDroppingTheRemainderTooEarly() {
        long[][] examples = {
                {1L, 0L}, {6L, 0L}, {7L, 1L}, {99L, 14L},
                {101L, 15L}, {107L, 16L}, {199L, 29L},
                {225_001L, 33_750L}, {225_007L, 33_751L}
        };
        for (long[] example : examples) {
            assertEquals(example[1], policy.calculateDiscount(example[0], 2),
                    "Subtotal: " + example[0]);
        }
    }

    @Test
    void supportsTheLargestLongSubtotalWithoutIntermediateOverflow() {
        assertEquals(1_383_505_805_528_216_371L,
                policy.calculateDiscount(Long.MAX_VALUE, 2));
    }

    @Test
    void matchesAnExactBigIntegerOracleAcrossBoundaryAndGeneratedAmounts() {
        long[] boundaries = {0L, 1L, 99L, 100L, 107L, Long.MAX_VALUE / 15,
                Long.MAX_VALUE / 15 + 1, Long.MAX_VALUE - 1, Long.MAX_VALUE};
        for (long subtotal : boundaries) {
            assertMatchesOracle(subtotal);
        }
        Random random = new Random(23110062L);
        for (int index = 0; index < 2_048; index++) {
            assertMatchesOracle(random.nextLong() & Long.MAX_VALUE);
        }
    }

    @Test
    void rejectsANegativeSubtotal() {
        assertThrows(IllegalArgumentException.class, () -> policy.calculateDiscount(-1L, 1));
        assertThrows(IllegalArgumentException.class,
                () -> policy.calculateDiscount(Long.MIN_VALUE, 1));
    }

    @Test
    void namesThePolicyForConfirmationSummaries() {
        assertEquals("Member discount 15%", policy.name());
    }

    private void assertMatchesOracle(long subtotal) {
        long expected = BigInteger.valueOf(subtotal).multiply(BigInteger.valueOf(15))
                .divide(BigInteger.valueOf(100)).longValueExact();
        long actual = policy.calculateDiscount(subtotal, 2);
        assertEquals(expected, actual, "Subtotal: " + subtotal);
        assertTrue(actual >= 0 && actual <= subtotal);
    }
}
