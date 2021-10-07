package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.CardInfo;
import ru.netology.web.data.Sql;
import ru.netology.web.page.DashboardPage;


import java.sql.SQLDataException;
import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.Sql.clearDBTables;

public class TestBuyTour {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void openPage() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanTables() throws SQLException {
        clearDBTables();
    }

    @Test
    @DisplayName("1. Покупка тура с валидными данными")
    void tourPurchaseShouldCorrect() throws SQLException {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        dashboardPage.validBuy (approvedCard);
        dashboardPage.assertSuccessfulPurchaseMessage();
        assertEquals(Sql.getStatusPurchase(), "APPROVED");
        assertEquals(Sql.getDataFromOrderEntity().getPayment_id(), Sql.getTransactionIdFromPaymentEntity());
    }
    @Test
    @DisplayName("2. Покупка тура с невалидными данными")
    void tourPurchaseShouldUncorrected() throws SQLException {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val declinedCards = CardInfo.getDeclinedCard();
        dashboardPage.validBuy(declinedCards);
        dashboardPage.assertNotSuccessfulPurchaseMessage();
        dashboardPage.assertMessageOfIncorrectInputMonth("Неверный формат");
        assertEquals(Sql.getStatusPurchase(), "DECLINED");
        assertEquals(Sql.getDataFromOrderEntity().getPayment_id(), Sql.getTransactionIdFromPaymentEntity());
    }

    @Test
    @DisplayName("3. Отправка формы с незаполненным полем Номер карты")
    void tourShouldNotPurchaseWithoutNumber () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setNumber("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCard("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("4. Отправка формы с незаполненным полем Месяц")
    void tourShouldNotPurchaseWithoutMonth () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setMonth("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputMonth("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("5. Отправка формы с незаполненным полем Год")
    void tourShouldNotPurchaseWithoutYear () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setYear("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputYear("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("6. Отправка формы с незаполненным полем Владелец")
    void tourShouldNotPurchaseWithoutHolder () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setHolder("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputHolder("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("7. Отправка формы с незаполненным полем CVC/CVV")
    void tourShouldNotPurchaseWithoutCVC () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setCvc("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCVC("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("8. Отправка пустой формы")
    void tourShouldNotPurchaseWithoutAll ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setNumber("");
        approvedCard.setMonth("");
        approvedCard.setYear("");
        approvedCard.setHolder("");
        approvedCard.setCvc("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCard("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputMonth("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputYear("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputHolder("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputCVC("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("9. Отправка формы с с невалидным значением \"Номера карты\" (14 знаков)")
    void tourShouldNotPurchaseWithIncorrectNumber ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setNumber("4444 4444 4444 41");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCard("Неверный формат");
    }

    @Test
    @DisplayName("10. Отправка формы с с невалидным значением \"Месяца\" (число больше 12)")
    void tourShouldNotPurchaseWithIncorrectMonth ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setMonth("23");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCVC("Неверный формат");
    }

    @Test
    @DisplayName("11. Отправка формы с  невалидным значением \"Год\" (число меньше 20)")
    void tourShouldNotPurchaseWithIncorrectYear () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setYear("18");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCVC("Истёк срок действия карты");
    }

    @Test
    @DisplayName("12. Отправка формы с невалидным значением в поле CVC/CVV (2 знака)")
    void tourShouldNotPurchaseWithIncorrectCVC () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setCvc("12");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCVC("Неверный формат");
    }

    @Test
    @DisplayName("13. Отправка формы с полем Владелец, заполненным кириллицей")
    void tourShouldNotPurchaseWithIncorrectHolder ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToPaymentPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setHolder("Петров Иван");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCVC("Неверный формат");
    }

    @Test
    @DisplayName("14. Покупка тура в кредит с валидными данными")
    void tourPurchaseByCreditShouldCorrect() throws SQLException {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertSuccessfulPurchaseMessage();
        assertEquals(Sql.getStatusPurchaseByCredit(), "APPROVED");
        assertEquals(Sql.getBankIdFromPaymentEntity(), Sql.getDataFromOrderEntity().getCredit_id());
    }

    @Test
    @DisplayName("15. Покупка тура в кредит с невалидными данными")
    void tourPurchaseByCreditShouldUncorrected() throws SQLException {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getDeclinedCard();
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertNotSuccessfulPurchaseMessage();
        assertEquals(Sql.getStatusPurchaseByCredit(), "DECLINED");
        assertEquals(Sql.getDataFromOrderEntity().getCredit_id(), Sql.getBankIdFromPaymentEntity());
    }

    @Test
    @DisplayName("16. Покупка тура в кредит. Отправка формы с незаполненным полем Номер карты")
    void tourShouldNotPurchaseByCreditWithoutNumber () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setNumber("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCard("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("17. Покупка тура в кредит. Отправка формы с незаполненным полем Месяц")
    void tourShouldNotPurchaseByCreditWithoutMonth ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setMonth("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputMonth("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("18. Покупка тура в кредит. Отправка формы с незаполненным полем Год")
    void tourShouldNotPurchaseByCreditWithoutYear ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setYear("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputYear("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("19. Покупка тура в кредит. Отправка формы с незаполненным полем Владелец")
    void tourShouldNotPurchaseByCreditWithoutHolder ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setHolder("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputHolder("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("20. Покупка тура в кредит. Отправка формы с незаполненным полем CVC/CVV")
    void tourShouldNotPurchaseByCreditWithoutCVC ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setCvc("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCVC("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("21. Покупка тура в кредит. Отправка пустой формы")
    void tourShouldNotPurchaseByCreditWithoutAll () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setNumber("");
        approvedCard.setMonth("");
        approvedCard.setYear("");
        approvedCard.setHolder("");
        approvedCard.setCvc("");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCard("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputMonth("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputYear("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputHolder("Поле обязательно для заполнения");
        dashboardPage.assertMessageOfIncorrectInputCVC("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("22. Покупка тура в кредит. Отправка формы с с невалидным значением \"Номера карты\" (14 знаков)")
    void tourShouldNotPurchaseByCreditWithIncorrectNumber ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setNumber("4444 4444 4444 41");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCard("Неверный формат");
    }

    @Test
    @DisplayName("23. Покупка тура в кредит. Отправка формы с с невалидным значением \"Месяца\" (число больше 12)")
    void tourShouldNotPurchaseByCreditWithIncorrectMonth ()  {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setMonth("56");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputMonth("Неверный формат");
    }

    @Test
    @DisplayName("24. Покупка тура в кредит. Отправка формы с  невалидным значением \"Год\" (число меньше 20)")
    void tourShouldNotPurchaseByCreditWithIncorrectYear () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setYear("19");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputYear("Истёк срок действия карты");
    }

    @Test
    @DisplayName("25. Покупка тура в кредит. Отправка формы с невалидным значением в поле CVC/CVV (2 знака)")
    void tourShouldNotPurchaseByCreditWithIncorrectCVC () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setCvc("99");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputCVC("Неверный формат");
    }

    @Test
    @DisplayName("26. Покупка тура в кредит. Отправка формы с полем Владелец, заполненным кириллицей")
    void tourShouldNotPurchaseByCreditWithIncorrectHolder () {
        val dashboardPage = new DashboardPage();
        dashboardPage.goToCreditPage();
        val approvedCard = CardInfo.getApprovedCard();
        approvedCard.setHolder("Ио Ким");
        dashboardPage.validBuy(approvedCard);
        dashboardPage.assertMessageOfIncorrectInputHolder("Неверный формат");
    }
}

