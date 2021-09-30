package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private SelenideElement heading = $$("h3").find(text("Кредит по данным карты"));


    public CreditPage() {
        heading.shouldBe(visible);
        }

}
