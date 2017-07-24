package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.AuthorizedUser.id;

public class MealRestController {
    @Autowired
    private MealService service;

    @Autowired
    private UserService userService;

    public List<MealWithExceed> getAll() {
        List<Meal> all = service.getAll();

        all = all
                .stream()
                .filter(userMeal -> id() == userMeal.getUserId())
                .sorted((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()))
                .collect(Collectors.toList());

        if (all.isEmpty()) {
            throw new NotFoundException("");
        }

        int caloriesPerDay = userService.get(AuthorizedUser.id()).getCaloriesPerDay();

        return
                MealsUtil
                        .getFilteredWithExceeded(all, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    public Meal get(Integer id) {
        Meal meal = service.get(id);
        if (id() != meal.getUserId()) {
            throw new NotFoundException("");
        }

        return meal;
    }

    public Meal save(Meal meal) {
        if (id() != meal.getUserId()) {
            throw new NotFoundException("");
        }

        return service.save(meal);
    }

    public void delete(Integer id) {
        Meal meal = service.get(id);
        if (id() != meal.getUserId()) {
            throw new NotFoundException("");
        }

        service.delete(id);
    }

    public void update(Meal meal) {
        if (id() != meal.getUserId()) {
            throw new NotFoundException("");
        }

        service.update(meal);
    }

}