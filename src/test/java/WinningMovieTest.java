import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class WinningMovieTest {

    private static final String MOVIE_TITLE = "Titanic";
    private static final String MOVIE_YEAR = "1997";
    private static final String MOVIE_URL = "/title/tt0120338/";
    private static final String AWARDS_TEXT = "Won 11 Oscars";

    @BeforeClass
    public void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.browser = "chrome";
    }

    @Test
    public void testSearchForTitanic() {

        open("https://www.imdb.com");

        $("input[name='q']").setValue(MOVIE_TITLE).pressEnter();

        $$(".ipc-metadata-list-summary-item .ipc-metadata-list-summary-item__c")
                .find(text(MOVIE_TITLE)).shouldHave(text(MOVIE_YEAR)).click();

        SelenideElement pageHeader = $("h1");
        pageHeader.shouldHave(text(MOVIE_TITLE));

        SelenideElement awardsLink = $("[data-testid='awards']");
        awardsLink.shouldBe(visible, enabled)
                .shouldHave(text(AWARDS_TEXT)).scrollIntoView(true).click();

        $("h1").shouldHave(text("Awards"));
    }
}
