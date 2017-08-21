package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Created by Максим on 20.08.2017.
 */
@Controller
public class MealController {

    @Autowired
    private MealRestController mealController;

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String meals(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            return getAll(request);
        } else if (action.equals("delete")) {
            return delete(request);
        } else if (action.equals("create")) {
            return create(request);
        } else if (action.equals("update")) {
            return update(request);
        } else {
            return "index";
        }
    }

    private String getAll(HttpServletRequest request) {
        request.setAttribute("mealList", mealController.getAll());
        return "mealList";
    }

    @RequestMapping(value = "/meals?action=delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        mealController.delete(id);
        return "redirect:meals";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(HttpServletRequest request){
        final Meal meal = new Meal(LocalDateTime.now().withNano(0).withSecond(0), "", 1000);
        request.setAttribute("meal", meal);
        return "mealEdit";
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    public String update(HttpServletRequest request) {
        final Meal meal = mealController.get(getId(request));
        request.setAttribute("meal", meal);
        return "mealEdit";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    @RequestMapping(value = "/meals", method = RequestMethod.POST)
    public String mealsPost(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            final Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            if (request.getParameter("id").isEmpty()) {
                mealController.create(meal);
            } else {
                mealController.update(meal, getId(request));
            }
            return "redirect:meals";

        } else if (action.equals("filter")) {
            return filter(request);
        } else {
            return "index";
        }
    }

    private String filter(HttpServletRequest request) {
        LocalDate startDate = DateTimeUtil.parseLocalDate(resetParam("startDate", request));
        LocalDate endDate = DateTimeUtil.parseLocalDate(resetParam("endDate", request));
        LocalTime startTime = DateTimeUtil.parseLocalTime(resetParam("startTime", request));
        LocalTime endTime = DateTimeUtil.parseLocalTime(resetParam("endTime", request));
        request.setAttribute("mealList", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "mealList";
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}
