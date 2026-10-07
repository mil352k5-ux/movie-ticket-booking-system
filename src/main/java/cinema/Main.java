package cinema;

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
        showtime.tryLockSeats(firstSeats, firstBookingId);
        Booking booking = new Booking(
                firstBookingId,
                showtime,
                firstSeats,
                new StudentDiscountPolicy());
        showtime.confirmBooking(firstBookingId);
        System.out.println(booking.generateConfirmation());

        System.out.println();
        System.out.println("=== VALIDATION: DOUBLE BOOKING ===");
        try {
            showtime.tryLockSeats(Set.of("A1"), "B002");
            System.out.println("ERROR: invalid booking was accepted.");
        } catch (IllegalStateException exception) {
            System.out.println("Rejected correctly: " + exception.getMessage());
        }
    }
}
