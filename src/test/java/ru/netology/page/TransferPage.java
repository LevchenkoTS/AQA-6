package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.page;

public class TransferPage {
    @FindBy(css = "input[type='text']")
    private SelenideElement amountField;
    @FindBy(css = "input[type='tel']")
    private SelenideElement fromField;
    @FindBy(css = "button[data-test-id=action-transfer]")
    private SelenideElement submitButton;

    public DashboardPage moneyTransfer(String amount, String id) {
        amountField.setValue(amount);
        fromField.setValue(id);
        submitButton.click();
        return page(DashboardPage.class);


    }
}
