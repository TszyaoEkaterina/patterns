package ru.netology;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class DataGenerator {
    LocalDate currentDate = LocalDate.now();
    
    public static ApplicantData generateApplicantData(String locale) {
        Faker faker = new Faker(new Locale(locale));
        ApplicantData applicant = new ApplicantData(faker.address().city(),
                faker.name().fullName(), faker.phoneNumber().phoneNumber());
        return applicant;
    }
    
    public static String generateAppointmentDate(int daysFromToday) {
        String appointmentDate = currentDate.plusDays(daysFromToday).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return appointmentDate;
    }
    public static String generateWrongCity(String locale){
        Faker faker = new Faker(new Locale(locale));
        String wrongCity = faker.address().city();
        return wrongCity;
    }
    public static String generateWrongName(String locale){
        Faker faker = new Faker(new Locale(locale));
        String wrongName = faker.name().name();
        return wrongName;
    }
    
    @Value
    public static class ApplicantData {
        private String city;
        private String fullName;
        private String phone;
    }
}
