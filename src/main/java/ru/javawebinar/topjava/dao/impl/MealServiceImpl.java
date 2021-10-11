package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.MealService;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.StorageStrategy;

import java.util.List;

public class MealServiceImpl implements MealService {

    private final StorageStrategy<Meal> storage;

    public MealServiceImpl(StorageStrategy<Meal> storage) {
        this.storage = storage;
    }

    @Override
    public List<Meal> getAllMeals() {
        return storage.getAll();
    }

    @Override
    public Meal getMealById(int id) {
        return storage.getById(id);
    }

    @Override
    public void deleteMeal(int id) {
        storage.delete(id);
    }

    @Override
    public void updateMeal(Meal meal) {
        storage.update(meal);
    }

    @Override
    public void addMeal(Meal meal) {
        storage.add(meal);
    }
}
