import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TitanicWinningTest {

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
        searchForMovie();
        clickOnMovieLink();
        verifyPageHeader("Титанік");
        navigateToAwardsPage();
        verifyPageHeader("Awards");
    }

    private void openIMDb() {
        open("https://www.imdb.com?language=en");
    }

    private void searchForMovie() {
        $("input[name='q']").setValue(TitanicWinningTest.MOVIE_TITLE).pressEnter();
    }

    private void clickOnMovieLink() {
        $(TitanicWinningTest.MOVIE_LINK).shouldBe(visible).click();
    }

    private void verifyPageHeader(String expectedTitle) {
        $("h1").shouldBe(visible).shouldHave(text(expectedTitle));
    }

    private void navigateToAwardsPage() {
        SelenideElement awardsElement = $(TitanicWinningTest.AWARDS_LINK);
        awardsElement.scrollIntoView(true);
        awardsElement.shouldBe(visible, enabled).click();
    }
}
