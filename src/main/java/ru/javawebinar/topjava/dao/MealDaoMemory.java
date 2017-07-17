package ru.javawebinar.topjava.dao;

/**
 * Created by Максим on 17.07.2017.
 */
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static ru.javawebinar.topjava.dao.DataMock.mealList;

public class MealDaoMemory implements MealDao {
    private static final Object synObj = new Object();

    @Override
    public List<Meal> getAllMeals() {
        return mealList;
    }

    @Override
    public Meal getMealById(int id) {
        synchronized (synObj) {
            for (Meal meal : mealList) {
                if (meal.getId() == id) {
                    return meal;
                }
            }
            return null;
        }
    }

    @Override
    public void deleteMeal(int id) {
        synchronized (synObj) {
            for (Meal meal : mealList) {
                if (meal.getId() == id) {
                    mealList.remove(meal);
                    break;
                }
            }
        }
    }

    @Override
    public void addMeal(Meal meal) {
        synchronized (synObj) {
            mealList.add(meal);
        }
    }


    @Override
    public void updateMeal(Meal meal) {
        synchronized (synObj) {
            for (Meal meals : mealList) {
                if (meals.getId() == meal.getId()) {
                    mealList.remove(meals);
                    break;
                }
            }
            mealList.add(meal);
        }
    }
}
