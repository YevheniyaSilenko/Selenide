import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;

public class FirstTest {

    @BeforeMethod
    public void setUp() {
        Configuration.browserSize = "1920x1080"; // Встановлює розмір вікна браузера
        open("https://www.google.com"); // Відкриває Google
    }

    @Test
    public void userCanSearchForText() {
        // Натискає кнопку прийняття cookie, якщо вона існує
        if ($("#L2AGLb").exists()) {
            $("#L2AGLb").click();
        }

        // Вводимо запит в поле пошуку (правильне ім'я "q") та натискаємо Enter
        $(byName("q")).setValue("IMDB").pressEnter();

        // Перевіряємо, що результати пошуку видимі
        $(".g").shouldBe(visible);
    }
}
