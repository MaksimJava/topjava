package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if("sort".equalsIgnoreCase(action)) {
            doFilter(request, response);
        } else {
            String id = request.getParameter("id");
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    1, LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            mealRestController.update(meal);
            response.sendRedirect("meals");
        }
    }

    private void doFilter(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LocalDate datefrom = null;
        try {
            datefrom = LocalDate.parse(req.getParameter("datefrom"));
        } catch (Exception e) {
            //NOP
        }

        LocalDate dateto = null;
        try {
            dateto = LocalDate.parse(req.getParameter("dateto"));
        } catch (Exception e) {
            //NOP
        }

        LocalTime timefrom = null;
        try {
            timefrom = LocalTime.parse(req.getParameter("timefrom"));
        } catch (Exception e) {
            timefrom = LocalTime.MIN;
        }
        LocalTime timeto = null;
        try {
            timeto = LocalTime.parse(req.getParameter("timeto"));
        } catch (Exception e) {
            timefrom = LocalTime.MAX;
        }

        req.setAttribute("meals", mealRestController.getAll(datefrom, dateto, timefrom, timeto));
        req.getRequestDispatcher("/meals.jsp").forward(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            getAll(request, response);
        } else if (action.equals("delete")) {
            delete(request, response);
        } else if (action.equals("create") || action.equals("update")) {
            createUpdate(request, response, action);
        }
    }

    private void createUpdate(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
        final Meal meal = action.equals("create") ?
                new Meal(LocalDateTime.now().withNano(0).withSecond(0), "", 1000, 1) :
                mealRestController.get(getId(request));
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("meal.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getId(request);
        log.info("Delete {}", id);
        mealRestController.delete(id);
        response.sendRedirect("meals");
    }

    private void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("getAll");
        request.setAttribute("meals", mealRestController.getAll());
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
