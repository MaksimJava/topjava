package ru.javawebinar.topjava.service.user;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

/**
 * Created by Максим on 13.08.2017.
 */
@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
}
