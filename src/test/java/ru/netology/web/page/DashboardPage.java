package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.CardInfo;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $$("h2").find(text("Путешествие дня"));
    private SelenideElement pushButtonBuy = $$("button").find(exactText("Купить"));
    private SelenideElement pushButtonCredit = $$("button").find(exactText("Купить в кредит"));
    ElementsCollection formInputControl = $$(".input");
    SelenideElement numberCard = formInputControl.find(exactText("Номер карты")).$(".input__control");
    SelenideElement month = formInputControl.find(exactText("Месяц")).$(".input__control");
    SelenideElement year = formInputControl.find(exactText("Год")).$(".input__control");
    SelenideElement holder = formInputControl.find(exactText("Владелец")).$(".input__control");
    SelenideElement cvc = formInputControl.find(exactText("CVC/CVV")).$(".input__control");
    ElementsCollection formInputTops = $$(".input__top");
    SelenideElement cardNumberInputSub = formInputTops.find(exactText("Номер карты")).parent().$(".input__sub");
    SelenideElement yearInputSub = formInputTops.find(exactText("Год")).parent().$(".input__sub");
    SelenideElement cvcInputSub = formInputTops.find(exactText("CVC/CVV")).parent().$(".input__sub");
    SelenideElement holderInputSub = formInputTops.find(exactText("Владелец")).parent().$(".input__sub");
    SelenideElement monthInputSub = formInputTops.find(exactText("Месяц")).parent().$(".input__sub");

    private final SelenideElement continueButton = $$("button").find(exactText("Продолжить"));

    SelenideElement paymentOkWindow = $(".notification_status_ok");
    SelenideElement paymentErrorWindow = $(".notification_status_error");

    public DashboardPage validBuy (CardInfo card) {
        numberCard.setValue(card.number);
        month.setValue(card.month);
        year.setValue(card.year);
        holder.setValue(card.holder);
        cvc.setValue(card.cvc);
        continueButton.click();
        return new DashboardPage();
    }

    public PaymentPage goToPaymentPage() {
        pushButtonBuy.click();
        return new PaymentPage();
    }

    public CreditPage goToCreditPage() {
        pushButtonCredit.click();
        return new CreditPage();
    }


    public DashboardPage() {
        heading.shouldBe(visible);
    }
    public void assertSuccessfulPurchaseMessage() {
        paymentOkWindow.waitUntil(Condition.visible, 15000);
    }

    public void assertNotSuccessfulPurchaseMessage() {
        paymentErrorWindow.waitUntil(Condition.visible, 15000);
    }

    public void assertMessageOfIncorrectInputCard(String message) {
        cardNumberInputSub.shouldHave(exactText(message));
    }

    public void assertMessageOfIncorrectInputMonth(String message) {
        monthInputSub.shouldHave(exactText(message));
    }

    public void assertMessageOfIncorrectInputYear(String message) {
        yearInputSub.shouldHave(exactText(message));
    }

    public void assertMessageOfIncorrectInputCVC(String message) {
        cvcInputSub.shouldHave(exactText(message));
    }

    public void assertMessageOfIncorrectInputHolder(String message) {
        holderInputSub.shouldHave(exactText(message));
    }

}
