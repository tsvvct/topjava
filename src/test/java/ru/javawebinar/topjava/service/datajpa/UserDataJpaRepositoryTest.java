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
public class UserDataJpaRepositoryTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User actualUser = service.getWithMeals(USER_ID);
        user.setMeals(MealTestData.meals);
        USER_MATCHER_WITH_MEALS.assertMatch(actualUser, user);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void getByEmailWithMeals() {
        User user = service.getByEmailWithMeals("admin@gmail.com");
        admin.setMeals(MealTestData.adminMeals);
        USER_MATCHER_WITH_MEALS.assertMatch(user, admin);
    }

    @Test
    public void getAllWithMeals() {
        List<User> all = service.getAllWithMeals();
        admin.setMeals(MealTestData.adminMeals);
        user.setMeals(MealTestData.meals);
        USER_MATCHER_WITH_MEALS.assertMatch(all, admin, user);
    }
}
