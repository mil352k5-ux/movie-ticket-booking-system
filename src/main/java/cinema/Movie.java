package cinema;

public final class Movie {
    private final String movieId;
    private final String title;
    private final int durationMinutes;

    public Movie(String movieId, String title, int durationMinutes) {
        if (movieId == null || movieId.isBlank()) {
            throw new IllegalArgumentException("Movie ID must not be blank.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Movie title must not be blank.");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.movieId = movieId;
        this.title = title;
        this.durationMinutes = durationMinutes;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
