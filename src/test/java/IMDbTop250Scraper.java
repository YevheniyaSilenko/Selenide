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

        System.out.println("Founded movies: " + movieElements.size());
        movieElements.shouldHave(CollectionCondition.sizeGreaterThan(0));

        List<Movie> movies = new ArrayList<>();

        for (SelenideElement movieElement : movieElements.first(20)) {
            try {
                String title = movieElement.$("h3.ipc-title__text").getText();
                String yearText = movieElement.$("span.sc-5bc66c50-6.OOdsw.cli-title-metadata-item").getText();
                int year = Integer.parseInt(yearText);

                String ratingText = movieElement.$("span.ipc-rating-star--rating").getText();
                double rating = Double.parseDouble(ratingText);

                movies.add(new Movie(title, year, rating));
            } catch (Exception e) {
                System.out.println("Error between data of movies: " + e.getMessage());
            }
        }

        for (int i = 0; i < 5 && i < movies.size(); i++) {
            System.out.println(movies.get(i));
        }

        double averageRating = movies.stream().mapToDouble(Movie::getRating).average().orElse(0.0);
        System.out.println("Mid rate of movies: " + averageRating);
    }
}
class Movie {
    private final String title;//Only Ukranian language
    private final int year;
    private final double rating;


    public Movie(String title, int year, double rating) {
        this.title = title;
        this.year = year;
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return title + " (" + year + ") - " + rating;
    }
}

