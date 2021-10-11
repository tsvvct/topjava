package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealService;
import ru.javawebinar.topjava.dao.impl.MealServiceImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
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
    private final int MAX_CALORIES_PER_DAY = 2000;
    private final String DATE_TIME_FORMAT_FOR_VIEW = "yyyy.MM.dd HH:mm";
    private final String DATE_TIME_FORMAT_FROM_STORAGE = "yyyy-MM-dd'T'HH:mm";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
    private final MealService mealService;

    public MealServlet() {
        super();
        this.mealService = new MealServiceImpl(MealsUtil.getStorageStrategy());
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

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealService.deleteMeal(mealId);
            forward = "/meals.jsp";
            List<MealTo> mealsTo = MealsUtil.getAllWithExcess(mealService.getAllMeals(), MAX_CALORIES_PER_DAY);
            request.setAttribute("userMeals", mealsTo);
        } else if (action.equalsIgnoreCase("edit")) {
            forward = "/meal.jsp";
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealService.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = "/meals.jsp";
            List<MealTo> mealsTo = MealsUtil.getAllWithExcess(mealService.getAllMeals(), MAX_CALORIES_PER_DAY);
            request.setAttribute("userMeals", mealsTo);
        } else {
            forward = "/meal.jsp";
        }
        request.setAttribute("dateTimeFormatForView", DATE_TIME_FORMAT_FOR_VIEW);
        request.setAttribute("dateTimeFormatFromStorage", DATE_TIME_FORMAT_FROM_STORAGE);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals/doPost");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        log.debug("DateTime value: {}" ,request.getParameter("dateTime"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), dateTimeFormatter);
        Meal meal =  new Meal(dateTime, description, calories);

        String stringMealId = request.getParameter("mealId");

        if (stringMealId == null || stringMealId.isEmpty()) {
            mealService.addMeal(meal);
        } else {
            int mealId = Integer.parseInt(stringMealId);
            meal.setId(mealId);
            mealService.updateMeal(meal);
        }

        List<MealTo> mealsTo = MealsUtil.getAllWithExcess(mealService.getAllMeals(), MAX_CALORIES_PER_DAY);
        request.setAttribute("userMeals", mealsTo);
        request.setAttribute("dateTimeFormatForView", DATE_TIME_FORMAT_FOR_VIEW);
        request.setAttribute("dateTimeFormatFromStorage", DATE_TIME_FORMAT_FROM_STORAGE);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
