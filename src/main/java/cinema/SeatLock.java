package cinema;

import java.time.Instant;

public record SeatLock(String bookingId, Instant expiresAt) {
    public SeatLock {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("Booking ID must not be blank.");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("Lock expiration must not be null.");
        }
    }

    public boolean isOwnedBy(String requestedBookingId) {
        return bookingId.equals(requestedBookingId);
    }

    public boolean isExpiredAt(Instant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("Current instant must not be null.");
        }
        return !instant.isBefore(expiresAt);
    }
}
