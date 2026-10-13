package cinema;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

public final class BookingHistory {
    private final Path directory;

    public BookingHistory(Path directory) {
        if (directory == null) {
            throw new IllegalArgumentException("History directory must not be null.");
        }
        this.directory = directory;
    }

    public synchronized void save(Booking booking) throws IOException {
        if (booking == null) {
            throw new IllegalArgumentException("Booking must not be null.");
        }
        String confirmation = booking.generateConfirmation();
        Files.createDirectories(directory);
        Path destination = pathFor(booking.getBookingId());
        Path temporary = Files.createTempFile(directory, "booking-", ".tmp");
        try {
            Files.writeString(temporary, confirmation, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, destination);
            } catch (FileAlreadyExistsException exception) {
                if (!Files.readString(destination, StandardCharsets.UTF_8).equals(confirmation)) {
                    throw new IOException("A different confirmation already exists for booking "
                            + booking.getBookingId() + ".", exception);
                }
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    public synchronized Optional<String> find(String bookingId) throws IOException {
        Path file = pathFor(bookingId);
        try {
            return Optional.of(Files.readString(file, StandardCharsets.UTF_8));
        } catch (NoSuchFileException exception) {
            return Optional.empty();
        }
    }

    private Path pathFor(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("Booking ID must not be blank.");
        }
        String encodedId = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(bookingId.getBytes(StandardCharsets.UTF_8));
        return directory.resolve("booking-" + encodedId + ".txt");
    }
}
