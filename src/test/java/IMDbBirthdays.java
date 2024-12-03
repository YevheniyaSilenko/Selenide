import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

public class IMDbBirthdays {

    @BeforeClass
    public void setUp() {

        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    @Test
    public void fetchCelebritiesWithTodayBirthday() {
        openIMDbHomepage();
        openBornTodayMenu();
        ElementsCollection celebrities = fetchCelebrityElements();
        printCelebrityInfo(celebrities);
    }

    private void openIMDbHomepage() {
        Selenide.open("https://www.imdb.com");
        System.out.println("IMDb homepage opened.");
    }


    private void openBornTodayMenu() {
        clickMenuButton();
        clickBornTodayLink();
    }


    private void clickMenuButton() {
        try {
            WebElement menuButton = Selenide.$(By.id("imdbHeader-navDrawerOpen"));
            menuButton.click();
            System.out.println("Menu opened.");
        } catch (Exception e) {
            System.err.println("Error opening menu: " + e.getMessage());
        }
    }

    private void clickBornTodayLink() {
        try {
            WebElement bornTodayLink = Selenide.$(By.xpath("//span[text()='Born Today']"));
            bornTodayLink.click();
            System.out.println("'Born Today' page opened.");
        } catch (Exception e) {
            System.err.println("Error opening 'Born Today' link: " + e.getMessage());
        }
    }

    private ElementsCollection fetchCelebrityElements() {

        try {
            ElementsCollection celebrities = Selenide.$$(
                    By.cssSelector("div.sc-8a39b693-3.ekUbVT")
            );
            if (celebrities.isEmpty()) {
                System.out.println("No celebrities found.");
            } else {
                System.out.println("Found " + celebrities.size() + " celebrities.");
            }
            return celebrities;
        } catch (Exception e) {
            System.err.println("Error fetching celebrity elements: " + e.getMessage());
            return Selenide.$$(By.cssSelector("div.sc-8a39b693-3.ekUbVT"));
        }
    }

    private void printCelebrityInfo(ElementsCollection celebrities) {
        for (int i = 0; i < Math.min(3, celebrities.size()); i++) {
            WebElement celebrity = celebrities.get(i);
            String name = getCelebrityName(celebrity);
            String professions = getCelebrityProfessions(celebrity);
            String famousMovie = getCelebrityFamousMovie(celebrity);

            System.out.printf("%d. %s - Professions: %s - Famous Movie: %s%n", i + 1, name, professions, famousMovie);
        }
    }

    private String getCelebrityName(WebElement celebrity) {
        try {
            return celebrity.findElement(By.cssSelector("h3.ipc-title__text")).getText();
        } catch (Exception e) {
            System.err.println("Error fetching celebrity name: " + e.getMessage());
            return "Unknown";
        }
    }

    private String getCelebrityProfessions(WebElement celebrity) {
        try {
            List<WebElement> professions = celebrity.findElements(By.cssSelector("ul.ipc-inline-list"));
            return professions.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            System.err.println("Error fetching celebrity professions: " + e.getMessage());
            return "Unknown";
        }
    }

    private String getCelebrityFamousMovie(WebElement celebrity) {
        try {
            return celebrity.findElements(By.cssSelector("a[data-testid='nlib-known-for-title']"))
                    .stream()
                    .findFirst()
                    .map(WebElement::getText)
                    .orElse("No famous movie listed");
        } catch (Exception e) {
            System.err.println("Error fetching famous movie: " + e.getMessage());
            return "Unknown";
        }
    }

    @AfterClass
    public void tearDown() {

        if (WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.getWebDriver().quit();
        }
    }
}
