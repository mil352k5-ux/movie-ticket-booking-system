package cinema;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

final class MutableClock extends Clock {
    private Instant currentInstant;
    private final ZoneId zone;

    MutableClock(Instant currentInstant, ZoneId zone) {
        if (currentInstant == null) {
            throw new IllegalArgumentException("Current instant must not be null.");
        }
        if (zone == null) {
            throw new IllegalArgumentException("Zone must not be null.");
        }
        this.currentInstant = currentInstant;
        this.zone = zone;
    }

    synchronized void advance(Duration duration) {
        if (duration == null || duration.isNegative()) {
            throw new IllegalArgumentException("Advance duration must not be negative.");
        }
        currentInstant = currentInstant.plus(duration);
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public synchronized Clock withZone(ZoneId requestedZone) {
        return new MutableClock(currentInstant, requestedZone);
    }

    @Override
    public synchronized Instant instant() {
        return currentInstant;
    }
}
