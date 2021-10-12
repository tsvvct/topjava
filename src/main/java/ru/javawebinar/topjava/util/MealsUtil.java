package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.dao.MealService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<MealTo> mealsTo = filteredWithExcess(getTestData(), LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> getAllWithExcess(List<Meal> meals, int caloriesPerDay) {
        return filteredWithExcess(meals, meal -> true, caloriesPerDay);
    }

    public static List<MealTo> filteredWithExcess(List<Meal> meals, LocalTime startTime,
                                                  LocalTime endTime, int caloriesPerDay) {
        Predicate<Meal> mealFilter = meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime);
        return filteredWithExcess(meals, mealFilter, caloriesPerDay);
    }

    private static List<MealTo> filteredWithExcess(List<Meal> meals, Predicate<Meal> mealFilter,
                                                   int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(mealFilter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static MealService getStorageStrategy() {
        MealService mealService;
        try {
            Properties prop = new Properties();
            InputStream inputStream = MealsUtil.class.getClassLoader().getResourceAsStream("/localstorage.properties");
            prop.load(inputStream);
            String storageClassName = prop.getProperty("mealstorageclassname");
            Class<?> clazz = Class.forName(storageClassName);
            Constructor<?> constructor = clazz.getConstructor();
            mealService = (MealService) constructor.newInstance();
            String profile = prop.getProperty("profile");
            if (profile.equals("dev")){
                initializeStorageData(mealService);
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return mealService;
    }

    private static void initializeStorageData(MealService mealService) {
        List<Meal> initialMeals = getTestData();
        initialMeals.forEach(mealService::add);
    }

    public static List<Meal> getTestData() {
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