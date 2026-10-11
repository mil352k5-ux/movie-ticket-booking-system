package cinema;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Screen {
    private final String screenId;
    private final String name;
    private final Map<String, Seat> seats = new LinkedHashMap<>();

    public Screen(String screenId, String name) {
        if (screenId == null || screenId.isBlank()) {
            throw new IllegalArgumentException("Screen ID must not be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Screen name must not be blank.");
        }
        this.screenId = screenId;
        this.name = name;
    }

    public void addSeat(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat must not be null.");
        }
        if (seats.putIfAbsent(seat.getSeatId(), seat) != null) {
            throw new IllegalArgumentException("Duplicate seat: " + seat.getSeatId());
        }
    }

    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }

    public Collection<Seat> getSeats() {
        return Collections.unmodifiableCollection(seats.values());
    }

    public String getScreenId() {
        return screenId;
    }

    public String getName() {
        return name;
    }
}
