package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LuxuryBookingFlowTest {

    @Test
    void bookingServiceConfirmsLuxurySeatAndPersistsConfirmation(@TempDir Path historyDirectory)
            throws IOException {
        TestContext context = createContext();

        Booking booking = context.service().createBooking(
                "B-W4-LUXURY",
                context.showtime(),
                Set.of("A1", "A6"),
                new NoDiscountPolicy());

        assertEquals(275_000L, booking.calculateSubtotal());
        context.service().confirmBooking(booking);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A1"));
        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A6"));

        String confirmation = booking.generateConfirmation();
        assertTrue(confirmation.contains("Seats: A1, A6"));
        assertTrue(confirmation.contains("Subtotal: " + String.format("%,d", 275_000L) + " VND"));
        assertTrue(confirmation.contains("Total: " + String.format("%,d", 275_000L) + " VND"));

        BookingHistory history = new BookingHistory(historyDirectory);
        history.save(booking);
        assertEquals(confirmation, history.find("B-W4-LUXURY").orElseThrow());
    }

    @Test
    void anotherBookingCannotReuseBookedLuxurySeat() {
        TestContext context = createContext();
        Booking first = context.service().createBooking(
                "B-W4-FIRST", context.showtime(), Set.of("A1", "A6"), new NoDiscountPolicy());
        context.service().confirmBooking(first);

        assertThrows(
                IllegalStateException.class,
                () -> context.service().createBooking(
                        "B-W4-SECOND", context.showtime(), Set.of("A6"), new NoDiscountPolicy()));
        assertEquals(SeatState.BOOKED, context.showtime().getSeatStates().get("A6"));
    }

    private TestContext createContext() {
        Movie movie = new Movie("M-W4-LUXURY", "Luxury Flow Test", 120);
        Screen screen = new Screen("S-W4-LUXURY", "Luxury Test Screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        screen.addSeat(new Seat("A6", new LuxuryTier()));
        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 20, 19, 30));
        return new TestContext(showtime, new BookingService());
    }

    private record TestContext(Showtime showtime, BookingService service) {
    }
}
