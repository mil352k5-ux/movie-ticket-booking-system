package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;

class BookingConcurrencyTest {

    @Test
    void onlyOneUserCanBookTheLastSeat() throws Exception {
        Movie movie = new Movie("M-CONCURRENCY", "Concurrency Test", 120);
        Screen screen = new Screen("S-CONCURRENCY", "Concurrency Screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        Showtime showtime = new Showtime(
                movie,
                screen,
                LocalDateTime.of(2026, 10, 14, 21, 0));
        BookingService service = new BookingService();
        CountDownLatch readyGate = new CountDownLatch(2);
        CountDownLatch startGate = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            List<Future<Attempt>> attempts = List.of(
                    submitAttempt(executor, service, showtime, "B-CONCURRENT-1", readyGate, startGate),
                    submitAttempt(executor, service, showtime, "B-CONCURRENT-2", readyGate, startGate));

            assertTrue(readyGate.await(5, TimeUnit.SECONDS), "Both users must reach the start gate.");
            startGate.countDown();

            Attempt first = getAttempt(attempts.get(0));
            Attempt second = getAttempt(attempts.get(1));
            List<Attempt> results = List.of(first, second);
            List<Attempt> successes = results.stream().filter(Attempt::succeeded).toList();
            List<Attempt> failures = results.stream().filter(attempt -> !attempt.succeeded()).toList();

            assertEquals(1, successes.size());
            assertEquals(1, failures.size());
            assertTrue(failures.get(0).failure() instanceof IllegalStateException);

            Booking winner = successes.get(0).booking();
            assertNotNull(winner);
            assertEquals(BookingStatus.PENDING, winner.getStatus());
            assertEquals(SeatState.LOCKED, showtime.getSeatStates().get("A1"));

            service.confirmBooking(winner);

            assertEquals(BookingStatus.CONFIRMED, winner.getStatus());
            assertEquals(SeatState.BOOKED, showtime.getSeatStates().get("A1"));
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    private Future<Attempt> submitAttempt(
            ExecutorService executor,
            BookingService service,
            Showtime showtime,
            String bookingId,
            CountDownLatch readyGate,
            CountDownLatch startGate) {
        return executor.submit(() -> {
            readyGate.countDown();
            try {
                startGate.await();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return Attempt.failure(exception);
            }

            try {
                return Attempt.success(service.createBooking(
                        bookingId, showtime, Set.of("A1"), new NoDiscountPolicy()));
            } catch (IllegalStateException exception) {
                return Attempt.failure(exception);
            }
        });
    }

    private Attempt getAttempt(Future<Attempt> future)
            throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(5, TimeUnit.SECONDS);
    }

    private record Attempt(Booking booking, Throwable failure) {
        private static Attempt success(Booking booking) {
            return new Attempt(booking, null);
        }

        private static Attempt failure(Throwable failure) {
            return new Attempt(null, failure);
        }

        private boolean succeeded() {
            return booking != null;
        }
    }
}
