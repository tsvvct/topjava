package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.DateTimeUtil.getValueFilter;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseOrNull;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(authUserId()),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAll(String dateFrom, String dateTo, String timeFrom, String timeTo) {
        Predicate<Meal> timeFilter = getValueFilter(Meal::getTime, parseOrNull(LocalTime::parse, timeFrom),
                parseOrNull(LocalTime::parse, timeTo), true, false);
        List<Meal> filteredMeals = service.getAll(authUserId(), parseOrNull(LocalDate::parse, dateFrom),
                parseOrNull(LocalDate::parse, dateTo));
        return MealsUtil.filterByPredicate(filteredMeals, MealsUtil.DEFAULT_CALORIES_PER_DAY, timeFilter);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public Meal update(Meal meal, int id) {
        log.info("update {} with id: {}", meal, id);
        assureIdConsistent(meal, id);
        return service.update(meal, authUserId());
    }
}