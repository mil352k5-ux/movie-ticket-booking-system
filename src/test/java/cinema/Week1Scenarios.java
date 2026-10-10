package cinema;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Executable week 1 scenarios. Run this class with assertions implemented
 * locally so the project does not need an external test framework.
 */
public final class Week1Scenarios {
    private static final long BASE_PRICE = 100_000L;

    private Week1Scenarios() {
    }

    public static void main(String[] args) {
        happyPathScenario();
        System.out.println("WEEK 1 HAPPY PATH: PASSED");

        doubleBookingScenario();
        System.out.println("WEEK 1 DOUBLE BOOKING: PASSED");

        pricingPolymorphismScenario();
        System.out.println("WEEK 1 PRICING POLYMORPHISM: PASSED");

        System.out.println("ALL WEEK 1 SCENARIOS PASSED: 3");
    }

    private static void happyPathScenario() {
        Movie movie = new Movie("W1-M001", "Interstellar", 169);
        Screen screen = new Screen("W1-S001", "Week 1 Screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        screen.addSeat(new Seat("A2", new PremiumTier()));
        screen.addSeat(new Seat("A3", new ReclinerTier()));

        LocalDateTime startTime = LocalDateTime.of(2026, 10, 10, 19, 30);
        Showtime showtime = new Showtime(movie, screen, startTime);
        String bookingId = "W1-B001";
        Set<String> selectedSeats = Set.of("A1", "A2");

        showtime.tryLockSeats(selectedSeats, bookingId);
        Booking booking = new Booking(
                bookingId,
                showtime,
                selectedSeats,
                new StudentDiscountPolicy());
        showtime.confirmBooking(bookingId);

        String confirmation = booking.generateConfirmation();
        assertContains(confirmation, "Movie: Interstellar", "confirmation movie");
        assertContains(confirmation, "Time: 2026-10-10 19:30", "confirmation time");
        assertContains(confirmation, "Seats: A1, A2", "confirmation seats");
        assertEquals(202_500L, booking.calculateTotal(), "student-discount total");
        assertContains(
                confirmation,
                "Total: " + String.format("%,d", booking.calculateTotal()) + " VND",
                "confirmation total");
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"), "A1 state");
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A2"), "A2 state");
    }

    private static void doubleBookingScenario() {
        Movie movie = new Movie("W1-M002", "Arrival", 116);
        Screen screen = new Screen("W1-S002", "Validation Screen");
        screen.addSeat(new Seat("B1", new StandardTier()));
        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 11, 20, 0));

        showtime.tryLockSeats(Set.of("B1"), "W1-B002");
        assertThrows(
                IllegalStateException.class,
                () -> showtime.tryLockSeats(Set.of("B1"), "W1-B003"),
                "locking an already LOCKED seat");

        showtime.confirmBooking("W1-B002");
        assertThrows(
                IllegalStateException.class,
                () -> showtime.tryLockSeats(Set.of("B1"), "W1-B004"),
                "locking an already BOOKED seat");
    }

    private static void pricingPolymorphismScenario() {
        Seat[] seats = {
                new Seat("C1", new StandardTier()),
                new Seat("C2", new PremiumTier()),
                new Seat("C3", new ReclinerTier())
        };
        long[] expectedPrices = {BASE_PRICE, 125_000L, 150_000L};

        for (int i = 0; i < seats.length; i++) {
            assertEquals(expectedPrices[i], seats[i].price(BASE_PRICE), "seat price " + i);
        }
        assertTrue(seats[0].price(BASE_PRICE) != seats[1].price(BASE_PRICE),
                "Standard and Premium prices must differ");
        assertTrue(seats[1].price(BASE_PRICE) != seats[2].price(BASE_PRICE),
                "Premium and Recliner prices must differ");

        PricingTier discountedTier = new PricingTier() {
            @Override
            public long calculatePrice(long basePrice) {
                return Math.multiplyExact(basePrice, 80) / 100;
            }

            @Override
            public String name() {
                return "Test discounted tier";
            }
        };
        Seat sameContractWithNewImplementation = new Seat("C4", discountedTier);
        assertEquals(80_000L, sameContractWithNewImplementation.price(BASE_PRICE),
                "new PricingTier implementation");
    }

    private static void assertContains(String actual, String expected, String description) {
        assertTrue(actual.contains(expected), description + " should contain '" + expected + "'");
    }

    private static void assertEquals(Object expected, Object actual, String description) {
        if (!expected.equals(actual)) {
            throw new AssertionError(description + " expected <" + expected + "> but was <" + actual + ">");
        }
    }

    private static void assertEquals(long expected, long actual, String description) {
        if (expected != actual) {
            throw new AssertionError(description + " expected <" + expected + "> but was <" + actual + ">");
        }
    }

    private static void assertTrue(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
    }

    private static void assertThrows(
            Class<? extends Throwable> expectedType,
            ThrowingAction action,
            String description) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expectedType.isInstance(actual)) {
                return;
            }
            throw new AssertionError(
                    description + " expected " + expectedType.getSimpleName()
                            + " but was " + actual.getClass().getSimpleName(),
                    actual);
        }
        throw new AssertionError(description + " should throw " + expectedType.getSimpleName());
    }

    @FunctionalInterface
    private interface ThrowingAction {
        void run();
    }
}
