package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<MealTo> mealsTo = filteredByStreams(getMealTestData(), LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> getAllWithExcess(List<Meal> meals, int caloriesPerDay) {
        return filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime,
                                                 LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static List<Meal> getMealTestData() {
        return Arrays.asList(
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
