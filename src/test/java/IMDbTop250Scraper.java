import com.codeborne.selenide.*;
import java.util.ArrayList;
import java.util.List;
import static com.codeborne.selenide.Selenide.*;

public class IMDbTop250Scraper {

    public static void main(String[] args) {
        configureSelenide();
        openImdbPage();
        openNavigationMenu();
        navigateToTop250();
        List<String> movieData = scrapeMovies(20);
        printTopMovies(movieData);
        double averageRating = calculateAverageRating(movieData);
        printAverageRating(averageRating);
    }
    private static void configureSelenide() {
        Configuration.timeout = 10000;
    }
    private static void openImdbPage() {
        open("https://www.imdb.com");
    }
    private static void openNavigationMenu() {
        $("#imdbHeader-navDrawerOpen").should(Condition.exist).click();
    }
    private static void navigateToTop250() {
        SelenideElement top250Link = $("aside.drawer a:nth-child(2)");
        top250Link.should(Condition.exist).click();
    }
    private static List<String> scrapeMovies(int limit) {
        ElementsCollection movieElements = $$("li.ipc-metadata-list-summary-item");
        List<String> movieData = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();

        System.out.println("Found movies: " + movieElements.size());
        movieElements.shouldHave(CollectionCondition.sizeGreaterThan(0));

        for (int i = 0; i < Math.min(limit, movieElements.size()); i++) {
            try {
                String movieInfo = extractMovieInfo(movieElements.get(i));
                movieData.add(movieInfo);
                ratings.add(extractMovieRating(movieElements.get(i)));
            } catch (Exception e) {
                System.out.println("Error processing movie data: " + e.getMessage());
            }
        }

        return movieData;
    }
    private static String extractMovieInfo(SelenideElement movieElement) {
        String title = movieElement.$("h3.ipc-title__text").getText();
        String yearText = movieElement.$("span:contains('(')").getText();
        int year = Integer.parseInt(yearText.replaceAll("[^\\d]", ""));
        double rating = extractMovieRating(movieElement);
        return String.format("%s (%d) - %.1f", title, year, rating);
    }
    private static double extractMovieRating(SelenideElement movieElement) {
        String ratingText = movieElement.$("span.ipc-rating-star--rating").getText();
        return Double.parseDouble(ratingText);
    }
    private static void printTopMovies(List<String> movieData) {
        for (int i = 0; i < Math.min(5, movieData.size()); i++) {
            System.out.println(movieData.get(i));
        }
    }
    private static double calculateAverageRating(List<String> movieData) {
        List<Double> ratings = new ArrayList<>();
        for (String movie : movieData) {
            try {
                ratings.add(Double.parseDouble(movie.split(" - ")[1]));
            } catch (Exception e) {
                System.out.println("Error parsing rating: " + e.getMessage());
            }
        }
        return ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
    private static void printAverageRating(double averageRating) {
        System.out.println("Average movie rating: " + averageRating);
    }
}

