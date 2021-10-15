package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private Map<Integer, Meal> currentUserRepository;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, int userId) {
        currentUserRepository = getUserMealRepository(userId);
        synchronized (meal) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                currentUserRepository.put(meal.getId(), meal);
                return meal;
            }
            return currentUserRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        currentUserRepository = getUserMealRepository(userId);
        return currentUserRepository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        currentUserRepository = getUserMealRepository(userId);
        return currentUserRepository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        currentUserRepository = getUserMealRepository(userId);
        return currentUserRepository.values().stream()
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getUserMealRepository(int id) {
        repository.computeIfAbsent(id, ConcurrentHashMap::new);
        return repository.get(id);
    }
}

