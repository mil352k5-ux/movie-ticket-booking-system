package cinema;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookingPricingTest {

    @Test
    void calculatesSubtotalStudentDiscountAndTotal() {
        Booking booking = createBooking(new StudentDiscountPolicy());

        assertAll(
                () -> assertEquals(225_000L, booking.calculateSubtotal()),
                () -> assertEquals(22_500L, booking.calculateDiscount()),
                () -> assertEquals(202_500L, booking.calculateTotal()));
    }

    @Test
    void keepsSubtotalWhenNoDiscountPolicyIsUsed() {
        Booking booking = createBooking(new NoDiscountPolicy());

        assertAll(
                () -> assertEquals(225_000L, booking.calculateSubtotal()),
                () -> assertEquals(0L, booking.calculateDiscount()),
                () -> assertEquals(225_000L, booking.calculateTotal()));
    }

    private Booking createBooking(DiscountPolicy discountPolicy) {
        Movie movie = new Movie("M-W2", "Interstellar", 169);
        Screen screen = new Screen("S-W2", "Pricing Test Screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        screen.addSeat(new Seat("A2", new PremiumTier()));
        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 12, 19, 30));

        return new Booking("B-W2", showtime, Set.of("A1", "A2"), discountPolicy);
    }
}
