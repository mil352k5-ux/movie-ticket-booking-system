package cinema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LuxuryMemberIntegrationTest {
    private static final Set<String> SEATS = Set.of("A1", "A6");
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

    @TempDir
    Path directory;

    @Test
    void swapsDiscountPoliciesForTheSameStandardAndLuxurySeatPrices() {
        DiscountPolicy[] policies = {
                new NoDiscountPolicy(), new StudentDiscountPolicy(), new MemberDiscountPolicy()
        };
        long[] discounts = {0L, 27_500L, 41_250L};
        long[] totals = {275_000L, 247_500L, 233_750L};

        for (int index = 0; index < policies.length; index++) {
            TestContext context = createContext();
            Booking booking = context.service().createBooking(
                    "B-W4-POLICY-" + index, context.showtime(), SEATS, policies[index]);

            assertEquals(275_000L, booking.calculateSubtotal());
            assertEquals(discounts[index], booking.calculateDiscount());
            assertEquals(totals[index], booking.calculateTotal());
            context.service().confirmBooking(booking);
            assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
            assertSeatState(context.showtime(), SeatState.BOOKED);
        }
    }

    @Test
    void confirmsMemberLuxuryBookingPersistsItAndRejectsSeatReuse() throws IOException {
        TestContext context = createContext();
        Booking booking = createMemberBooking(context, "B-W4-COMBINED");

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertSeatState(context.showtime(), SeatState.LOCKED);
        assertThrows(IllegalStateException.class, booking::generateConfirmation);
        context.service().confirmBooking(booking);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertSeatState(context.showtime(), SeatState.BOOKED);
        String confirmation = booking.generateConfirmation();
        assertTrue(confirmation.contains("Movie: Interstellar"));
        assertTrue(confirmation.contains("Time: 2026-10-20 19:30"));
        assertTrue(confirmation.contains("Seats: A1, A6"));
        assertMemberPricing(confirmation);

        new BookingHistory(directory).save(booking);
        assertEquals(confirmation,
                new BookingHistory(directory).find(booking.getBookingId()).orElseThrow());
        assertThrows(IllegalStateException.class,
                () -> context.service().createBooking("B-W4-REUSE", context.showtime(),
                        Set.of("A6"), new NoDiscountPolicy()));
        assertSeatState(context.showtime(), SeatState.BOOKED);
    }

    @Test
    void cancellingMemberLuxuryBookingReleasesSeatsAndPreventsConfirmation() throws IOException {
        TestContext context = createContext();
        Booking booking = createMemberBooking(context, "B-W4-CANCEL");

        context.service().cancelBooking(booking);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertSeatState(context.showtime(), SeatState.AVAILABLE);
        assertThrows(IllegalStateException.class, () -> context.service().confirmBooking(booking));
        assertThrows(IllegalStateException.class, () -> new BookingHistory(directory).save(booking));
        assertEquals(0L, historyFileCount());
    }

    @Test
    void expiredMemberLuxuryBookingCannotBeConfirmedOrSaved() throws IOException {
        TestContext context = createContext();
        Booking booking = createMemberBooking(context, "B-W4-EXPIRED");
        context.clock().advance(LOCK_DURATION);

        assertThrows(IllegalStateException.class, () -> context.service().confirmBooking(booking));

        assertEquals(BookingStatus.EXPIRED, booking.getStatus());
        assertSeatState(context.showtime(), SeatState.AVAILABLE);
        assertThrows(IllegalStateException.class, () -> new BookingHistory(directory).save(booking));
        assertEquals(0L, historyFileCount());
    }

    @Test
    void consoleConfirmsLuxuryWithMemberDiscountAndSavesThePrintedSummary() throws IOException {
        TestContext context = createContext();
        String output = runConsole(context, "1\nA1,A6\n3\ny\n0\n");

        assertTrue(output.contains("A6 | Luxury | AVAILABLE"));
        assertTrue(output.contains("3 = member"));
        assertTrue(output.contains("Booking confirmed successfully."));
        assertMemberPricing(output);
        assertSeatState(context.showtime(), SeatState.BOOKED);
        String bookingId = output.lines().filter(line -> line.startsWith("Booking ID: "))
                .findFirst().orElseThrow().substring("Booking ID: ".length());
        String saved = new BookingHistory(directory).find(bookingId).orElseThrow();
        assertTrue(saved.contains("Seats: A1, A6"));
        assertMemberPricing(saved);
        assertTrue(output.contains(saved));
        assertEquals(1L, historyFileCount());
    }

    @Test
    void consoleRejectsSecondBookingOfLuxurySeatWithoutAnotherHistoryFile() throws IOException {
        TestContext context = createContext();
        String output = runConsole(context, "1\nA1,A6\n3\ny\n1\nA6\n3\n0\n");

        assertTrue(output.contains("Rejected: Seat A6 is not available."));
        assertEquals(1L, output.lines()
                .filter(line -> line.equals("Booking confirmed successfully.")).count());
        assertSeatState(context.showtime(), SeatState.BOOKED);
        assertEquals(1L, historyFileCount());
    }

    @Test
    void consoleCancellationOfMemberLuxuryBookingCreatesNoHistory() throws IOException {
        TestContext context = createContext();
        String output = runConsole(context, "1\nA1,A6\n3\nn\n0\n");

        assertTrue(output.contains("Total: " + money(233_750L)));
        assertTrue(output.contains("Booking cancelled."));
        assertSeatState(context.showtime(), SeatState.AVAILABLE);
        assertEquals(0L, historyFileCount());
    }

    private Booking createMemberBooking(TestContext context, String bookingId) {
        return context.service().createBooking(
                bookingId, context.showtime(), SEATS, new MemberDiscountPolicy());
    }

    private void assertMemberPricing(String summary) {
        assertTrue(summary.contains("Subtotal: " + money(275_000L)));
        assertTrue(summary.contains("Discount (Member discount 15%): " + money(41_250L)));
        assertTrue(summary.contains("Total: " + money(233_750L)));
    }

    private String money(long amount) {
        return String.format("%,d", amount) + " VND";
    }

    private void assertSeatState(Showtime showtime, SeatState expected) {
        for (String seatId : SEATS) {
            assertEquals(expected, showtime.getSeatStates().get(seatId));
        }
    }

    private long historyFileCount() throws IOException {
        try (var files = Files.list(directory)) {
            return files.count();
        }
    }

    private String runConsole(TestContext context, String commands) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new CinemaConsole(List.of(context.showtime()), context.service(), new BookingHistory(directory),
                new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(output, true, StandardCharsets.UTF_8)).run();
        return output.toString(StandardCharsets.UTF_8);
    }

    private TestContext createContext() {
        Screen screen = new Screen("S-W4-COMBINED", "Luxury and Member test screen");
        screen.addSeat(new Seat("A1", new StandardTier()));
        screen.addSeat(new Seat("A6", new LuxuryTier()));
        MutableClock clock = new MutableClock(
                Instant.parse("2026-10-18T12:00:00Z"), ZoneOffset.UTC);
        Showtime showtime = new Showtime(new Movie("M-W4-COMBINED", "Interstellar", 169), screen,
                LocalDateTime.of(2026, 10, 20, 19, 30), clock, LOCK_DURATION);
        return new TestContext(showtime, new BookingService(), clock);
    }

    private record TestContext(Showtime showtime, BookingService service, MutableClock clock) {
    }
}
