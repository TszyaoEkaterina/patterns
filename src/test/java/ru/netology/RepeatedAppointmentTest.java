package ru.netology;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.ownText;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class RepeatedAppointmentTest {
    
    @BeforeEach
    void openWebsite() {
        open("http://localhost:9999");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);//clear field
    }
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @Test
    void shouldReplanAppointmentAfterRepeatedAppointmentAndConfirmation() {
        DataGenerator.ApplicantData applicant = DataGenerator.generateApplicantData("ru");
        String appointmentDate1 = DataGenerator.generateAppointmentDate(3);
        String appointmentDate2 = DataGenerator.generateAppointmentDate(5);
        //first submission
        $("[placeholder=Город]").setValue(applicant.getCity());
        $("[placeholder=\"Дата встречи\"]").setValue(appointmentDate1);
        $(byName("name")).setValue(applicant.getFullName());
        $(byName("phone")).setValue(applicant.getPhone());
        $(".checkbox").click();
        $(".grid-row button").click();
        $("[data-test-id=success-notification] .notification__title").should(ownText("Успешно!"));
        $("[data-test-id=success-notification] .notification__content").should(ownText("Встреча успешно " +
                "запланирована на \r\n" + appointmentDate1));
        //second submission
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);//clear field
        $("[placeholder=\"Дата встречи\"]").setValue(appointmentDate2);
        $(".grid-row button").click();
        $("[data-test-id=replan-notification] .notification__title").should(ownText("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] .notification__content").should(ownText("У вас уже запланирована" +
                " встреча на другую дату. Перепланировать?"));
        //confirmation
        $("[data-test-id=replan-notification] .button").click();
        $("[data-test-id=success-notification] .notification__title").should(ownText("Успешно!"));
        $("[data-test-id=success-notification] .notification__content").should(ownText("Встреча успешно " +
                "запланирована на \r\n" + appointmentDate2));
    }
    @Test
    void shouldNotAcceptInvalidCity(){
        DataGenerator.ApplicantData applicant = DataGenerator.generateApplicantData("ru");
        String appointmentDate = DataGenerator.generateAppointmentDate(3);
        $("[placeholder=Город]").setValue(DataGenerator.generateWrongCity("en"));
        $("[placeholder=\"Дата встречи\"]").setValue(appointmentDate);
        $(byName("name")).setValue(applicant.getFullName());
        $(byName("phone")).setValue(applicant.getPhone());
        $(".checkbox").click();
        $(".grid-row button").click();
        $("[data-test-id='city'] .input__sub").should(ownText("Доставка в выбранный город недоступна"));
    }
    @Test
    void shouldNotAcceptInvalidName(){
        DataGenerator.ApplicantData applicant = DataGenerator.generateApplicantData("ru");
        String appointmentDate = DataGenerator.generateAppointmentDate(3);
        $("[placeholder=Город]").setValue(applicant.getCity());
        $("[placeholder=\"Дата встречи\"]").setValue(appointmentDate);
        $(byName("name")).setValue(DataGenerator.generateWrongName("en"));
        $(byName("phone")).setValue(applicant.getPhone());
        $(".checkbox").click();
        $(".grid-row button").click();
        $("[data-test-id='name'] .input__sub").should(ownText("Имя и Фамилия указаные неверно." +
                " Допустимы только русские буквы, пробелы и дефисы."));
    }
    @Test
    void shouldNotAcceptInvalidDate(){
        DataGenerator.ApplicantData applicant = DataGenerator.generateApplicantData("ru");
        String invalidAppointmentDate = DataGenerator.generateAppointmentDate(0);
        $("[placeholder=Город]").setValue(applicant.getCity());
        $("[placeholder=\"Дата встречи\"]").setValue(invalidAppointmentDate);
        $(byName("name")).setValue(applicant.getFullName());
        $(byName("phone")).setValue(applicant.getPhone());
        $(".checkbox").click();
        $(".grid-row button").click();
        $("[data-test-id='date'] .input__sub").should(ownText("Заказ на выбранную дату невозможен"));
    }
}
