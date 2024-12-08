import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;

public class IMDbBirthdays {

    @Test
    public void verifyCelebritiesWithTodayBirthday() {
        open("https://www.imdb.com/feature/bornondate/?ref_=nv_cel_brn");

        SelenideElement bornTodayElement = $x("//span[text()='Born Today']");

        ElementsCollection celebrities = $$("div.sc-7c95f518-3.krmPLY");

        int count = 0;
        for (SelenideElement celebrity : celebrities) {
            if (count >= 3) break;

            String name = celebrity.$("h3.ipc-title__text").getText();
            String professions = celebrity.$$(".ipc-inline-list__item").stream()
                    .map(element -> element.getText())
                    .reduce((first, second) -> first + ", " + second)
                    .orElse("No professions listed");
            String knownFor = celebrity.$("a.ipc-link").getText();

            System.out.println("Celebrity: " + name);
            System.out.println("Professions: " + professions);
            System.out.println("Known for: " + knownFor);

            Assert.assertFalse(name.isEmpty(), "Celebrity name is empty for celebrity " + (count + 1));
            Assert.assertFalse(professions.isEmpty(), "Professions are empty for celebrity " + (count + 1));
            Assert.assertFalse(knownFor.isEmpty(), "Known for field is empty for celebrity " + (count + 1));

            count++;
        }
    }
}
