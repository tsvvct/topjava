package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class MealDataJpaRepositoryTest extends MealServiceTest {
    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        adminMeal1.setUser(admin);
        MEAL_MATCHER_WITH_USER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(NOT_FOUND, USER_ID));
    }

    @Test
    public void getWithUserNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getAllWithUser() {
        meals.forEach(meal -> meal.setUser(user));
        MEAL_MATCHER_WITH_USER.assertMatch(service.getAllWithUser(USER_ID), meals);
    }

    @Test
    public void getBetweenInclusiveWithUser() {
        meal3.setUser(user);
        meal2.setUser(user);
        meal1.setUser(user);
        MEAL_MATCHER_WITH_USER.assertMatch(service.getBetweenInclusiveWithUser(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDatesWithUser() {
        meals.forEach(meal -> meal.setUser(user));
        MEAL_MATCHER_WITH_USER.assertMatch(service.getBetweenInclusiveWithUser(null, null, USER_ID), meals);
    }
}
