package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.I18nMessageResolver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;

class AdminRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-email?email=" + user.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), updated);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicatedEmail() throws Exception {
        User updated = getUpdated();
        updated.setEmail(admin.getEmail());
        String expectedErrDetails = i18nMessageResolver.getMessage(I18nMessageResolver.EXCEPTION_DUPLICATE_EMAIL);//"User with this email already exists";
        perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.detail").value(expectedErrDetails));

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicatedEmail() throws Exception {
        User newUser = getNew();
        newUser.setEmail(user.getEmail());
        String expectedErrDetails = i18nMessageResolver.getMessage(I18nMessageResolver.EXCEPTION_DUPLICATE_EMAIL);//"User with this email already exists";
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.detail").value(expectedErrDetails));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNotValidName() throws Exception {
        User newUser = getNew();
        newUser.setName("");
        createWithNotValidData(jsonWithPassword(newUser, newUser.getPassword()), REST_URL, admin,
                "не должно быть пустым");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNotValidEmail() throws Exception {
        User newUser = getNew();
        newUser.setEmail("");
        createWithNotValidData(jsonWithPassword(newUser, newUser.getPassword()), REST_URL, admin,
                "не должно быть пустым");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNotValidPassword() throws Exception {
        User newUser = getNew();
        newUser.setPassword("");
        createWithNotValidData(jsonWithPassword(newUser, newUser.getPassword()), REST_URL, admin,
                "не должно быть пустым");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNotValidCaloriesPerDay() throws Exception {
        User newUser = getNew();
        newUser.setCaloriesPerDay(0);
        createWithNotValidData(jsonWithPassword(newUser, newUser.getPassword()), REST_URL, admin,
                "должно находиться в диапазоне");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotValidName() throws Exception {
        User updated = getUpdated();
        updated.setName("");
        updateWithNotValidData(jsonWithPassword(updated, updated.getPassword()), REST_URL + USER_ID, admin,
                "не должно быть пустым", "размер должен находиться в диапазоне");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotValidEmail() throws Exception {
        User updated = getUpdated();
        updated.setEmail("");
        updateWithNotValidData(jsonWithPassword(updated, updated.getPassword()), REST_URL + USER_ID, admin,
                "не должно быть пустым");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotValidPassword() throws Exception {
        User updated = getUpdated();
        updated.setPassword("");
        updateWithNotValidData(jsonWithPassword(updated, updated.getPassword()), REST_URL + USER_ID, admin,
                "не должно быть пустым", "размер должен находиться в диапазоне");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotValidCaloriesPerDay() throws Exception {
        User updated = getUpdated();
        updated.setCaloriesPerDay(0);
        updateWithNotValidData(jsonWithPassword(updated, updated.getPassword()), REST_URL + USER_ID, admin,
                "должно находиться в диапазоне");
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, user));
    }

    @Test
    void getWithMeals() throws Exception {
        assumeDataJpa();
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID + "/with-meals")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_MEALS_MATCHER.contentJson(admin));
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userService.get(USER_ID).isEnabled());
    }
}