package cinema;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Set;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Movie movie = new Movie("M001", "Interstellar", 169);
        Screen screen = new Screen("S001", "Screen 1");
        screen.addSeat(new Seat("A1", new StandardTier()));
        screen.addSeat(new Seat("A2", new PremiumTier()));
        screen.addSeat(new Seat("A3", new ReclinerTier()));

        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 10, 19, 30));

        System.out.println("=== HAPPY PATH ===");
        String firstBookingId = "B001";
        Set<String> firstSeats = Set.of("A1", "A2");
        BookingService service = new BookingService();
        Booking booking = service.createBooking(
                firstBookingId,
                showtime,
                firstSeats,
                new StudentDiscountPolicy());
        service.confirmBooking(booking);
        System.out.println(booking.generateConfirmation());

        Path historyDirectory = args.length == 0
                ? Path.of("data", "booking-history") : Path.of(args[0]);
        BookingHistory history = new BookingHistory(historyDirectory);
        try {
            history.save(booking);
            System.out.println("History reload matches confirmation: "
                    + history.find(firstBookingId).orElseThrow().equals(booking.generateConfirmation()));
        } catch (IOException exception) {
            System.err.println("Booking confirmed, but history could not be saved: " + exception.getMessage());
        }

        System.out.println();
        System.out.println("=== VALIDATION: DOUBLE BOOKING ===");
        try {
            service.createBooking("B002", showtime, Set.of("A1"), new NoDiscountPolicy());
            System.out.println("ERROR: invalid booking was accepted.");
        } catch (IllegalStateException exception) {
            System.out.println("Rejected correctly: " + exception.getMessage());
        }
    }
}
