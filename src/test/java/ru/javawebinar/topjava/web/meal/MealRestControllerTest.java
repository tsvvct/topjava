package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

public class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getNotFound() throws Exception {
        validateRootCause(
                NotFoundException.class, () -> perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                        .andExpect(status().is4xxClientError())
        );
    }

    @Test
    void getNotOwn() throws Exception {
        validateRootCause(
                NotFoundException.class, () -> perform(MockMvcRequestBuilders.delete(REST_URL + ADMIN_MEAL_ID))
                        .andExpect(status().is4xxClientError())
        );
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(meals, authUserCaloriesPerDay())));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "between?startDate=2020-01-31&startTime=00:00&endDate=2020-01-31&endTime=23:59"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(List.of(meal7, meal6, meal5, meal4), authUserCaloriesPerDay())));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void updateNotOwn() throws Exception {
        Meal updated = MealTestData.getUpdated();
        updated.setId(ADMIN_MEAL_ID);
        validateRootCause(
                NotFoundException.class, () -> perform(MockMvcRequestBuilders.put(REST_URL + ADMIN_MEAL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated)))
                        .andExpect(status().isNoContent())
        );
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        validateRootCause(
                NotFoundException.class, () -> perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                        .andExpect(status().is4xxClientError())
        );
    }

    @Test
    void deleteNotOwn() throws Exception {
        validateRootCause(
                NotFoundException.class,
                () -> perform(MockMvcRequestBuilders.delete(REST_URL + ADMIN_MEAL_ID))
                        .andExpect(status().is4xxClientError())
        );
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void duplicateDateTimeCreate() throws Exception {
        //https://stackoverflow.com/questions/27987097/disabling-transaction-on-spring-testng-test-method
        //Не знаю надо ли проводить подобные тесты для REST контроллера, ведь такие случаи тестируются у нас в сервисе,
        //но было интересно понять почему они не работают и как можно их проводить. Наверняка есть более элегантный
        //способ, но я его не нашел. :`(
        Meal duplicatedMeal = MealTestData.getNew();
        duplicatedMeal.setDateTime(meal1.getDateTime());
        String mealJson = JsonUtil.writeValue(duplicatedMeal);

        AtomicReference<Exception> exc = new AtomicReference<>();
        Runnable task = () -> {
            try {
                perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mealJson))
                        .andExpect(status().isCreated());
            } catch (Exception e) {
                exc.set(e);
            }
        };

        validateRootCause(
                PSQLException.class, () -> {
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    executor.submit(task);
                    executor.shutdown();
                    while (!executor.isTerminated()) {
                        if (exc.get() != null) {
                            throw exc.get();
                        }
                    }
                    if (exc.get() != null) {
                        throw exc.get();
                    }
                }
        );
    }

    protected <T extends Throwable> void validateRootCause(Class<T> rootExceptionClass, Executable executable) {
        assertThrows(rootExceptionClass, () -> {
            try {
                executable.execute();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }
}
