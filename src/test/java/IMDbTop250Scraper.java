import java.util.HashSet;
import java.util.Set;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class IMDbTop250Scraper {

    public static void main(String[] args) {
        try {
            openIMDbHomePage();
            navigateToTop250();

            scrapeTopMovies();
        } catch (Exception e) {
            logError("Error occurred during the scraping process: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void scrapeTopMovies() {
        double totalRating = 0;
        int validRatingsCount = 0;
        Set<String> seenTitles = new HashSet<>();

        System.out.println("Top 5 Movies:");
        for (int i = 0; i < 5; i++) {

            MovieDetails movie = getMovieDetails(i);

            if (seenTitles.contains(movie.title)) {
                System.err.println("Duplicate found: " + movie.title);
                continue;
            } else {
                seenTitles.add(movie.title);
            }

            if (movie.rating != -1) {
                totalRating += movie.rating;
                validRatingsCount++;
            }

            printMovieDetails(i, movie);
        }

        if (validRatingsCount > 0) {
            System.out.printf("Average Rating: %.2f%n", totalRating / validRatingsCount);
        } else {
            System.out.println("No valid ratings found.");
        }
    }

    private static MovieDetails getMovieDetails(int index) {
        String title = getMovieTitle(index);
        String releaseYear = getReleaseYear(index);
        double rating = getMovieRating(index);

        return new MovieDetails(title, releaseYear, rating);
    }

    private static void printMovieDetails(int index, MovieDetails movie) {
        System.out.printf("%d. %s (%s) - Rating: %.1f%n", index + 1, movie.title, movie.releaseYear, movie.rating);
    }

    private static String getMovieTitle(int index) {
        return $("ul > li:nth-child(" + (index + 1) + ") h3").getText();
    }

    private static String getReleaseYear(int index) {
        return $("ul > li:nth-child(" + (index + 1) + ") div.ipc-metadata-list-summary-item__c div div div.sc-300a8231-6.dBUjvq.cli-title-metadata > span:nth-child(1)").getText();
    }

    private static double getMovieRating(int index) {
        String ratingText = $("ul > li:nth-child(" + (index + 1) + ") div.ipc-metadata-list-summary-item__c > div > div > span > div").getText().replaceAll("[^0-9.]", "");

        if (ratingText.isEmpty()) {
            logError("Rating not found for movie at index " + index);
            return -1; // Return -1 if rating is missing
        }

        try {
            return Double.parseDouble(ratingText.substring(0, Math.min(ratingText.indexOf('.') + 3, ratingText.length())));
        } catch (NumberFormatException e) {
            logError("Error parsing rating for movie at index " + index);
            return -1; // Return -1 if parsing fails
        }
    }

    private static void openIMDbHomePage() {
        open("https://www.imdb.com");
    }

    private static void navigateToTop250() {
        $("#imdbHeader-navDrawerOpen").shouldBe(visible).click();
        $("a[href='/chart/top/?ref_=nv_mv_250']").shouldBe(visible).click();
        $("h1.ipc-title__text").shouldBe(visible);
    }


    private static void logError(String message) {
        System.err.println(message);
    }

    static class MovieDetails {
        String title;
        String releaseYear;
        double rating;

        MovieDetails(String title, String releaseYear, double rating) {
            this.title = title;
            this.releaseYear = releaseYear;
            this.rating = rating;
        }
    }
}
