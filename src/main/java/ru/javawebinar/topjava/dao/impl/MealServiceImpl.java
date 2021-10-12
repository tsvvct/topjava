package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.util.IntSequenceGenerator;
import ru.javawebinar.topjava.dao.MealService;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MealServiceImpl implements MealService {

    private final ConcurrentMap<Integer, Meal> storage;
    private final IntSequenceGenerator sequenceGenerator;

    public MealServiceImpl() {
        this.sequenceGenerator = new IntSequenceGenerator();
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public Meal getById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        storage.replace(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal add(Meal meal) {
        meal.setId(getNextId());
        storage.put(meal.getId(), meal);
        return meal;
    }

    public int getNextId() {
        return sequenceGenerator.getNext();
    }
}
