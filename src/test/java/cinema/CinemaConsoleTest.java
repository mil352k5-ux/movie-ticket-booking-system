package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CinemaConsoleTest {
    @TempDir
    Path directory;

    @Test
    void browsesShowtimeBooksSeatsAndSavesConfirmation() throws Exception {
        Showtime showtime = createShowtime();
        String output = runConsole(showtime, "1\nA1,A2\n1\ny\n0\n");

        assertTrue(output.contains("Interstellar"));
        assertTrue(output.contains("Premium"));
        assertTrue(output.contains("Booking confirmed successfully."));
        assertTrue(output.contains("Total: " + String.format("%,d", 202_500L) + " VND"));
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"));
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A2"));
        String bookingId = output.lines().filter(line -> line.startsWith("Booking ID: "))
                .findFirst().orElseThrow().substring("Booking ID: ".length());
        assertTrue(new BookingHistory(directory).find(bookingId).orElseThrow().contains("Seats: A1, A2"));
    }

    @Test
    void selectsMemberDiscountConfirmsAndSavesTheSamePricingToHistory() throws Exception {
        Showtime showtime = createShowtime();
        String output = runConsole(showtime, "1\nA1,A2\n3\ny\n0\n");

        assertTrue(output.contains("3 = member"));
        assertTrue(output.contains("Booking confirmed successfully."));
        assertTrue(output.contains("Discount (Member discount 15%): "
                + String.format("%,d", 33_750L) + " VND"));
        assertTrue(output.contains("Total: " + String.format("%,d", 191_250L) + " VND"));
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"));
        assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A2"));
        String bookingId = output.lines().filter(line -> line.startsWith("Booking ID: "))
                .findFirst().orElseThrow().substring("Booking ID: ".length());
        String saved = new BookingHistory(directory).find(bookingId).orElseThrow();
        assertTrue(saved.contains("Seats: A1, A2"));
        assertTrue(saved.contains("Discount (Member discount 15%): "
                + String.format("%,d", 33_750L) + " VND"));
        assertTrue(saved.contains("Total: " + String.format("%,d", 191_250L) + " VND"));
        assertTrue(output.contains(saved));
        try (var files = Files.list(directory)) {
            assertEquals(1L, files.count());
        }
    }

    @Test
    void cancellationWithMemberDiscountReleasesSeatsAndCreatesNoHistory() throws Exception {
        Showtime showtime = createShowtime();
        String output = runConsole(showtime, "1\nA1,A2\n3\nn\n0\n");

        assertTrue(output.contains("Total: " + String.format("%,d", 191_250L) + " VND"));
        assertTrue(output.contains("Booking cancelled."));
        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A1"));
        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A2"));
        try (var files = Files.list(directory)) {
            assertEquals(0L, files.count());
        }
    }

    @Test
    void rejectsUnknownDiscountOptionBeforeAnySeatsAreLocked() throws Exception {
        Showtime showtime = createShowtime();
        String output = runConsole(showtime, "1\nA1,A2\n4\n0\n");

        assertTrue(output.contains("Rejected: Unknown discount option."));
        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A1"));
        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A2"));
        try (var files = Files.list(directory)) {
            assertEquals(0L, files.count());
        }
    }

    @Test
    void keepsTheExistingGroupDiscountAtOptionTwo() throws Exception {
        Screen screen = new Screen("S-UI-GROUP", "Group test screen");
        for (int index = 1; index <= 5; index++) {
            screen.addSeat(new Seat("A" + index, new StandardTier()));
        }
        Showtime showtime = new Showtime(new Movie("M-UI-GROUP", "Interstellar", 169), screen,
                LocalDateTime.of(2026, 10, 20, 19, 30));
        String output = runConsole(showtime, "1\nA1,A2,A3,A4,A5\n2\ny\n0\n");

        assertTrue(output.contains("Booking confirmed successfully."));
        assertTrue(output.contains("Discount (Group discount 5% for 5 or more seats): "
                + String.format("%,d", 25_000L) + " VND"));
        assertTrue(output.contains("Total: " + String.format("%,d", 475_000L) + " VND"));
        for (int index = 1; index <= 5; index++) {
            assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A" + index));
        }
    }

    @Test
    void rejectsUnknownSeatWithoutLeavingAPartialLock() {
        Showtime showtime = createShowtime();
        String output = runConsole(showtime, "1\nA1,UNKNOWN\n0\n0\n");

        assertTrue(output.contains("Unknown seat: UNKNOWN"));
        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A1"));
        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A2"));
    }

    @Test
    void cancellationReleasesSeatAndDoesNotSaveAConfirmation() throws Exception {
        Showtime showtime = createShowtime();
        String output = runConsole(showtime, "1\nA1\n0\nn\n0\n");

        assertTrue(output.contains("Booking cancelled."));
        assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A1"));
        try (var files = Files.list(directory)) {
            assertEquals(0, files.count());
        }
    }

    private String runConsole(Showtime showtime, String commands) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new CinemaConsole(List.of(showtime), new BookingService(), new BookingHistory(directory),
                new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(output, true, StandardCharsets.UTF_8)).run();
        return output.toString(StandardCharsets.UTF_8);
    }

    private Showtime createShowtime() {
        Screen screen = new Screen("S-UI", "Console test screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        screen.addSeat(new Seat("A2", new PremiumTier()));
        return new Showtime(new Movie("M-UI", "Interstellar", 169), screen,
                LocalDateTime.of(2026, 10, 20, 19, 30));
    }
}
