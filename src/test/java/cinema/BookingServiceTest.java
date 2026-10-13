package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookingServiceTest {
    private static final Instant INITIAL_TIME = Instant.parse("2026-10-14T12:00:00Z");
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

    @Test
    void createBookingStartsPending() {
        TestContext context = createContext("A1");

        Booking booking = context.service().createBooking(
                "B1", context.showtime(), Set.of("A1"), new NoDiscountPolicy());

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(SeatState.LOCKED, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void confirmMovesPendingBookingToConfirmed() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");

        context.service().confirmBooking(booking);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void cancelMovesPendingBookingToCancelledAndReleasesSeats() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");

        context.service().cancelBooking(booking);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void cancelledBookingCannotBeConfirmed() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");
        context.service().cancelBooking(booking);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> context.service().confirmBooking(booking));

        assertEquals("Booking must be PENDING.", exception.getMessage());
    }

    @Test
    void confirmedBookingCannotBeCancelled() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");
        context.service().confirmBooking(booking);

        assertThrows(IllegalStateException.class, () -> context.service().cancelBooking(booking));
    }

    @Test
    void bookingCannotBeConfirmedTwice() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");
        context.service().confirmBooking(booking);

        assertThrows(IllegalStateException.class, () -> context.service().confirmBooking(booking));
    }

    @Test
    void expiredLockMovesBookingToExpired() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");
        context.clock().advance(LOCK_DURATION);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> context.service().confirmBooking(booking));

        assertEquals(BookingStatus.EXPIRED, booking.getStatus());
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A1"));
        assertEquals("Booking lock expired for B1.", exception.getMessage());
    }

    @Test
    void failedCreateDoesNotLeavePartialLock() {
        TestContext context = createContext("A1", "A2");
        createBooking(context, "B1", "A1");

        assertThrows(
                IllegalStateException.class,
                () -> context.service().createBooking(
                        "B2", context.showtime(), Set.of("A1", "A2"), new NoDiscountPolicy()));

        assertEquals(SeatState.LOCKED, context.showtime().getSeatStates().get("A1"));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A2"));
    }

    @Test
    void bookingCannotUseSeatLockedByAnotherBooking() {
        TestContext context = createContext("A1");
        createBooking(context, "B1", "A1");

        assertThrows(
                IllegalStateException.class,
                () -> context.service().createBooking(
                        "B2", context.showtime(), Set.of("A1"), new NoDiscountPolicy()));
    }

    @Test
    void pendingBookingCannotGenerateConfirmation() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");

        assertThrows(IllegalStateException.class, booking::generateConfirmation);
    }

    @Test
    void cancelledBookingCannotGenerateConfirmation() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");
        context.service().cancelBooking(booking);

        assertThrows(IllegalStateException.class, booking::generateConfirmation);
    }

    @Test
    void expiredBookingCannotGenerateConfirmation() {
        TestContext context = createContext("A1");
        Booking booking = createBooking(context, "B1", "A1");
        context.clock().advance(LOCK_DURATION);
        assertThrows(IllegalStateException.class, () -> context.service().confirmBooking(booking));

        assertThrows(IllegalStateException.class, booking::generateConfirmation);
    }

    @Test
    void duplicateBookingIdCannotLockDifferentSeats() {
        TestContext context = createContext("A1", "A2");
        Booking first = createBooking(context, "B1", "A1");

        assertThrows(IllegalStateException.class, () -> createBooking(context, "B1", "A2"));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A2"));
        context.service().confirmBooking(first);
        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A1"));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A2"));
    }

    @Test
    void cancelledBookingIdCannotBeReused() {
        TestContext context = createContext("A1");
        Booking first = createBooking(context, "B1", "A1");
        context.service().cancelBooking(first);

        assertThrows(IllegalStateException.class, () -> createBooking(context, "B1", "A1"));
        assertEquals(BookingStatus.CANCELLED, first.getStatus());
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A1"));
    }

    private Booking createBooking(TestContext context, String bookingId, String seatId) {
        return context.service().createBooking(
                bookingId, context.showtime(), Set.of(seatId), new NoDiscountPolicy());
    }

    private TestContext createContext(String... seatIds) {
        Movie movie = new Movie("M-SERVICE", "Booking Service Test", 120);
        Screen screen = new Screen("S-SERVICE", "Booking Service Screen");
        for (String seatId : seatIds) {
            screen.addSeat(new Seat(seatId, new StandardTier()));
        }
        MutableClock clock = new MutableClock(INITIAL_TIME, ZoneOffset.UTC);
        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 14, 20, 0),
                clock,
                LOCK_DURATION);
        return new TestContext(showtime, clock, new BookingService());
    }

    private record TestContext(Showtime showtime, MutableClock clock, BookingService service) {
    }
}
