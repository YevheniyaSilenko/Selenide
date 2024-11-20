import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class WinningMovieTest {

    private static final String MOVIE_TITLE = "Titanic";
    private static final String MOVIE_LINK = "a[href*='/title/tt0120338/']";
    private static final String AWARDS_LINK = "a[href='/title/tt0120338/awards/?ref_=tt_awd']";

    @BeforeClass
    public void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.browser = "chrome";
    }

    @Test
    public void testSearchForTitanic() {
        openIMDb();
        searchForMovie(MOVIE_TITLE);
        clickOnMovieLink(MOVIE_LINK);
        verifyMoviePageLoaded("Титанік");
        scrollToAndClickAwardsLink(AWARDS_LINK);
        verifyAwardsPageLoaded("Awards");
        waitForObservation(5000);
    }

    private void openIMDb() {
        open("https://www.imdb.com?language=en");
    }

    private void searchForMovie(String movieTitle) {
        $("input[name='q']").setValue(movieTitle).pressEnter();
    }

    private void clickOnMovieLink(String movieLink) {
        $(movieLink).click();
    }

    private void verifyMoviePageLoaded(String expectedTitle) {
        $("h1").shouldBe(visible).shouldHave(text(expectedTitle));
    }

    private void scrollToAndClickAwardsLink(String awardsLink) {
        SelenideElement awardsElement = $(awardsLink);
        for (int i = 0; i < 5; i++) {
            awardsElement.scrollIntoView(true);
            sleep(1000);
            executeJavaScript("window.scrollBy(0, 200);");
        }
        awardsElement.click();
    }

    private void verifyAwardsPageLoaded(String expectedTitle) {
        $("h1").shouldHave(text(expectedTitle));
    }

    private void waitForObservation(int milliseconds) {
        sleep(milliseconds);
    }
}

