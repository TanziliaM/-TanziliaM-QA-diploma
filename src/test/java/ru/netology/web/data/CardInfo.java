package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
public class CardInfo {
    public String number;
    public String month;
    public String year;
    public String holder;
    public String cvc;

    public static String approvedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String declinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static Faker faker = new Faker();
    public static Random random = new Random();


    private static String holder() {
        return faker.name().fullName();
    }

    public static String getMonth() {
        List<String> months = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        int index = random.nextInt(months.size());
        return months.get(index);
    }

    public static String getYear(){
        int year = LocalDate.now().plusYears(random.nextInt(5) + 1).getYear();
        return String.valueOf(year).substring(2);
    }

    public static String getCvc(){
        return String.valueOf(faker.number().numberBetween(100,999));
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo(approvedCardNumber(),
                getMonth(),
                getYear(),
                holder(),
                getCvc());
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo(declinedCardNumber(),
                getMonth(),
                getYear(),
                holder(),
                getCvc());
    }
}
