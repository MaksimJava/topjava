package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MealTestData {

    public static final List<Meal> USER_MEALS = Arrays.asList(
            new Meal(100007, LocalDateTime.of(2016, 12, 25, 19, 0, 0, 0), "Ужин", 510),
            new Meal(100006, LocalDateTime.of(2016, 12, 25, 15, 0, 0, 0), "Обед", 1000),
            new Meal(100005, LocalDateTime.of(2016, 12, 25, 9, 0, 0, 0), "Завтрак", 500),
            new Meal(100004, LocalDateTime.of(2016, 12, 24, 19, 0, 0, 0), "Ужин", 500),
            new Meal(100003, LocalDateTime.of(2016, 12, 24, 15, 0, 0, 0), "Обед", 1000),
            new Meal(100002, LocalDateTime.of(2016, 12, 24, 9, 0, 0, 0), "Завтрак", 500)
    );

    public static final List<Meal> ADMIN_MEALS = Arrays.asList(
            new Meal(100013, LocalDateTime.of(2016, 12, 25, 19, 0, 0, 0), "Ужин", 510),
            new Meal(100012, LocalDateTime.of(2016, 12, 25, 15, 0, 0, 0), "Обед", 1000),
            new Meal(100011, LocalDateTime.of(2016, 12, 25, 9, 0, 0, 0), "Завтрак", 500),
            new Meal(100010, LocalDateTime.of(2016, 12, 24, 19, 0, 0, 0), "Ужин", 500),
            new Meal(100009, LocalDateTime.of(2016, 12, 24, 15, 0, 0, 0), "Обед", 1000),
            new Meal(100008, LocalDateTime.of(2016, 12, 24, 9, 0, 0, 0), "Завтрак", 500)
    );

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>(
            (expected, actual) -> expected == actual || Objects.equals(expected.toString(), actual.toString())
    );

}
