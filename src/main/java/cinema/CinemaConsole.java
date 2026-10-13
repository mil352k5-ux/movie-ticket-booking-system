package cinema;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public final class CinemaConsole {
    private final List<Showtime> showtimes;
    private final BookingService service;
    private final BookingHistory history;
    private final InputStream input;
    private final PrintStream output;

    public CinemaConsole(List<Showtime> showtimes, BookingService service,
            BookingHistory history, InputStream input, PrintStream output) {
        this.showtimes = List.copyOf(showtimes);
        this.service = service;
        this.history = history;
        this.input = input;
        this.output = output;
    }

    public void run() {
        try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8)) {
            while (true) {
                output.println("=== SHOWTIMES ===");
                for (int index = 0; index < showtimes.size(); index++) {
                    Showtime showtime = showtimes.get(index);
                    output.println((index + 1) + ". " + showtime.getMovie().getTitle() + " | "
                            + showtime.getStartTime() + " | " + showtime.getScreen().getName());
                }
                output.println("Choose a showtime number (0 to exit):");
                String selection = nextLine(scanner);
                if (selection == null || selection.equals("0")) {
                    return;
                }
                try {
                    int index = Integer.parseInt(selection) - 1;
                    if (index < 0 || index >= showtimes.size()) {
                        throw new IllegalArgumentException("Unknown showtime number.");
                    }
                    Showtime showtime = showtimes.get(index);
                    Map<String, SeatState> states = showtime.getSeatStates();
                    for (Seat seat : showtime.getScreen().getSeats()) {
                        output.println(seat.getSeatId() + " | " + seat.getPricingTier().name()
                                + " | " + states.get(seat.getSeatId()));
                    }
                    output.println("Seat IDs, separated by commas:");
                    String seatsInput = nextLine(scanner);
                    if (seatsInput == null) {
                        return;
                    }
                    Set<String> seatIds = parseSeatIds(seatsInput);
                    output.println("Discount: 0 = none, 1 = student, 2 = group:");
                    String discountInput = nextLine(scanner);
                    if (discountInput == null) {
                        return;
                    }
                    DiscountPolicy policy = switch (discountInput) {
                        case "0" -> new NoDiscountPolicy();
                        case "1" -> new StudentDiscountPolicy();
                        case "2" -> new GroupDiscountPolicy();
                        default -> throw new IllegalArgumentException("Unknown discount option.");
                    };
                    Booking booking = service.createBooking(
                            "B-" + UUID.randomUUID(), showtime, seatIds, policy);
                    output.println("Total: " + String.format("%,d", booking.calculateTotal()) + " VND");
                    output.println("Enter y to confirm; any other input cancels:");
                    String confirmation = nextLine(scanner);
                    if (confirmation == null || !confirmation.equalsIgnoreCase("y")) {
                        service.cancelBooking(booking);
                        output.println("Booking cancelled.");
                        continue;
                    }
                    service.confirmBooking(booking);
                    output.println(booking.generateConfirmation());
                    try {
                        history.save(booking);
                    } catch (IOException exception) {
                        output.println("Booking confirmed, but history could not be saved: "
                                + exception.getMessage());
                    }
                } catch (IllegalArgumentException | IllegalStateException exception) {
                    output.println("Rejected: " + exception.getMessage());
                }
            }
        }
    }

    private String nextLine(Scanner scanner) {
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }

    private Set<String> parseSeatIds(String value) {
        Set<String> seatIds = new LinkedHashSet<>();
        for (String token : value.split(",", -1)) {
            String seatId = token.trim();
            if (seatId.isBlank() || !seatIds.add(seatId)) {
                throw new IllegalArgumentException("Seat IDs must be nonblank and unique.");
            }
        }
        return seatIds;
    }
}
