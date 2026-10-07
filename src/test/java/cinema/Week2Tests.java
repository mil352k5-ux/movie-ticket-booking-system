package cinema;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Week2Tests {
    private static int passed;

    private Week2Tests() {
    }

    public static void main(String[] args) {
        testPricingPolymorphism();
        testStudentDiscount();
        testGroupDiscountThreshold();
        testBookingValidation();
        testSeatReleaseAndConfirmation();
        testDuplicateSeatValidation();
        System.out.println("ALL WEEK 2 TESTS PASSED: " + passed);
    }

    private static void testPricingPolymorphism() {
        assertEquals(100_000, new StandardTier().calculatePrice(100_000), "Standard price");
        assertEquals(125_000, new PremiumTier().calculatePrice(100_000), "Premium price");
        assertEquals(150_000, new ReclinerTier().calculatePrice(100_000), "Recliner price");
    }

    private static void testStudentDiscount() {
        Showtime showtime = createShowtime(2);
        Set<String> seats = Set.of("A1", "A2");
        Booking booking = new Booking("B-STUDENT", showtime, seats, new StudentDiscountPolicy());
        assertEquals(225_000, booking.calculateSubtotal(), "Student subtotal");
        assertEquals(22_500, booking.calculateDiscount(), "Student discount");
        assertEquals(202_500, booking.calculateTotal(), "Student total");
    }

    private static void testGroupDiscountThreshold() {
        Showtime showtime = createShowtime(5);
        Set<String> seats = new LinkedHashSet<>(Set.of("A1", "A2", "A3", "A4", "A5"));
        Booking groupBooking = new Booking("B-GROUP", showtime, seats, new GroupDiscountPolicy());
        assertEquals(30_000, groupBooking.calculateDiscount(), "Group discount for 5 seats");

        Showtime smallShowtime = createShowtime(4);
        Booking smallBooking = new Booking(
                "B-SMALL",
                smallShowtime,
                Set.of("A1", "A2", "A3", "A4"),
                new GroupDiscountPolicy());
        assertEquals(0, smallBooking.calculateDiscount(), "No group discount below threshold");
    }

    private static void testBookingValidation() {
        Showtime showtime = createShowtime(2);
        assertThrows(() -> new Booking("B-EMPTY", showtime, Set.of(), new NoDiscountPolicy()),
                "Empty booking must be rejected");
        assertThrows(() -> new Booking("B-UNKNOWN", showtime, Set.of("Z9"), new NoDiscountPolicy()),
                "Unknown seat must be rejected");
        assertThrows(() -> showtime.tryLockSeats(Set.of(), "B-EMPTY-LOCK"),
                "Empty lock request must be rejected");
    }

    private static void testSeatReleaseAndConfirmation() {
        Showtime showtime = createShowtime(2);
        showtime.tryLockSeats(Set.of("A1"), "B-RELEASE");
        assertFalse(showtime.isSeatAvailable("A1"), "Locked seat is unavailable");
        showtime.releaseSeats("B-RELEASE");
        assertTrue(showtime.isSeatAvailable("A1"), "Released seat is available");

        showtime.tryLockSeats(Set.of("A1"), "B-CONFIRM");
        showtime.confirmBooking("B-CONFIRM");
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"), "Confirmed seat state");
        assertThrows(() -> showtime.tryLockSeats(Set.of("A1"), "B-OTHER"),
                "Booked seat must reject another booking");
    }

    private static void testDuplicateSeatValidation() {
        Screen screen = new Screen("S-DUP", "Duplicate test screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        assertThrows(() -> screen.addSeat(new Seat("A1", new PremiumTier())),
                "Duplicate seat ID must be rejected");
    }

    private static Showtime createShowtime(int seatCount) {
        Movie movie = new Movie("M-TEST", "Test Movie", 120);
        Screen screen = new Screen("S-TEST", "Test Screen");
        PricingTier[] tiers = {new StandardTier(), new PremiumTier(), new ReclinerTier()};
        for (int index = 1; index <= seatCount; index++) {
            screen.addSeat(new Seat("A" + index, tiers[(index - 1) % tiers.length]));
        }
        return new Showtime(movie, screen, LocalDateTime.of(2026, 10, 12, 19, 30));
    }

    private static void assertEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.001) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
        passed++;
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
        passed++;
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        passed++;
    }

    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    private static void assertThrows(Runnable action, String message) {
        try {
            action.run();
        } catch (RuntimeException expected) {
            passed++;
            return;
        }
        throw new AssertionError(message);
    }
}
