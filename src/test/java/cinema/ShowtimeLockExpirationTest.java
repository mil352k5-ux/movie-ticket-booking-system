package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ShowtimeLockExpirationTest {
    private static final Instant INITIAL_TIME = Instant.parse("2026-10-12T12:00:00Z");
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

    @Test
    void newSeatStartsAvailable() {
        TestContext context = createContext("A1");

        assertTrue(context.showtime().isSeatAvailable("A1"));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void lockingSeatChangesItsStateToLocked() {
        TestContext context = createContext("A1");

        context.showtime().tryLockSeats(Set.of("A1"), "B1");

        assertFalse(context.showtime().isSeatAvailable("A1"));
        assertEquals(SeatState.LOCKED, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void activeLockCannotBeTakenByAnotherBooking() {
        TestContext context = createContext("A1");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");
        context.clock().advance(Duration.ofMinutes(4));

        assertThrows(
                IllegalStateException.class,
                () -> context.showtime().tryLockSeats(Set.of("A1"), "B2"));
    }

    @Test
    void expiredLockMakesSeatAvailableAgain() {
        TestContext context = createContext("A1");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");

        context.clock().advance(LOCK_DURATION);

        assertTrue(context.showtime().isSeatAvailable("A1"));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void anotherBookingCanLockSeatAfterExpiration() {
        TestContext context = createContext("A1");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");
        context.clock().advance(LOCK_DURATION);

        context.showtime().tryLockSeats(Set.of("A1"), "B2");

        assertEquals(SeatState.LOCKED, context.showtime().getSeatStates().get("A1"));
        assertThrows(IllegalStateException.class, () -> context.showtime().confirmBooking("B1"));
        context.showtime().confirmBooking("B2");
        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void expiredBookingCannotBeConfirmed() {
        TestContext context = createContext("A1");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");
        context.clock().advance(LOCK_DURATION.plusSeconds(1));

        assertThrows(IllegalStateException.class, () -> context.showtime().confirmBooking("B1"));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void confirmationBeforeExpirationBooksSeat() {
        TestContext context = createContext("A1");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");
        context.clock().advance(Duration.ofMinutes(4));

        context.showtime().confirmBooking("B1");

        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void bookedSeatDoesNotExpire() {
        TestContext context = createContext("A1");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");
        context.showtime().confirmBooking("B1");

        context.clock().advance(Duration.ofHours(1));

        assertFalse(context.showtime().isSeatAvailable("A1"));
        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A1"));
    }

    @Test
    void releaseOnlyAffectsLocksOwnedByBooking() {
        TestContext context = createContext("A1");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");

        context.showtime().releaseSeats("B2");
        assertEquals(SeatState.LOCKED, context.showtime().getSeatStates().get("A1"));

        context.showtime().releaseSeats("B1");
        assertTrue(context.showtime().isSeatAvailable("A1"));
    }

    @Test
    void lockingMultipleSeatsRemainsAllOrNothing() {
        TestContext context = createContext("A1", "A2");
        context.showtime().tryLockSeats(Set.of("A1"), "B1");

        assertThrows(
                IllegalStateException.class,
                () -> context.showtime().tryLockSeats(Set.of("A1", "A2"), "B2"));

        assertEquals(SeatState.LOCKED, context.showtime().getSeatStates().get("A1"));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A2"));
    }

    @Test
    void rejectsInvalidClockAndLockDuration() {
        Movie movie = new Movie("M1", "Movie", 120);
        Screen screen = new Screen("S1", "Screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        LocalDateTime startTime = LocalDateTime.of(2026, 10, 12, 19, 30);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Showtime(movie, screen, startTime, null, LOCK_DURATION));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Showtime(movie, screen, startTime, Clock.systemUTC(), null));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Showtime(movie, screen, startTime, Clock.systemUTC(), Duration.ZERO));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Showtime(movie, screen, startTime, Clock.systemUTC(), Duration.ofSeconds(-1)));
    }

    @Test
    void returnedSeatStateMapCannotBeModified() {
        TestContext context = createContext("A1");
        Map<String, SeatState> states = context.showtime().getSeatStates();

        assertThrows(UnsupportedOperationException.class, () -> states.put("A1", SeatState.BOOKED));
        assertEquals(SeatState.AVAILABLE, context.showtime().getSeatStates().get("A1"));
    }

    private TestContext createContext(String... seatIds) {
        Movie movie = new Movie("M-LOCK", "Lock Test", 120);
        Screen screen = new Screen("S-LOCK", "Lock Test Screen");
        for (String seatId : seatIds) {
            screen.addSeat(new Seat(seatId, new StandardTier()));
        }
        MutableClock clock = new MutableClock(INITIAL_TIME, ZoneOffset.UTC);
        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 12, 20, 0),
                clock,
                LOCK_DURATION);
        return new TestContext(showtime, clock);
    }

    private record TestContext(Showtime showtime, MutableClock clock) {
    }
}
