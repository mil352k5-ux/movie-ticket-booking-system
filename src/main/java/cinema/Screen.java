package cinema;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Screen {
    private final String screenId;
    private final String name;
    private final Map<String, Seat> seats = new LinkedHashMap<>();

    public Screen(String screenId, String name) {
        this.screenId = requireText(screenId, "Screen ID");
        this.name = requireText(name, "Screen name");
    }

    public void addSeat(Seat seat) {
        Objects.requireNonNull(seat, "Seat must not be null.");
        if (seats.putIfAbsent(seat.getSeatId(), seat) != null) {
            throw new IllegalArgumentException("Duplicate seat: " + seat.getSeatId());
        }
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value;
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
