package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 2;
    private static final LocalDateTime testDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    public static final List<Meal> USER_MEALS = getMealsForTest();

    public static List<Meal> getMealsForTest() {
        return Arrays.asList(
                new Meal(USER_MEAL_ID, LocalDateTime.of(2021, Month.OCTOBER, 16, 1, 21), "Ночной дожор", 500),
                new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2021, Month.OCTOBER, 16, 10, 1), "Утренний обжор", 100),
                new Meal(USER_MEAL_ID + 2, LocalDateTime.of(2021, Month.OCTOBER, 16, 13, 1), "Дневной дожор", 800),
                new Meal(USER_MEAL_ID + 3, LocalDateTime.of(2021, Month.OCTOBER, 16, 20, 1), "Вечерний жвон", 500),
                new Meal(USER_MEAL_ID + 4, LocalDateTime.of(2021, Month.OCTOBER, 17, 10, 1), "Завтрак", 500),
                new Meal(USER_MEAL_ID + 5, LocalDateTime.of(2021, Month.OCTOBER, 17, 10, 31), "Кофе с собой", 100),
                new Meal(USER_MEAL_ID + 6, LocalDateTime.of(2021, Month.OCTOBER, 17, 13, 1), "Обед", 1000),
                new Meal(USER_MEAL_ID + 7, LocalDateTime.of(2021, Month.OCTOBER, 17, 20, 1), "Ужин", 500));
    }

    public static Meal getNew() {
        return new Meal(null, testDateTime, "new meal", 1555);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEALS.get(0));
        updated.setCalories(100500);
        updated.setDateTime(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES));
        updated.setDescription("updated meal");
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}