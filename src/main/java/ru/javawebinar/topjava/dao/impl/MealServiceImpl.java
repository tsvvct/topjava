package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.MealService;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;

public class MealServiceImpl implements MealService {

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(MealsUtil.getMealTestData());
    }
}
