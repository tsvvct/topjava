package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.StorageStrategy;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);
    private static final Integer MAX_CALORIES_PER_DAY = 2000;
    private static final String DATE_TIME_PICKER_FORMAT = "Y-m-d H:i";
    private static final String DATE_TIME_FORMAT_FOR_VIEW = "yyyy-MM-dd HH:mm";
    private static final String DATE_TIME_FORMAT_FROM_STORAGE = "yyyy-MM-dd'T'HH:mm";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_FOR_VIEW);
    private StorageStrategy<Meal> mealStorage;

    @Override
    public void init() throws ServletException {
        this.mealStorage = MealsUtil.getStorageStrategy();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals/doGet");

        String forward;
        String action = request.getParameter("action");
        if (action == null) {
            action = "listMeals";
        }

        switch (action) {
            case "delete": {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                mealStorage.delete(mealId);
                response.sendRedirect("meals");
                return;
            }
            case "edit": {
                forward = "/meal.jsp";
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = mealStorage.getById(mealId);
                request.setAttribute("meal", meal);
                break;
            }
            case "listMeals": {
                forward = "/meals.jsp";
                List<MealTo> mealsTo = MealsUtil.getAllWithExcess(mealStorage.getAll(), MAX_CALORIES_PER_DAY);
                request.setAttribute("userMeals", mealsTo);
                break;
            }
            default: {
                forward = "meals";
            }
        }

        request.setAttribute("dateTimeFormatForView", DATE_TIME_FORMAT_FOR_VIEW);
        request.setAttribute("dateTimePickerFormat", DATE_TIME_PICKER_FORMAT);
        request.setAttribute("dateTimeFormatFromStorage", DATE_TIME_FORMAT_FROM_STORAGE);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals/doPost");

        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), DATE_TIME_FORMATTER);
        Meal meal =  new Meal(dateTime, description, calories);

        String stringMealId = request.getParameter("mealId");
       if (stringMealId == null || stringMealId.isEmpty()) {
            mealStorage.add(meal);
        } else {
            int mealId = Integer.parseInt(stringMealId);
            meal.setId(mealId);
            mealStorage.update(meal);
        }

        response.sendRedirect("meals");
    }
}