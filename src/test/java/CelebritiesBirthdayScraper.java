import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CelebritiesBirthdayScraper {
    public static void main(String[] args) {
        // Вкажіть шлях до вашого ChromeDriver
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.imdb.com");

            WebElement menuButton = driver.findElement(By.id("imdbHeader-navDrawerOpen"));
            menuButton.click();

            WebElement celebsLink = driver.findElement(By.xpath("//span[text()='Celebs']"));
            celebsLink.click();

            WebElement bornTodayLink = driver.findElement(By.xpath("//a[text()='Born Today']"));
            bornTodayLink.click();

            List<WebElement> celebrities = driver.findElements(By.cssSelector(".list_item"));

            for (int i = 0; i < 3 && i < celebrities.size(); i++) {
                WebElement celebrity = celebrities.get(i);

                String name = celebrity.findElement(By.cssSelector(".name a")).getText();
                String details = celebrity.findElement(By.cssSelector(".text-small")).getText();

                System.out.println("Name: " + name);
                System.out.println("Details: " + details);
                System.out.println("------------------------");
            }

            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d"));
            WebElement dateElement = driver.findElement(By.cssSelector(".header"));
            String pageDate = dateElement.getText();

            if (!pageDate.contains(today)) {
                throw new AssertionError("Date mismatch! Expected: " + today + ", Found: " + pageDate);
            } else {
                System.out.println("Date assertion passed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

