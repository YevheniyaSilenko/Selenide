import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class WinningMovieTest {

    @BeforeClass
    public void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000; // Таймаут на 10 секунд
        Configuration.browser = "chrome";
    }

    @Test
    public void testSearchForTitanic() {

        // Відкриття IMDb з примусовою зміною мови на англійську
        open("https://www.imdb.com");
        // Пошук фільму "Titanic"
        $("input[name='q']").setValue("Titanic").pressEnter();

        // Клік на фільм "Титанік" (1997)
        $("a[href*='/title/tt0120338/']").click();

        // Перевірка, що сторінка фільму завантажилася (перевірка наявності заголовка або іншого елемента сторінки)
        $("h1").shouldHave(text("Титанік"));

        // Знаходження та скрол до посилання на нагороди
        SelenideElement awardsLink = $("a[href='/title/tt0120338/awards/?ref_=tt_awd']");
        awardsLink.scrollIntoView(true).click();

        // Перевірка, що відкрилося посилання з нагородами
        $("h1").shouldHave(text("Awards"));
    }
}
