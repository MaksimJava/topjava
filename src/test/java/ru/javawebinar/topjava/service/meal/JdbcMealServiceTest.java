package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.JDBC;

/**
 * Created by Максим on 13.08.2017.
 */
@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
}
