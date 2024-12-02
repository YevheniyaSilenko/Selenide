import com.codeborne.selenide.*;
import java.util.List;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class IMDbTop250Scraper {
    public static void main(String[] args) {
        openIMDbHomePage();
        navigateToTop250();

        List<SelenideElement> movieTitles = $$("ul > li h3").shouldBe(CollectionCondition.sizeGreaterThan(0));
        double totalRating = 0;

        for (int i = 0; i < 5; i++) {
            String title = movieTitles.get(i).getText();
            String releaseYear = getReleaseYear(i);
            double rating = getMovieRating(i);

            if (rating != -1) {
                totalRating += rating;
                System.out.println("Movie " + (i + 1) + ": " + title + " (Release Year: " + releaseYear + ", Rating: " + rating + ")");
            }
        }

        System.out.println(totalRating > 0 ? "Average Rating: " + totalRating / 5 : "No ratings found for top 5 movies.");
    }

    private static void openIMDbHomePage() {
        open("https://www.imdb.com");
    }

    private static void navigateToTop250() {
        $("#imdbHeader-navDrawerOpen").shouldBe(visible).click();
        $("aside.drawer a:nth-child(2)").shouldBe(visible).click();
        $("h1.ipc-title__text").shouldBe(visible); // Wait for page to load
    }

    private static String getReleaseYear(int index) {
        return $("ul > li:nth-child(" + (index + 1) + ") div.ipc-metadata-list-summary-item__c div div div.sc-300a8231-6.dBUjvq.cli-title-metadata > span:nth-child(1)").getText();
    }

    private static double getMovieRating(int index) {
        String ratingText = $("ul > li:nth-child(" + (index + 1) + ") div.ipc-metadata-list-summary-item__c > div > div > span > div").getText().replaceAll("[^0-9.]", "");
        try {
            return ratingText.isEmpty() ? -1 : Double.parseDouble(ratingText.substring(0, Math.min(ratingText.indexOf('.') + 3, ratingText.length())));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}