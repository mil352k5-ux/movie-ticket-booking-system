package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GroupDiscountPolicyTest {

    @Test
    void bookingWithFourSeatsGetsNoGroupDiscount() {
        Booking booking = createBooking(new GroupDiscountPolicy(), 4);

        assertEquals(0L, booking.calculateDiscount());
    }

    @Test
    void bookingWithExactlyFiveSeatsGetsFivePercentDiscount() {
        Booking booking = createBooking(new GroupDiscountPolicy(), 5);

        assertEquals(25_000L, booking.calculateDiscount());
    }

    @Test
    void bookingWithMoreThanFiveSeatsStillGetsFivePercentDiscount() {
        Booking booking = createBooking(new GroupDiscountPolicy(), 6);

        assertEquals(30_000L, booking.calculateDiscount());
    }

    @Test
    void studentPolicyStillGetsTenPercentDiscount() {
        Booking booking = createBooking(new StudentDiscountPolicy(), 5);

        assertEquals(50_000L, booking.calculateDiscount());
    }

    @Test
    void bookingUsesDiscountPolicyContractWithoutDependingOnConcreteSubtype() {
        DiscountPolicy policy = new GroupDiscountPolicy();
        Booking booking = createBooking(policy, 5);

        assertEquals(25_000L, booking.calculateDiscount());
    }

    private Booking createBooking(DiscountPolicy policy, int seatCount) {
        Movie movie = new Movie("M-GROUP", "Group Discount Test", 120);
        Screen screen = new Screen("S-GROUP", "Group Discount Screen");
        Set<String> seatIds = new java.util.LinkedHashSet<>();
        for (int index = 1; index <= seatCount; index++) {
            String seatId = "G" + index;
            screen.addSeat(new Seat(seatId, new StandardTier()));
            seatIds.add(seatId);
        }
        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 13, 19, 30));

        return new Booking("B-GROUP", showtime, seatIds, policy);
    }
}
