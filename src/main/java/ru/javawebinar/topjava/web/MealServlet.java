package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealService;
import ru.javawebinar.topjava.dao.impl.MealServiceImpl;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final int MAX_CALORIES_PER_DAY = 2000;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    private final MealService mealService;

    public MealServlet() {
        super();
        this.mealService = new MealServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<MealTo> mealsTo = MealsUtil.getAllWithExcess(mealService.getAllMeals(), MAX_CALORIES_PER_DAY);
        request.setAttribute("userMeals", mealsTo);
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

}
