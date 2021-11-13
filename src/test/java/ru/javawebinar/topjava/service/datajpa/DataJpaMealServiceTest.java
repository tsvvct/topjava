package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        Meal expectedMeal = getMealCopy(adminMeal1);
        expectedMeal.setUser(admin);
        MEAL_MATCHER_WITH_USER.assertMatch(actual, expectedMeal);
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
        List<Meal> expectedMeals = meals.stream()
                .map(MealTestData::getMealCopy)
                .peek(meal -> meal.setUser(user))
                .toList();
        MEAL_MATCHER_WITH_USER.assertMatch(service.getAllWithUser(USER_ID), expectedMeals);
    }

    @Test
    public void getBetweenInclusiveWithUser() {
        Meal expectedMeal3 = getMealCopy(meal3);
        expectedMeal3.setUser(user);
        Meal expectedMeal2 = getMealCopy(meal2);
        expectedMeal2.setUser(user);
        Meal expectedMeal1 = getMealCopy(meal1);
        expectedMeal1.setUser(user);

        MEAL_MATCHER_WITH_USER.assertMatch(service.getBetweenInclusiveWithUser(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                expectedMeal3, expectedMeal2, expectedMeal1);
    }

    @Test
    public void getBetweenWithNullDatesWithUser() {
        List<Meal> expectedMeals = meals.stream()
                .map(MealTestData::getMealCopy)
                .peek(meal -> meal.setUser(user))
                .toList();
        MEAL_MATCHER_WITH_USER.assertMatch(service.getBetweenInclusiveWithUser(null, null, USER_ID), expectedMeals);
    }
}
