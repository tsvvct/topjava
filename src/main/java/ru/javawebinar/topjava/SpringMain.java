package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("=========================================================");
            System.out.println("Spring configured beans");
            System.out.println("=========================================================");
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            System.out.println("=========================================================");
            System.out.println("Create bean: AdminRestController");
            System.out.println("=========================================================");
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            System.out.println("=========================================================");
            System.out.println("test userRestController");
            System.out.println("test create method");
            System.out.println("=========================================================");
            User userOne = adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            System.out.println("=========================================================");
            System.out.println("test get, update, delete methods");
            System.out.println("=========================================================");
            User userTwo = adminUserController.create(new User(null, "userName", "emailTwo@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName", "emailThree@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName", "emailFour@mail.ru", "password", Role.ADMIN));
            adminUserController.getAll().forEach(System.out::println);
            System.out.println("----- have to get same result");
            adminUserController.getAll().forEach(System.out::println);
            System.out.println("----- have to get same result");
            adminUserController.getAll().forEach(System.out::println);

            User someUser = adminUserController.get(1);
            System.out.println(someUser);

            User newUserOne = new User(null, "newUserName", "emailNew@mail.ru", "newPassword", Role.ADMIN);
            newUserOne.setId(1);
            adminUserController.update(newUserOne, 1);
            adminUserController.getAll().forEach(System.out::println);

            adminUserController.delete(1);
            adminUserController.getAll().forEach(System.out::println);

            System.out.println("=========================================================");
            System.out.println("Create bean: MealRestController");
            System.out.println("=========================================================");
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println("=========================================================");
            System.out.println("test mealRestController");
            System.out.println("test create method");
            System.out.println("=========================================================");

            SecurityUtil.setAuthUserId(userOne.getId());
            mealRestController.getAll().forEach(System.out::println);
            mealRestController.create(MealsUtil.meals.get(0));
            mealRestController.create(MealsUtil.meals.get(1));
            mealRestController.getAll().forEach(System.out::println);
            SecurityUtil.setAuthUserId(userOne.getId());
            mealRestController.getAll().forEach(System.out::println);
            mealRestController.create(MealsUtil.meals.get(2));
            mealRestController.create(MealsUtil.meals.get(3));
            mealRestController.getAll().forEach(System.out::println);
            System.out.println("=========================================================");
            System.out.println("test get, update, delete methods");
            System.out.println("=========================================================");
            SecurityUtil.setAuthUserId(userOne.getId());
            Meal mealOne = mealRestController.get(1);
            System.out.println(mealOne);

            Meal newMealOne = MealsUtil.meals.get(4);
            newMealOne.setId(1);
            mealRestController.update(newMealOne, mealOne.getId());

            mealRestController.delete(newMealOne.getId());
            mealRestController.getAll().forEach(System.out::println);

            SecurityUtil.setAuthUserId(userTwo.getId());
            try {
                mealRestController.get(0);
            } catch (NotFoundException ex) {
                System.out.println("Can't get others meal");
            }

            try {
                mealOne.setId(2);
                mealRestController.update(mealOne, 2);
            } catch (NotFoundException ex) {
                System.out.println("Can't update others meal");
            }

            try {
                mealRestController.delete(2);
            } catch (NotFoundException ex) {
                System.out.println("Can't delete others meal");
            }
        }
    }
}