package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class BookingServiceConcurrencyTest {

    @Test
    @Timeout(15)
    void confirmAndCancelCannotLeaveStatusAndSeatStateInconsistent() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            for (int iteration = 0; iteration < 30; iteration++) {
                BookingService service = new BookingService();
                Showtime showtime = createShowtime();
                Booking booking = service.createBooking(
                        "B-RACE-" + iteration, showtime, Set.of("A1"), new NoDiscountPolicy());

                List<Boolean> results = race(executor,
                        () -> service.confirmBooking(booking),
                        () -> service.cancelBooking(booking));

                assertEquals(1, results.stream().filter(Boolean::booleanValue).count());
                if (booking.getStatus() == BookingStatus.CONFIRMED) {
                    assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"));
                } else {
                    assertEquals(BookingStatus.CANCELLED, booking.getStatus());
                    assertEquals(SeatState.AVAILABLE, showtime.getSeatStates().get("A1"));
                }
            }
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test
    @Timeout(15)
    void simultaneousConfirmationsHaveOneWinnerAndKeepConfirmedStatus() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            for (int iteration = 0; iteration < 30; iteration++) {
                BookingService service = new BookingService();
                Showtime showtime = createShowtime();
                Booking booking = service.createBooking(
                        "B-CONFIRM-" + iteration, showtime, Set.of("A1"), new NoDiscountPolicy());

                List<Boolean> results = race(executor,
                        () -> service.confirmBooking(booking),
                        () -> service.confirmBooking(booking));

                assertEquals(1, results.stream().filter(Boolean::booleanValue).count());
                assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
                assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"));
            }
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    private List<Boolean> race(ExecutorService executor, Runnable first, Runnable second) throws Exception {
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        Future<Boolean> firstResult = submit(executor, first, ready, start);
        Future<Boolean> secondResult = submit(executor, second, ready, start);
        try {
            assertTrue(ready.await(5, TimeUnit.SECONDS));
            start.countDown();
            return List.of(firstResult.get(5, TimeUnit.SECONDS), secondResult.get(5, TimeUnit.SECONDS));
        } finally {
            start.countDown();
        }
    }

    private Future<Boolean> submit(
            ExecutorService executor, Runnable operation, CountDownLatch ready, CountDownLatch start) {
        return executor.submit(() -> {
            ready.countDown();
            start.await();
            try {
                operation.run();
                return true;
            } catch (IllegalStateException exception) {
                return false;
            }
        });
    }

    private Showtime createShowtime() {
        Screen screen = new Screen("S-RACE", "Lifecycle race screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        return new Showtime(new Movie("M-RACE", "Lifecycle race", 120), screen,
                LocalDateTime.of(2026, 10, 14, 20, 0),
                Clock.fixed(Instant.parse("2026-10-14T12:00:00Z"), ZoneOffset.UTC),
                Duration.ofMinutes(5));
    }
}
