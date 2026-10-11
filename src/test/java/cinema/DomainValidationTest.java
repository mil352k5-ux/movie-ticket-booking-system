package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DomainValidationTest {

    @Test
    void rejectsBlankMovieId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Movie(" ", "Movie", 120));

        assertEquals("Movie ID must not be blank.", exception.getMessage());
    }

    @Test
    void rejectsBlankMovieTitle() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Movie("M1", "", 120));

        assertEquals("Movie title must not be blank.", exception.getMessage());
    }

    @Test
    void rejectsNonPositiveMovieDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Movie("M1", "Movie", 0));
        assertThrows(IllegalArgumentException.class, () -> new Movie("M1", "Movie", -1));
    }

    @Test
    void rejectsBlankScreenIdAndName() {
        assertThrows(IllegalArgumentException.class, () -> new Screen("", "Screen"));
        assertThrows(IllegalArgumentException.class, () -> new Screen("S1", " "));
    }

    @Test
    void rejectsBlankSeatIdAndNullPricingTier() {
        assertThrows(IllegalArgumentException.class, () -> new Seat(" ", new StandardTier()));
        assertThrows(IllegalArgumentException.class, () -> new Seat("A1", null));
    }

    @Test
    void rejectsNullSeatAndDuplicateSeatId() {
        Screen screen = new Screen("S1", "Screen");
        assertThrows(IllegalArgumentException.class, () -> screen.addSeat(null));

        screen.addSeat(new Seat("A1", new StandardTier()));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> screen.addSeat(new Seat("A1", new PremiumTier())));

        assertEquals("Duplicate seat: A1", exception.getMessage());
    }

    @Test
    void rejectsNullShowtimeDependencies() {
        Movie movie = new Movie("M1", "Movie", 120);
        Screen screen = new Screen("S1", "Screen");
        LocalDateTime startTime = LocalDateTime.of(2026, 10, 13, 19, 30);

        assertThrows(IllegalArgumentException.class, () -> new Showtime(null, screen, startTime));
        assertThrows(IllegalArgumentException.class, () -> new Showtime(movie, null, startTime));
        assertThrows(IllegalArgumentException.class, () -> new Showtime(movie, screen, null));
    }

    @Test
    void rejectsBookingWithoutSeats() {
        Showtime showtime = createShowtime("A1");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Booking("B1", showtime, Set.of(), new NoDiscountPolicy()));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Booking("B1", showtime, null, new NoDiscountPolicy()));
    }

    @Test
    void rejectsBookingWithUnknownSeat() {
        Showtime showtime = createShowtime("A1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Booking("B1", showtime, Set.of("UNKNOWN"), new NoDiscountPolicy()));

        assertEquals("Unknown seat: UNKNOWN", exception.getMessage());
    }

    @Test
    void rejectsBlankBookingIdAndNullDiscountPolicy() {
        Showtime showtime = createShowtime("A1");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Booking(" ", showtime, Set.of("A1"), new NoDiscountPolicy()));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Booking("B1", showtime, Set.of("A1"), null));
    }

    @Test
    void rejectsBlankBookingIdWhenLockingSeats() {
        Showtime showtime = createShowtime("A1");

        assertThrows(
                IllegalArgumentException.class,
                () -> showtime.tryLockSeats(Set.of("A1"), " "));
    }

    @Test
    void rejectsEmptySeatSetWhenLocking() {
        Showtime showtime = createShowtime("A1");

        assertThrows(
                IllegalArgumentException.class,
                () -> showtime.tryLockSeats(Set.of(), "B1"));
        assertThrows(
                IllegalArgumentException.class,
                () -> showtime.tryLockSeats(null, "B1"));
    }

    @Test
    void rejectsUnknownSeatWhenLocking() {
        Showtime showtime = createShowtime("A1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> showtime.tryLockSeats(Set.of("UNKNOWN"), "B1"));

        assertEquals("Unknown seat: UNKNOWN", exception.getMessage());
    }

    @Test
    void lockingMultipleSeatsIsAllOrNothing() {
        Showtime showtime = createShowtime("A1", "A2");
        showtime.tryLockSeats(Set.of("A1"), "B1");

        assertThrows(
                IllegalStateException.class,
                () -> showtime.tryLockSeats(Set.of("A1", "A2"), "B2"));

        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A2"));
    }

    @Test
    void doubleBookingRemainsRejected() {
        Showtime showtime = createShowtime("A1");
        showtime.tryLockSeats(Set.of("A1"), "B1");

        assertThrows(
                IllegalStateException.class,
                () -> showtime.tryLockSeats(Set.of("A1"), "B2"));

        showtime.confirmBooking("B1");
        assertThrows(
                IllegalStateException.class,
                () -> showtime.tryLockSeats(Set.of("A1"), "B3"));
    }

    private Showtime createShowtime(String... seatIds) {
        Movie movie = new Movie("M-VALIDATION", "Validation Test", 120);
        Screen screen = new Screen("S-VALIDATION", "Validation Screen");
        for (String seatId : seatIds) {
            screen.addSeat(new Seat(seatId, new StandardTier()));
        }
        return new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 13, 20, 0));
    }
}
