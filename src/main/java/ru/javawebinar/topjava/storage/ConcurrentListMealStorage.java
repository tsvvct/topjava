package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentListMealStorage implements StorageStrategy<Meal> {
    private final List<Meal> storage;
    private final SequenceGenerator sequenceGenerator;

    public ConcurrentListMealStorage() {
        this.sequenceGenerator = new AtomicSequenceGenerator();
        this.storage = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addAll(List<Meal> meals) {
        storage.addAll(meals);
    }

    @Override
    public int getNextId() {
        return sequenceGenerator.getNext();
    }

    @Override
    public Meal getById(int id) {
        return storage.stream().filter(meal -> meal.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Meal> getAll() {
        return storage;
    }

    @Override
    public void delete(int id) {
        storage.removeIf(meal -> meal.getId() == id);
    }

    @Override
    public void update(Meal meal) {
        Meal mealAtStorage = getById(meal.getId());
        if(mealAtStorage != null) {
            storage.set(storage.indexOf(mealAtStorage), meal);
        }
    }

    @Override
    public void add(Meal meal) {
        meal.setId(getNextId());
        storage.add(meal);
    }
}
