import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TitanicWinningTest {

    private static final String TITANIC = "Titanic";
    private static final String MOVIE_YEAR = "1997";
    private static final String AWARDS_SELECTOR = "[data-testid='awards']";

    private static final Map<String, String> MOVIE_TITLES = Map.of(
            "en", "Titanic",
            "uk", "Титанік"
    );

    @BeforeClass
    public void setUp() {
        configureBrowser();
    }

    private void configureBrowser() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.browser = "chrome";
    }

    @Test
    public void testSearchForTitanic() {
        openIMDb();
        searchForMovie(TITANIC);
        clickOnMovieLink(MOVIE_YEAR);
        verifyPageHeader(getLocalizedTitle());
        navigateToAwardsPage();
        verifyPageHeader("Awards");
    }

    private void openIMDb() {
        String language = getCurrentLanguage();
        open("https://www.imdb.com?language=" + language);
    }

    private String getCurrentLanguage() {
        return System.getProperty("user.language", "en");
    }

    private void searchForMovie(String movieTitle) {
        $("input[name='q']").setValue(movieTitle).pressEnter();
    }

    private void clickOnMovieLink(String movieYear) {
        $$(".ipc-metadata-list-summary-item .ipc-metadata-list-summary-item__c")
                .find(text(movieYear))
                .shouldHave(text(getLocalizedTitle()))
                .scrollIntoView(true) // Прокрутка до елементу
                .click();
    }

    private void verifyPageHeader(String expectedTitle) {
        $("h1").shouldBe(visible).shouldHave(text(expectedTitle));
    }

    private void navigateToAwardsPage() {
        SelenideElement awardsElement = $(AWARDS_SELECTOR);
        awardsElement.scrollIntoView(true);
        awardsElement.shouldBe(visible, enabled).click();
    }

    private String getLocalizedTitle() {
        String language = getCurrentLanguage();
        return MOVIE_TITLES.getOrDefault(language, "Titanic");
    }
}
