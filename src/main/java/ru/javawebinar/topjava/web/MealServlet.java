package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Function;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController repository;

    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        repository = appCtx.getBean(MealRestController.class);
        // initialize meal repository with test data for different users
        Function<Meal, Integer> userIdSelector = meal -> (MealsUtil.meals.indexOf(meal) < 8) ? 1 : 2;
        MealsUtil.meals.forEach(meal -> repository.create(meal, userIdSelector.apply(meal)));
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String isFilter = request.getParameter("isFilterSubmit");
        if (isFilter != null) {
            saveFilterToSession(request);
        } else {
            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            repository.save(meal);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                repository.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        repository.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", repository.getAll(getAttributeOrDefault(request, "dateFrom"),
                        getAttributeOrDefault(request, "dateTo"), getAttributeOrDefault(request, "timeFrom"),
                        getAttributeOrDefault(request, "timeTo")));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private void saveFilterToSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("dateFrom", request.getParameter("dateFrom"));
        session.setAttribute("dateTo", request.getParameter("dateTo"));
        session.setAttribute("timeFrom", request.getParameter("timeFrom"));
        session.setAttribute("timeTo", request.getParameter("timeTo"));
    }

    private String getAttributeOrDefault(HttpServletRequest request, String name) {
        HttpSession session = request.getSession();
        String sessionAttribute = (String) session.getAttribute(name);

        if (sessionAttribute == null) {
            sessionAttribute = "";
        }

        return sessionAttribute;
    }
}
