package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    List<Meal> getAllMeals();

    Meal getMealById(int id);

    void deleteMeal(int id);

    void updateMeal(Meal meal);

    void addMeal(Meal meal);
}
