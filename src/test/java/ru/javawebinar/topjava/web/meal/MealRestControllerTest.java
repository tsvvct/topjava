package ru.javawebinar.topjava.web.meal;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.I18nMessageResolver;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.user;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_MEAL_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + ADMIN_MEAL_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotValidDateTime() throws Exception {
        Meal updated = getUpdated();
        updated.setDateTime(null);
        updateWithNotValidData(JsonUtil.writeValue(updated), REST_URL + MEAL1_ID, user,
                "не должно равняться null");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotValidDescription() throws Exception {
        Meal updated = getUpdated();
        updated.setDescription("");
        updateWithNotValidData(JsonUtil.writeValue(updated), REST_URL + MEAL1_ID, user,
                "не должно быть пустым", "размер должен находиться в диапазоне");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotValidCalories() throws Exception {
        Meal updated = getUpdated();
        updated.setCalories(null);
        updateWithNotValidData(JsonUtil.writeValue(updated), REST_URL + MEAL1_ID, user,
                "не должно равняться null");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithDuplicatedDateTime() throws Exception {
        Meal newMeal = getNew();
        newMeal.setDateTime(meal1.getDateTime());
        String expectedErrDetails = i18nMessageResolver.getMessage(I18nMessageResolver.EXCEPTION_DUPLICATE_DATETIME);//"Meal with such date time already exist";
        createWithNotValidData(JsonUtil.writeValue(newMeal), REST_URL, user, expectedErrDetails);

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithNotValidDateTime() throws Exception {
        Meal newMeal = getNew();
        newMeal.setDateTime(null);
        createWithNotValidData(JsonUtil.writeValue(newMeal), REST_URL, user, "не должно равняться null");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithNotValidDescription() throws Exception {
        Meal newMeal = getNew();
        newMeal.setDescription("");
        createWithNotValidData(JsonUtil.writeValue(newMeal), REST_URL, user, "не должно быть пустым", "размер должен находиться в диапазоне");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithNotValidCalories() throws Exception {
        Meal newMeal = getNew();
        newMeal.setCalories(null);
        createWithNotValidData(JsonUtil.writeValue(newMeal), REST_URL, user, "не должно равняться null");
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(newMeal)));

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(getTos(meals, user.getCaloriesPerDay())));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-01-30").param("startTime", "07:00")
                .param("endDate", "2020-01-31").param("endTime", "11:00")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(TO_MATCHER.contentJson(createTo(meal5, true), createTo(meal1, false)));
    }

    @Test
    void getBetweenAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endTime=")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(TO_MATCHER.contentJson(getTos(meals, user.getCaloriesPerDay())));
    }
}