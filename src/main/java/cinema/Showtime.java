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
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        for (Seat seat : screen.getSeats()) {
            seatStates.put(seat.getSeatId(), SeatState.AVAILABLE);
        }
    }

    public synchronized void tryLockSeats(Set<String> seatIds, String bookingId) {
        for (String seatId : seatIds) {
            if (!isSeatAvailable(seatId)) {
                throw new IllegalStateException("Seat " + seatId + " is not available.");
            }
        }
        for (String seatId : seatIds) {
            seatStates.put(seatId, SeatState.LOCKED);
            seatLockOwners.put(seatId, bookingId);
        }
    }

    public synchronized void confirmBooking(String bookingId) {
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
