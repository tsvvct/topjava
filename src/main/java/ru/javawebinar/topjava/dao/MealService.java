package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    Meal getById(int id);

    List<Meal> getAll();

    void delete(int id);

    Meal update(Meal item);

    Meal add(Meal item);
}
