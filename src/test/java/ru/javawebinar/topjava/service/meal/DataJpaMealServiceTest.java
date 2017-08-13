package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

/**
 * Created by Максим on 13.08.2017.
 */
@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
}
