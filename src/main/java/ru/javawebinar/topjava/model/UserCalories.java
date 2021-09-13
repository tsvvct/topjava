package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.util.Map;

public class UserCalories {
    private final Integer maxCaloriesPerDay;

    private final Map<LocalDate, Integer> totalCaloriesByDay;

    public UserCalories(Integer maxCaloriesPerDay, Map<LocalDate, Integer> totalCaloriesByDay) {
        this.maxCaloriesPerDay = maxCaloriesPerDay;
        this.totalCaloriesByDay = totalCaloriesByDay;
    }

    public boolean isCaloriesExcessForDay(LocalDate localDate) {
        return totalCaloriesByDay.get(localDate) > maxCaloriesPerDay;
    }

}
