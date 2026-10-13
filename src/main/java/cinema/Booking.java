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
    private BookingStatus status = BookingStatus.PENDING;

    public Booking(String bookingId, Showtime showtime, Set<String> seatIds, DiscountPolicy discountPolicy) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("Booking ID must not be blank.");
        }
        if (showtime == null) {
            throw new IllegalArgumentException("Showtime must not be null.");
        }
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs must not be empty.");
        }
        if (discountPolicy == null) {
            throw new IllegalArgumentException("Discount policy must not be null.");
        }
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

    public String getBookingId() {
        return bookingId;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public synchronized BookingStatus getStatus() {
        return status;
    }

    synchronized void markConfirmed() {
        transitionFromPendingTo(BookingStatus.CONFIRMED);
    }

    synchronized void markCancelled() {
        transitionFromPendingTo(BookingStatus.CANCELLED);
    }

    synchronized void markExpired() {
        transitionFromPendingTo(BookingStatus.EXPIRED);
    }

    private void transitionFromPendingTo(BookingStatus nextStatus) {
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking must be PENDING.");
        }
        status = nextStatus;
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
