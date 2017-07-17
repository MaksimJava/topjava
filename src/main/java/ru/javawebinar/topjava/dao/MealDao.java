package ru.javawebinar.topjava.dao;

/**
 * Created by Максим on 17.07.2017.
 */
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    List<Meal> getAllMeals();
    Meal getMealById(int id);
    void deleteMeal(int id);
    void addMeal(Meal meal);
    void updateMeal(Meal meal);
}
