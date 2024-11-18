import com.codeborne.selenide.*;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class IMDbTop250Scraper {

    public static void main(String[] args) {
        Configuration.timeout = 10000;

        open("https://www.imdb.com");

        $("#imdbHeader-navDrawerOpen").should(Condition.exist).click();

        SelenideElement top250Link = $("aside.drawer a:nth-child(2)");
        top250Link.should(Condition.exist).click();

        ElementsCollection movieElements = $$("li.ipc-metadata-list-summary-item");

        System.out.println("Found movies: " + movieElements.size());
        movieElements.shouldHave(CollectionCondition.sizeGreaterThan(0));

        List<String> movieData = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();

        for (SelenideElement movieElement : movieElements.first(20)) {
            try {
                String title = movieElement.$("h3.ipc-title__text").getText();
                String yearText = movieElement.$("span.sc-5bc66c50-6.OOdsw.cli-title-metadata-item").getText();
                int year = Integer.parseInt(yearText);

                String ratingText = movieElement.$("span.ipc-rating-star--rating").getText();
                double rating = Double.parseDouble(ratingText);

                movieData.add(String.format("%s (%d) - %.1f", title, year, rating));
                ratings.add(rating);
            } catch (Exception e) {
                System.out.println("Error processing movie data: " + e.getMessage());
            }
        }

        // Print the first 5 movies
        for (int i = 0; i < 5 && i < movieData.size(); i++) {
            System.out.println(movieData.get(i));
        }

        // Calculate average rating using the separate ratings list
        double averageRating = ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        System.out.println("Average movie rating: " + averageRating);
    }
}
