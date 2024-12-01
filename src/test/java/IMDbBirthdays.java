import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class IMDbBirthdays {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver"); // Замість цього вкажіть свій шлях
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void fetchCelebritiesWithTodayBirthday() {
        driver.get("https://www.imdb.com");
        System.out.println("IMDb homepage opened.");

        openBornTodayMenu();

        List<WebElement> celebrities = fetchCelebrityElements();
        for (int i = 0; i < Math.min(3, celebrities.size()); i++) {
            printCelebrityInfo(celebrities.get(i), i + 1);
        }
    }

    private void openBornTodayMenu() {
        try {
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("imdbHeader-navDrawerOpen")));
            menuButton.click();
            System.out.println("Menu opened.");

            WebElement bornTodayLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Born Today']")));
            bornTodayLink.click();
            System.out.println("'Born Today' page opened.");
        } catch (Exception e) {
            System.err.println("Error opening 'Born Today' menu: " + e.getMessage());
        }
    }

    private List<WebElement> fetchCelebrityElements() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.sc-77f37b3d-1.gJmTWZ")));
            List<WebElement> celebrities = driver.findElements(By.cssSelector("div.sc-77f37b3d-1.gJmTWZ"));

            if (celebrities.isEmpty()) {
                System.out.println("No celebrities found. Check your selectors.");
            } else {
                System.out.println("Found " + celebrities.size() + " celebrities.");
            }

            return celebrities;
        } catch (Exception e) {
            System.err.println("Error fetching celebrity elements: " + e.getMessage());
            return List.of();
        }
    }

    private void printCelebrityInfo(WebElement celebrity, int rank) {
        try {
            String name = celebrity.findElement(By.cssSelector("div.sc-9b5cbdfc-2.jHuaUr")).getText();
            String famousMovie = celebrity.findElements(By.cssSelector("a[href*='/title/']")).stream()
                    .findFirst()
                    .map(WebElement::getText)
                    .orElse("No famous movie listed");

            System.out.printf("%d. %s - Famous Movie: %s%n", rank, name, famousMovie);
        } catch (Exception e) {
            System.err.println("Error fetching celebrity info: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
