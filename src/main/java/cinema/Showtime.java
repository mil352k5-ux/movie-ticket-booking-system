package cinema;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Showtime {
    private final Movie movie;
    private final Screen screen;
    private final LocalDateTime startTime;
    private final Map<String, SeatState> seatStates = new HashMap<>();
    private final Map<String, String> seatLockOwners = new HashMap<>();

    public Showtime(Movie movie, Screen screen, LocalDateTime startTime) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie must not be null.");
        }
        if (screen == null) {
            throw new IllegalArgumentException("Screen must not be null.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time must not be null.");
        }
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        for (Seat seat : screen.getSeats()) {
            seatStates.put(seat.getSeatId(), SeatState.AVAILABLE);
        }
    }

    public synchronized void tryLockSeats(Set<String> seatIds, String bookingId) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs must not be empty.");
        }
        requireBookingId(bookingId);
        for (String seatId : seatIds) {
            if (!seatStates.containsKey(seatId)) {
                throw new IllegalArgumentException("Unknown seat: " + seatId);
            }
            if (seatStates.get(seatId) != SeatState.AVAILABLE) {
                throw new IllegalStateException("Seat " + seatId + " is not available.");
            }
        }
        for (String seatId : seatIds) {
            seatStates.put(seatId, SeatState.LOCKED);
            seatLockOwners.put(seatId, bookingId);
        }
    }

    public synchronized void confirmBooking(String bookingId) {
        requireBookingId(bookingId);
        boolean found = false;
        for (Map.Entry<String, String> entry : seatLockOwners.entrySet()) {
            if (bookingId.equals(entry.getValue())) {
                seatStates.put(entry.getKey(), SeatState.BOOKED);
                found = true;
            }
        }
        if (!found) {
            throw new IllegalStateException("No locked seats found for booking " + bookingId);
        }
    }

    public synchronized void releaseSeats(String bookingId) {
        requireBookingId(bookingId);
        for (Map.Entry<String, String> entry : seatLockOwners.entrySet()) {
            if (bookingId.equals(entry.getValue()) && seatStates.get(entry.getKey()) == SeatState.LOCKED) {
                seatStates.put(entry.getKey(), SeatState.AVAILABLE);
            }
        }
        seatLockOwners.entrySet().removeIf(entry -> bookingId.equals(entry.getValue()));
    }

    public synchronized boolean isSeatAvailable(String seatId) {
        if (!seatStates.containsKey(seatId)) {
            throw new IllegalArgumentException("Unknown seat: " + seatId);
        }
        return seatStates.get(seatId) == SeatState.AVAILABLE;
    }

    private static void requireBookingId(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("Booking ID must not be blank.");
        }
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Map<String, SeatState> getSeatStates() {
        return Collections.unmodifiableMap(seatStates);
    }
}
