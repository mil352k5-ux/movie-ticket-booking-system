package cinema;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class Showtime {
    private static final Duration DEFAULT_LOCK_DURATION = Duration.ofMinutes(5);

    private final Movie movie;
    private final Screen screen;
    private final LocalDateTime startTime;
    private final Clock clock;
    private final Duration lockDuration;
    private final Map<String, SeatState> seatStates = new HashMap<>();
    private final Map<String, SeatLock> seatLocks = new HashMap<>();
    private final Set<String> usedBookingIds = new HashSet<>();

    public Showtime(Movie movie, Screen screen, LocalDateTime startTime) {
        this(movie, screen, startTime, Clock.systemUTC(), DEFAULT_LOCK_DURATION);
    }

    public Showtime(
            Movie movie,
            Screen screen,
            LocalDateTime startTime,
            Clock clock,
            Duration lockDuration) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie must not be null.");
        }
        if (screen == null) {
            throw new IllegalArgumentException("Screen must not be null.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time must not be null.");
        }
        if (clock == null) {
            throw new IllegalArgumentException("Clock must not be null.");
        }
        if (lockDuration == null || lockDuration.isZero() || lockDuration.isNegative()) {
            throw new IllegalArgumentException("Lock duration must be positive.");
        }
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.clock = clock;
        this.lockDuration = lockDuration;
        for (Seat seat : screen.getSeats()) {
            seatStates.put(seat.getSeatId(), SeatState.AVAILABLE);
        }
    }

    public synchronized void tryLockSeats(Set<String> seatIds, String bookingId) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs must not be empty.");
        }
        requireBookingId(bookingId);
        Instant now = clock.instant();
        releaseExpiredLocks(now);
        if (usedBookingIds.contains(bookingId)) {
            throw new IllegalStateException("Booking ID has already been used for this showtime.");
        }
        for (String seatId : seatIds) {
            if (!seatStates.containsKey(seatId)) {
                throw new IllegalArgumentException("Unknown seat: " + seatId);
            }
            if (seatStates.get(seatId) != SeatState.AVAILABLE) {
                throw new IllegalStateException("Seat " + seatId + " is not available.");
            }
        }
        Instant expiresAt = now.plus(lockDuration);
        usedBookingIds.add(bookingId);
        for (String seatId : seatIds) {
            seatStates.put(seatId, SeatState.LOCKED);
            seatLocks.put(seatId, new SeatLock(bookingId, expiresAt));
        }
    }

    public synchronized void confirmBooking(String bookingId) {
        requireBookingId(bookingId);
        releaseExpiredLocks(clock.instant());
        boolean found = false;
        Iterator<Map.Entry<String, SeatLock>> iterator = seatLocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SeatLock> entry = iterator.next();
            if (entry.getValue().isOwnedBy(bookingId)) {
                seatStates.put(entry.getKey(), SeatState.BOOKED);
                iterator.remove();
                found = true;
            }
        }
        if (!found) {
            throw new IllegalStateException("No locked seats found for booking " + bookingId);
        }
    }

    public synchronized void releaseSeats(String bookingId) {
        requireBookingId(bookingId);
        releaseExpiredLocks(clock.instant());
        Iterator<Map.Entry<String, SeatLock>> iterator = seatLocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SeatLock> entry = iterator.next();
            if (entry.getValue().isOwnedBy(bookingId)
                    && seatStates.get(entry.getKey()) == SeatState.LOCKED) {
                seatStates.put(entry.getKey(), SeatState.AVAILABLE);
                iterator.remove();
            }
        }
    }

    public synchronized boolean isSeatAvailable(String seatId) {
        if (!seatStates.containsKey(seatId)) {
            throw new IllegalArgumentException("Unknown seat: " + seatId);
        }
        releaseExpiredLocks(clock.instant());
        return seatStates.get(seatId) == SeatState.AVAILABLE;
    }

    private void releaseExpiredLocks(Instant now) {
        Iterator<Map.Entry<String, SeatLock>> iterator = seatLocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SeatLock> entry = iterator.next();
            if (entry.getValue().isExpiredAt(now)) {
                if (seatStates.get(entry.getKey()) == SeatState.LOCKED) {
                    seatStates.put(entry.getKey(), SeatState.AVAILABLE);
                }
                iterator.remove();
            }
        }
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

    public synchronized Map<String, SeatState> getSeatStates() {
        releaseExpiredLocks(clock.instant());
        return Map.copyOf(seatStates);
    }
}
