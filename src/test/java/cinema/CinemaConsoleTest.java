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
