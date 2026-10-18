package cinema;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MemberDiscountBookingTest {
    private final BookingService service = new BookingService();

    @Test
    void swapsPoliciesThroughTheInterfaceWithoutChangingTheBookingFlow() {
        DiscountPolicy[] policies = {
                new NoDiscountPolicy(), new StudentDiscountPolicy(), new MemberDiscountPolicy()
        };
        long[] discounts = {0L, 22_500L, 33_750L};
        long[] totals = {225_000L, 202_500L, 191_250L};

        for (int index = 0; index < policies.length; index++) {
            Showtime showtime = createShowtime();
            Booking booking = service.createBooking("B-W4-" + index, showtime,
                    Set.of("A1", "A2"), policies[index]);
            assertEquals(225_000L, booking.calculateSubtotal());
            assertEquals(discounts[index], booking.calculateDiscount());
            assertEquals(totals[index], booking.calculateTotal());
            service.confirmBooking(booking);
            assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
            assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"));
            assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A2"));
        }
    }

    @Test
    void confirmsMemberBookingAndIncludesItsPricingInTheSummary() {
        Showtime showtime = createShowtime();
        Booking booking = service.createBooking("B-W4-MEMBER", showtime,
                Set.of("A1", "A2"), new MemberDiscountPolicy());

        assertAll(
                () -> assertEquals(BookingStatus.PENDING, booking.getStatus()),
                () -> assertEquals(SeatState.LOCKED, showtime.getSeatStates().get("A1")),
                () -> assertEquals(SeatState.LOCKED, showtime.getSeatStates().get("A2")),
                () -> assertThrows(IllegalStateException.class, booking::generateConfirmation));
        service.confirmBooking(booking);

        String confirmation = booking.generateConfirmation();
        assertAll(
                () -> assertEquals(BookingStatus.CONFIRMED, booking.getStatus()),
                () -> assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1")),
                () -> assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A2")),
                () -> assertTrue(confirmation.contains("Movie: Interstellar")),
                () -> assertTrue(confirmation.contains("Time: 2026-10-20 19:30")),
                () -> assertTrue(confirmation.contains("Seats: A1, A2")),
                () -> assertTrue(confirmation.contains("Subtotal: "
                        + String.format("%,d", 225_000L) + " VND")),
                () -> assertTrue(confirmation.contains("Discount (Member discount 15%): "
                        + String.format("%,d", 33_750L) + " VND")),
                () -> assertTrue(confirmation.contains("Total: "
                        + String.format("%,d", 191_250L) + " VND")));
    }

    @Test
    void rejectsDoubleBookingEvenWhenTheSecondCustomerChoosesAnotherPolicy() {
        Showtime showtime = createShowtime();
        Booking member = service.createBooking("B-W4-FIRST", showtime,
                Set.of("A1", "A2"), new MemberDiscountPolicy());
        service.confirmBooking(member);

        assertThrows(IllegalStateException.class,
                () -> service.createBooking("B-W4-SECOND", showtime,
                        Set.of("A1"), new StudentDiscountPolicy()));
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"));
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A2"));
    }

    @Test
    void cancelsMemberBookingAndAllowsTheReleasedSeatsToBeBookedAgain() {
        Showtime showtime = createShowtime();
        Booking member = service.createBooking("B-W4-CANCEL", showtime,
                Set.of("A1", "A2"), new MemberDiscountPolicy());
        service.cancelBooking(member);

        assertAll(
                () -> assertEquals(BookingStatus.CANCELLED, member.getStatus()),
                () -> assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A1")),
                () -> assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A2")),
                () -> assertThrows(IllegalStateException.class, member::generateConfirmation));
        Booking replacement = service.createBooking("B-W4-REPLACEMENT", showtime,
                Set.of("A1", "A2"), new MemberDiscountPolicy());
        service.confirmBooking(replacement);
        assertEquals(191_250L, replacement.calculateTotal());
        assertEquals(BookingStatus.CONFIRMED, replacement.getStatus());
    }

    private Showtime createShowtime() {
        Screen screen = new Screen("S-W4", "Member discount test screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        screen.addSeat(new Seat("A2", new PremiumTier()));
        return new Showtime(new Movie("M-W4", "Interstellar", 169), screen,
                LocalDateTime.of(2026, 10, 20, 19, 30));
    }
}
