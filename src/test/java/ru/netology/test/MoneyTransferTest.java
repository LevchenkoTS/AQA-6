package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoneyTransferTest {

    @BeforeEach
    public void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);

    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        val amountToTransfer = 5000;
        var dashboardPage = new DashboardPage();

        var cardsPage = dashboardPage.chooseCard(DataHelper.getSecondCard().getSecretCardId());
        var dashboard = cardsPage.moneyTransfer(String.valueOf(amountToTransfer), DataHelper.getFirstCard().getCardId());

        val expectedBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCard().getSecretCardId());
        val expectedBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCard().getSecretCardId());

        assertEquals(expectedBalanceFirstCard, new DashboardPage().getCardBalance(DataHelper.getFirstCard().getSecretCardId()));
        assertEquals(expectedBalanceSecondCard, new DashboardPage().getCardBalance(DataHelper.getSecondCard().getSecretCardId()));
    }


    @Test
    void shouldTransferMoneyBetweenOwnCards2() {
        val amountToTransfer = 1_000;
        var dashboardPage = new DashboardPage();

        var cardsPage = dashboardPage.chooseCard(DataHelper.getFirstCard().getSecretCardId());
        var dashboard = cardsPage.moneyTransfer(String.valueOf(amountToTransfer), DataHelper.getSecondCard().getCardId());

        val expectedBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCard().getSecretCardId());
        val expectedBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCard().getSecretCardId());

        assertEquals(expectedBalanceFirstCard, new DashboardPage().getCardBalance(DataHelper.getFirstCard().getSecretCardId()));
        assertEquals(expectedBalanceSecondCard, new DashboardPage().getCardBalance(DataHelper.getSecondCard().getSecretCardId()));
    }

    @Test
    void shouldNotTransferMoneyOverLimitFromFirstCard() {
        val amountToTransfer = 15_000;
        var dashboardPage = new DashboardPage();

        var cardsPage = dashboardPage.chooseCard(DataHelper.getSecondCard().getSecretCardId());
        var dashboard = cardsPage.moneyTransfer(String.valueOf(amountToTransfer), DataHelper.getFirstCard().getCardId());
        $(withText("Ваши карты")).shouldBe(visible);

        val balanceFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCard().getSecretCardId());
        val balanceSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCard().getSecretCardId());

        assertTrue(balanceFirstCard > balanceSecondCard);
        assertTrue(balanceFirstCard >= 0);
    }

    @Test
    void shouldNotTransferMoneyOverLimitFromSecondCard() {
        val amountToTransfer = 30_000;
        var dashboardPage = new DashboardPage();

        var cardsPage = dashboardPage.chooseCard(DataHelper.getFirstCard().getSecretCardId());
        var dashboard = cardsPage.moneyTransfer(String.valueOf(amountToTransfer), DataHelper.getSecondCard().getCardId());
        $(withText("Ваши карты")).shouldBe(visible);

        val balanceFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCard().getSecretCardId());
        val balanceSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCard().getSecretCardId());

        assertTrue(balanceFirstCard > balanceSecondCard);
        assertTrue(balanceSecondCard >= 0);
    }
}
