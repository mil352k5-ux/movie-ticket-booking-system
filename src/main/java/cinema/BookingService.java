package cinema;

import java.util.LinkedHashSet;
import java.util.Set;

public final class BookingService {

    public Booking createBooking(
            String bookingId,
            Showtime showtime,
            Set<String> seatIds,
            DiscountPolicy discountPolicy) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs must not be empty.");
        }

        Set<String> requestedSeatIds = new LinkedHashSet<>(seatIds);
        Booking booking = new Booking(bookingId, showtime, requestedSeatIds, discountPolicy);
        showtime.tryLockSeats(requestedSeatIds, booking.getBookingId());
        return booking;
    }

    public void confirmBooking(Booking booking) {
        requireBooking(booking);
        synchronized (booking) {
            requirePending(booking);
            try {
                booking.getShowtime().confirmBooking(booking.getBookingId());
            } catch (IllegalStateException exception) {
                booking.markExpired();
                throw new IllegalStateException(
                        "Booking lock expired for " + booking.getBookingId() + ".", exception);
            }
            booking.markConfirmed();
        }
    }

    public void cancelBooking(Booking booking) {
        requireBooking(booking);
        synchronized (booking) {
            requirePending(booking);
            booking.getShowtime().releaseSeats(booking.getBookingId());
            booking.markCancelled();
        }
    }

    private static void requireBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking must not be null.");
        }
    }

    private static void requirePending(Booking booking) {
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking must be PENDING.");
        }
    }
}
