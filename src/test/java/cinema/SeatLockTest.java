package cinema;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class SeatLockTest {

    @Test
    void reportsOwnerAndExpiration() {
        Instant expiresAt = Instant.parse("2026-10-12T12:05:00Z");
        SeatLock lock = new SeatLock("B1", expiresAt);

        assertTrue(lock.isOwnedBy("B1"));
        assertFalse(lock.isOwnedBy("B2"));
        assertFalse(lock.isExpiredAt(expiresAt.minusMillis(1)));
        assertTrue(lock.isExpiredAt(expiresAt));
    }

    @Test
    void rejectsInvalidValues() {
        Instant expiresAt = Instant.parse("2026-10-12T12:05:00Z");

        assertThrows(IllegalArgumentException.class, () -> new SeatLock(" ", expiresAt));
        assertThrows(IllegalArgumentException.class, () -> new SeatLock("B1", null));
        assertThrows(
                IllegalArgumentException.class,
                () -> new SeatLock("B1", expiresAt).isExpiredAt(null));
    }
}
