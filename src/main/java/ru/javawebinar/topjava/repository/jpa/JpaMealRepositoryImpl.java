package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(em.getReference(User.class, userId));
            em.persist(meal);
            return meal;
        } else {
            if (meal.getUser().getId() == userId) {
                return em.merge(meal);
            } else {
                return null;
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal reference = em.getReference(Meal.class, id);
        if (reference.getUser().getId() == userId) {
            em.remove(reference);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal.getUser().getId() == userId) {
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em
                .createNamedQuery(Meal.GET_ALL, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em
                .createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("userId", userId)
                .getResultList();
    }
}