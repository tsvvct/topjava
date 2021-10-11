package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapMealStorage implements StorageStrategy<Meal> {

    private final ConcurrentMap<Integer, Meal> storage;
    private final SequenceGenerator sequenceGenerator;

    public ConcurrentMapMealStorage() {
        this.sequenceGenerator = new AtomicSequenceGenerator();
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public void addAll(List<Meal> meals) {
        for (Meal meal : meals) {
            storage.put(meal.getId(), meal);
        }
    }

    @Override
    public int getNextId() {
        return sequenceGenerator.getNext();
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
    public void update(Meal meal) {
        storage.replace(meal.getId(), meal);
    }

    @Override
    public void add(Meal meal) {
        meal.setId(getNextId());
        storage.put(meal.getId(), meal);
    }
}
