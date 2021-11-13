package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User actualUser = service.getWithMeals(USER_ID);
        User expectedUser = new User(user);
        expectedUser.setMeals(MealTestData.meals);
        USER_MATCHER_WITH_MEALS.assertMatch(actualUser, expectedUser);
    }

    @Test
    public void getWithMealsForUserWithoutMeals() {
        User actualUser = service.getWithMeals(USER_WO_MEALS_ID);
        USER_MATCHER_WITH_MEALS.assertMatch(actualUser, userWoMeals);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void getByEmailWithMeals() {
        User user = service.getByEmailWithMeals("admin@gmail.com");
        User expectedUser = new User(admin);
        expectedUser.setMeals(MealTestData.adminMeals);
        USER_MATCHER_WITH_MEALS.assertMatch(user, expectedUser);
    }

    @Test
    public void getAllWithMeals() {
        List<User> all = service.getAllWithMeals();
        User expectedUser1 = new User(admin);
        expectedUser1.setMeals(MealTestData.adminMeals);
        User expectedUser2 = new User(user);
        expectedUser2.setMeals(MealTestData.meals);
        USER_MATCHER_WITH_MEALS.assertMatch(all, expectedUser1, expectedUser2, userWoMeals);
    }
}
