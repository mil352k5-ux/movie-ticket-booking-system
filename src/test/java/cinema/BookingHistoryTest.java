package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BookingHistoryTest {
    @TempDir
    Path directory;

    @Test
    void savesAndReloadsConfirmationWithANewHistoryInstance() throws Exception {
        Booking booking = confirmedBooking("B1", new NoDiscountPolicy());
        new BookingHistory(directory).save(booking);

        String reloaded = new BookingHistory(directory).find("B1").orElseThrow();

        assertEquals(booking.generateConfirmation(), reloaded);
        assertTrue(reloaded.contains("Phim kiểm thử"));
    }

    @Test
    void missingBookingReturnsEmpty() throws Exception {
        assertTrue(new BookingHistory(directory).find("MISSING").isEmpty());
    }

    @Test
    void rejectsPendingBookingWithoutSavingAConfirmation() throws Exception {
        BookingHistory history = new BookingHistory(directory);
        Booking booking = new BookingService().createBooking(
                "B1", createShowtime(), Set.of("A1"), new NoDiscountPolicy());

        assertThrows(IllegalStateException.class, () -> history.save(booking));
        assertTrue(history.find("B1").isEmpty());
    }

    @Test
    void repeatedSaveOfTheSameConfirmationIsIdempotent() throws Exception {
        Booking booking = confirmedBooking("B1", new NoDiscountPolicy());
        new BookingHistory(directory).save(booking);
        new BookingHistory(directory).save(booking);

        assertEquals(booking.generateConfirmation(), new BookingHistory(directory).find("B1").orElseThrow());
    }

    @Test
    void doesNotOverwriteDifferentConfirmationWithTheSameId() throws Exception {
        Booking original = confirmedBooking("B1", new NoDiscountPolicy());
        Booking changed = confirmedBooking("B1", new StudentDiscountPolicy());
        BookingHistory history = new BookingHistory(directory);
        history.save(original);

        assertThrows(IOException.class, () -> history.save(changed));
        assertEquals(original.generateConfirmation(), history.find("B1").orElseThrow());
    }

    @Test
    void bookingIdWithPathSeparatorsStaysInsideHistoryDirectory() throws Exception {
        Booking booking = confirmedBooking("../B1", new NoDiscountPolicy());
        BookingHistory history = new BookingHistory(directory);
        history.save(booking);

        assertEquals(booking.generateConfirmation(), history.find("../B1").orElseThrow());
        try (var files = Files.list(directory)) {
            assertEquals(1, files.count());
        }
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new BookingHistory(null));
        BookingHistory history = new BookingHistory(directory);
        assertThrows(IllegalArgumentException.class, () -> history.save(null));
        assertThrows(IllegalArgumentException.class, () -> history.find(null));
        assertThrows(IllegalArgumentException.class, () -> history.find(" "));
    }

    @Test
    void storageFailureDoesNotUndoConfirmedBooking() throws Exception {
        Path regularFile = Files.createTempFile(directory, "not-a-directory-", ".txt");
        Booking booking = confirmedBooking("B1", new NoDiscountPolicy());

        assertThrows(IOException.class, () -> new BookingHistory(regularFile).save(booking));
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(SeatState.BOOKED, booking.getShowtime().getSeatStates().get("A1"));
    }

    private Booking confirmedBooking(String id, DiscountPolicy policy) {
        BookingService service = new BookingService();
        Booking booking = service.createBooking(id, createShowtime(), Set.of("A1"), policy);
        service.confirmBooking(booking);
        return booking;
    }

    private Showtime createShowtime() {
        Screen screen = new Screen("S-HISTORY", "History screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        return new Showtime(new Movie("M-HISTORY", "Phim kiểm thử", 120), screen,
                LocalDateTime.of(2026, 10, 14, 19, 30),
                Clock.fixed(Instant.parse("2026-10-14T12:00:00Z"), ZoneOffset.UTC),
                Duration.ofMinutes(5));
    }
}
