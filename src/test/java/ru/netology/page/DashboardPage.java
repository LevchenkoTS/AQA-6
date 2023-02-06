package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private SelenideElement heading = $(withText("Ваши карты"));
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id) {
        val cardBalance = cards.findBy(Condition.text(id)).text();
        return extractBalance(cardBalance);
    }


    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage chooseCard(String id) {
        val button = cards.findBy(Condition.text(id));
        button.$("button[data-test-id='action-deposit']").click();
        return page(TransferPage.class);
    }

}