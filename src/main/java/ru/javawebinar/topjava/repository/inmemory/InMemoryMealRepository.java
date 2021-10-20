package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.getValueFilter;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            getUserMealRepository(userId).put(meal.getId(), meal);
            return meal;
        }
        return getUserMealRepository(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return getUserMealRepository(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return getUserMealRepository(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAll(userId, meal -> true);
    }

    private List<Meal> getAll(int userId, Predicate<Meal> filter) {
        return getUserMealRepository(userId).values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate dateFrom, LocalDate dateTo) {
        return getAll(userId, getValueFilter(Meal::getDate, dateFrom, dateTo, true, true));
    }

    private Map<Integer, Meal> getUserMealRepository(int id) {
        return repository.computeIfAbsent(id, idParam -> new ConcurrentHashMap<>());
    }
}