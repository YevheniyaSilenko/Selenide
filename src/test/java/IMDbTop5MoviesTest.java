import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.*;

public class IMDbTop5MoviesTest {

    @Test
    public void verifyTop5MoviesAndRatings() {
        open("https://www.imdb.com/chart/top/?ref_=nv_mv_250");
        ElementsCollection listOfMovies = $$(".ipc-metadata-list-summary-item");

        int count = 0;
        for (SelenideElement movie : listOfMovies) {
            if (count >= 5) break; // Stop after verifying the top 5 movies
            String movieTitle = movie.$(".ipc-title").getText();
            String movieYear = movie.$$(".cli-title-metadata-item").get(0).getText();
            String movieRating = movie.$(".cli-ratings-container .ipc-rating-star--rating").getText();
            System.out.println("Movie " + movieTitle + " (" + movieYear + ")" + " | Rating: " + movieRating);

            Assert.assertFalse(movieTitle.isEmpty(), "Movie title is empty for movie " + (count + 1));
            Assert.assertFalse(movieYear.isEmpty(), "Movie year is missing for movie " + (count + 1));
            Assert.assertFalse(movieRating.isEmpty(), "Movie rating is empty for movie " + (count + 1));

            count++;
        }
    }
}
