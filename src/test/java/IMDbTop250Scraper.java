import com.codeborne.selenide.*;
import java.util.List;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class IMDbTop250Scraper {
    public static void main(String[] args) {
        open("https://www.imdb.com");

        SelenideElement burgerMenu = $("#imdbHeader-navDrawerOpen");
        burgerMenu.shouldBe(visible).click();

        SelenideElement top250Link = $("aside.drawer a:nth-child(2)");
        top250Link.shouldBe(visible).click();

        $$("ul > li h3").shouldBe(CollectionCondition.sizeGreaterThan(0));

        List<SelenideElement> movieTitles = $$("ul > li h3");
        System.out.println("Found " + movieTitles.size() + " movie titles.");

        if (movieTitles.size() < 5) {
            System.out.println("There are not enough movies available.");
            return;
        }

        double totalRating = 0;
        int count = 0;

        for (int i = 0; i < 5; i++) {
            String title = movieTitles.get(i).getText();

            SelenideElement releaseYearElement = $("ul > li:nth-child(" + (i + 1) + ") div.ipc-metadata-list-summary-item__c div div div.sc-300a8231-6.dBUjvq.cli-title-metadata > span:nth-child(1)");
            releaseYearElement.shouldBe(visible);
            String releaseYear = releaseYearElement.getText();

            SelenideElement ratingElement = $("ul > li:nth-child(" + (i + 1) + ") div.ipc-metadata-list-summary-item__c > div > div > span > div");
            ratingElement.shouldBe(visible);

            String ratingText = ratingElement.getText();
            if (ratingText.isEmpty()) {
                System.out.println("Rating not found for movie " + (i + 1));
                continue;
            }

            try {
                ratingText = ratingText.replaceAll("[^0-9.]", "");
                if (ratingText.indexOf('.') != ratingText.lastIndexOf('.')) {
                    ratingText = ratingText.substring(0, ratingText.indexOf('.') + 3);
                }

                double rating = Double.parseDouble(ratingText);

                totalRating += rating;
                count++;

                System.out.println("Movie " + (i + 1) + ": " + title + " (Release Year: " + releaseYear + ", Rating: " + rating + ")");
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating format for movie " + (i + 1) + ": " + ratingText);
            }
        }

        if (count > 0) {
            double averageRating = totalRating / count;
            System.out.println("Average Rating of the Top 5 Movies: " + averageRating);
        } else {
            System.out.println("No ratings found for the top 5 movies.");
        }
    }
}
