package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> meals = getTestData();

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    private static List<Meal> getTestData() {
        return Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 26, 1, 21), "Ночной дожор", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 26, 10, 1), "Утренний обжор", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 26, 13, 1), "Дневной дожор", 800),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 26, 20, 1), "Вечерний жвон", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 27, 10, 1), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 27, 10, 31), "Кофе с собой", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 27, 13, 1), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 27, 20, 1), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 28, 10, 20), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 28, 10, 50), "Кофе с собой", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 28, 13, 0), "Обед", 800),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 28, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 29, 10, 30), "Кофе с собой", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
    }
}