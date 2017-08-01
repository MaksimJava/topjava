package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.MealTestData.USER_MEALS;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {
    private List<Meal> testUserMeals;

    @Autowired
    protected MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        testUserMeals = new ArrayList<Meal>(USER_MEALS);
        dbPopulator.execute();
    }

    @Test
    public void testGet() {
        Meal testMeal = testUserMeals.get(0);
        Meal meal = service.get(testMeal.getId(), USER_ID);
        MATCHER.assertEquals(testMeal, meal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetAnotherMeal() {
        service.get(testUserMeals.get(0).getId(), ADMIN_ID);
    }

    @Test
    public void testDelete() {
        service.delete(testUserMeals.get(0).getId(), USER_ID);
        MATCHER.assertCollectionEquals(testUserMeals.subList(1, testUserMeals.size()), service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAnotherMeal() {
        service.delete(testUserMeals.get(0).getId(), ADMIN_ID);
    }

    @Test
    public void testGetBetweenDates() {
        LocalDate start = LocalDate.of(2016, 12, 25);
        LocalDate end = LocalDate.of(2016, 12, 25);

        Collection<Meal> meals = service.getBetweenDates(start, end, USER_ID);
        Collection<Meal> testMeals = testUserMeals
                .stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDate(), start, end))
                .collect(Collectors.toList());
        MATCHER.assertCollectionEquals(testMeals, meals);
    }

    @Test
    public void testGetBetweenDateTimes() {
        LocalDateTime start = LocalDateTime.of(2016, 12, 25, 9, 0, 0);
        LocalDateTime end = LocalDateTime.of(2016, 12, 25, 13, 0, 0);

        Collection<Meal> meals = service.getBetweenDateTimes(start, end, USER_ID);
        Collection<Meal> testMeals = testUserMeals
                .stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime(), start, end))
                .collect(Collectors.toList());
        MATCHER.assertCollectionEquals(testMeals, meals);
    }

    @Test
    public void testGetAll() {
        Collection<Meal> meals = service.getAll(USER_ID);
        MATCHER.assertCollectionEquals(testUserMeals, meals);
    }

    @Test
    public void testUpdate() {
        Meal testMeal = testUserMeals.get(0);
        Meal meal = service.update(testMeal, USER_ID);
        MATCHER.assertEquals(testMeal, meal);
    }

    @Test
    public void testSave() {
        Meal testMeal = new Meal(1, LocalDateTime.of(2016, 12, 25, 9, 0, 0), "Test description", 600);
        Meal meal = service.save(testMeal, USER_ID);
        MATCHER.assertEquals(testMeal, meal);
    }

}