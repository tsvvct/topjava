package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserCalories;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 10, 30), "Кофе с собой", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("-=== filteredByCycles ===-");
        filteredByCycles(meals, LocalTime.of(7, 0),LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
        System.out.println("-=== filteredByStreams ===-");
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
        System.out.println("-=== optionalTwoFilteredByCycles ===-");
        optionalTwoFilteredByCycles(meals, LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000).forEach(System.out::println);
        System.out.println("-=== optionalTwoFilteredByStreams ===-");
        optionalTwoFilteredByStreams(meals, LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDays = new HashMap<>();
        for (UserMeal userMeal : meals) {
            totalCaloriesByDays.merge(userMeal.getDate(), userMeal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime)) {
                userMealWithExcessList.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), totalCaloriesByDays.get(userMeal.getDate()) > caloriesPerDay));
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDays = meals.stream()
                .collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        totalCaloriesByDays.get(userMeal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> optionalTwoFilteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                                       LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        Map<LocalDate, Integer> totalCaloriesByDays = new HashMap<>();
        UserCalories userCalories = new UserCalories(caloriesPerDay, totalCaloriesByDays);

        for (UserMeal userMeal : meals) {
            totalCaloriesByDays.merge(userMeal.getDate(), userMeal.getCalories(), Integer::sum);

            if (TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime)) {
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(), userCalories);
                userMealWithExcessList.add(userMealWithExcess);
            }
        }

        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> optionalTwoFilteredByStreams(List<UserMeal> meals,
                                                                        LocalTime startTime,
                                                                        LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate))
                .values().stream()
                .flatMap(userMealList -> {
                    int totalCaloriesByDay = userMealList.stream()
                            .map(UserMeal::getCalories).reduce(0, Integer::sum);
                    return userMealList.stream()
                            .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime))
                            .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                                    userMeal.getCalories(), totalCaloriesByDay > caloriesPerDay));
                })
                .collect(Collectors.toList());
    }
}
