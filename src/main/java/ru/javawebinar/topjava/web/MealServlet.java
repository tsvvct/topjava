package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.dao.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);
    private static final Integer maxCaloriesPerDay = 2000;

    private MealService mealService;

    @Override
    public void init() throws ServletException {
        this.mealService = MealsUtil.getStorageStrategy();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals/doGet");

        String forward;
        Action action = Action.getOrDefault(request.getParameter("action"));
        log.debug("    action: {}", action);

        switch (action) {
            case CREATE: {
                forward = "/meal.jsp";
                break;
            }
            case LIST: {
                forward = "/meals.jsp";
                List<MealTo> mealsTo = MealsUtil.getAllWithExcess(mealService.getAll(), maxCaloriesPerDay);
                request.setAttribute("userMeals", mealsTo);
                break;
            }
            case UPDATE: {
                forward = "/meal.jsp";
                Meal meal = mealService.getById(parseId(request));
                request.setAttribute("meal", meal);
                break;
            }
            case DELETE: {
                mealService.delete(parseId(request));
                log.debug("    meal with id: {} removed", parseId(request));
                forward = "meals";
                log.debug("    redirect to: {}", forward);
                response.sendRedirect(forward);
                return;
            }
            default: {
                log.debug("Such action: {} isn't supported.", request.getParameter("action"));
                throw new ServletException("Such action: " + request.getParameter("action") + " isn't supported.");
            }
        }

        request.setAttribute("dateTimeFormatForView", ServletUtil.DATE_TIME_FORMAT_FOR_VIEW);
        request.setAttribute("dateTimePickerFormat", ServletUtil.DATE_TIME_PICKER_FORMAT);
        request.setAttribute("dateTimeFormatFromStorage", ServletUtil.DATE_TIME_FORMAT_FROM_STORAGE);
        log.debug("    forward to: {}", forward);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals/doPost");

        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), ServletUtil.DATE_TIME_FORMATTER);
        Meal meal =  new Meal(dateTime, description, calories);

        String stringMealId = request.getParameter("mealId");
       if (stringMealId == null || stringMealId.isEmpty()) {
            mealService.add(meal);
            log.debug("    meal created: {}", meal);
        } else {
            meal.setId(parseId(request));
            mealService.update(meal);
            log.debug("    meal updated: {}", meal);
        }

       log.debug("    redirect to: {}", "meals");
        response.sendRedirect("meals");
    }

    private int parseId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("mealId"));
    }
}