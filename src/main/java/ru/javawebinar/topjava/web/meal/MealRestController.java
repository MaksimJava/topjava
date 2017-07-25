package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UnknownFormatFlagsException;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.AuthorizedUser.id;

public class MealRestController {
    public static final int NULL = 0;
    public static final int TIME = 1;
    public static final int FROM = 2;
    public static final int TO = 3;
    public static final int FULL = 5;

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

    public List<MealWithExceed> getAll(LocalDate datefrom, LocalDate dateto, LocalTime timefrom, LocalTime timeto) {
        int filter = generateFilter(datefrom, dateto, timefrom, timeto);
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
        switch (filter) {
            case NULL:
                return getAll();
            case TIME:
                return MealsUtil.getFilteredWithExceeded(all, timefrom, timeto, caloriesPerDay);
            case FROM:
                return MealsUtil.getFilteredByDateWithExceeded(all, datefrom, LocalDate.MAX, caloriesPerDay);
            case TO:
                return MealsUtil.getFilteredByDateWithExceeded(all, LocalDate.MIN, dateto, caloriesPerDay);
            case FULL:
                return MealsUtil.getFilteredByDateTimeWithExceeded(all, LocalDateTime.of(datefrom, timefrom), LocalDateTime.of(dateto, timeto), caloriesPerDay);
            default:
                throw new IllegalArgumentException();
        }
    }

    private int generateFilter(LocalDate datefrom, LocalDate dateto, LocalTime timefrom, LocalTime timeto) {
        if (datefrom == null && dateto == null && timefrom == null && timeto == null) {
            return NULL;
        }

        if (datefrom == null && dateto == null) {
            return TIME;
        }

        if (datefrom != null && dateto == null) {
            return FROM;
        }

        if (datefrom == null && dateto != null) {
            return TO;
        }

        if (datefrom != null && dateto != null && timefrom != null && timeto != null) {
            return FULL;
        }

        throw new UnknownFormatFlagsException("unknown condition");
    }
}