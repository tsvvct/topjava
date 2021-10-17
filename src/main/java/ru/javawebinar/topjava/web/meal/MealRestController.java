package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.DateTimeUtil.getValueFilter;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseOrNull;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController extends AbstractMealController {
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            return super.create(meal, authUserId());
        } else {
            return super.update(meal, meal.getId(), authUserId());
        }
    }

    public Meal get(int id) {
        return super.get(id, authUserId());
    }

    public void delete(int id) {
        super.delete(id, authUserId());
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(super.getAll(authUserId(), meal -> true),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAll(String dateFrom, String dateTo, String timeFrom, String timeTo) {
        Predicate<Meal> dateFilter = getValueFilter(Meal::getDate, parseOrNull(LocalDate::parse, dateFrom),
                parseOrNull(LocalDate::parse, dateTo), true, true);
        Predicate<Meal> timeFilter = getValueFilter(Meal::getTime, parseOrNull(LocalTime::parse, timeFrom),
                parseOrNull(LocalTime::parse, timeTo), true, false);
        return MealsUtil.filterByPredicate(super.getAll(authUserId(), dateFilter),
                MealsUtil.DEFAULT_CALORIES_PER_DAY, timeFilter);
    }
}