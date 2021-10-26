package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//import static ru.javawebinar.topjava.CommonTestData.USER_MEAL_ID;

public class MealTestData extends CommonTestData {

    public static final Meal userMeal0 = new Meal(USER_MEAL_ID, LocalDateTime.of(2021, Month.OCTOBER, 16, 1, 21),
            "Случайный бутерброд", 500);
    public static final Meal userMeal1 = new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2021, Month.OCTOBER, 16, 10, 1),
            "Утренний чай", 100);
    public static final Meal userMeal2 = new Meal(USER_MEAL_ID + 2, LocalDateTime.of(2021, Month.OCTOBER, 16, 13, 1),
            "Дивный эклер", 800);
    public static final Meal userMeal3 = new Meal(USER_MEAL_ID + 3, LocalDateTime.of(2021, Month.OCTOBER, 16, 20, 1),
            "Вечерний борщ", 500);
    public static final Meal userMeal4 = new Meal(USER_MEAL_ID + 4, LocalDateTime.of(2021, Month.OCTOBER, 17, 10, 1),
            "Завтрак", 500);
    public static final Meal userMeal5 = new Meal(USER_MEAL_ID + 5, LocalDateTime.of(2021, Month.OCTOBER, 17, 10, 31),
            "Кофе с собой", 100);
    public static final Meal userMeal6 = new Meal(USER_MEAL_ID + 6, LocalDateTime.of(2021, Month.OCTOBER, 17, 13, 1),
            "Обед", 1000);
    public static final Meal userMeal7 = new Meal(USER_MEAL_ID + 7, LocalDateTime.of(2021, Month.OCTOBER, 17, 20, 1),
            "Ужин", 500);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0), "new meal", 1555);
    }

    public static List<Meal> getFiltered() {
        return Stream.of(userMeal0, userMeal1, userMeal2, userMeal3)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static List<Meal> getAll() {
        return Stream.of(userMeal0, userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6, userMeal7)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal0);
        updated.setCalories(100500);
        updated.setDateTime(LocalDateTime.of(2021, Month.DECEMBER, 31, 23, 59));
        updated.setDescription("updated meal");
        return updated;
    }
}