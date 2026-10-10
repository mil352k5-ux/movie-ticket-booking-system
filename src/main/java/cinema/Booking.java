package cinema;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Booking {
    private static final long BASE_PRICE = 100_000L;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String bookingId;
    private final Showtime showtime;
    private final List<Seat> selectedSeats;
    private final DiscountPolicy discountPolicy;

    public Booking(String bookingId, Showtime showtime, Set<String> seatIds, DiscountPolicy discountPolicy) {
        this.bookingId = bookingId;
        this.showtime = showtime;
        this.discountPolicy = discountPolicy;
        this.selectedSeats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = showtime.getScreen().getSeat(seatId);
            if (seat == null) {
                throw new IllegalArgumentException("Unknown seat: " + seatId);
            }
            selectedSeats.add(seat);
        }
    }

    public long calculateSubtotal() {
        return selectedSeats.stream().mapToLong(seat -> seat.price(BASE_PRICE)).sum();
    }

    public long calculateDiscount() {
        return discountPolicy.calculateDiscount(calculateSubtotal(), selectedSeats.size());
    }

    public long calculateTotal() {
        return calculateSubtotal() - calculateDiscount();
    }

    public String generateConfirmation() {
        String seats = selectedSeats.stream()
                .sorted(Comparator.comparing(Seat::getSeatId))
                .map(Seat::getSeatId)
                .collect(Collectors.joining(", "));
        return "Booking confirmed successfully.\n"
                + "Booking ID: " + bookingId + "\n"
                + "Movie: " + showtime.getMovie().getTitle() + "\n"
                + "Time: " + showtime.getStartTime().format(TIME_FORMAT) + "\n"
                + "Seats: " + seats + "\n"
                + "Subtotal: " + format(calculateSubtotal()) + " VND\n"
                + "Discount (" + discountPolicy.name() + "): " + format(calculateDiscount()) + " VND\n"
                + "Total: " + format(calculateTotal()) + " VND";
    }

    private String format(long amount) {
        return String.format("%,d", amount);
    }
}
