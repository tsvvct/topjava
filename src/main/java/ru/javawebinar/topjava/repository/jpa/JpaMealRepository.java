package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
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
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(getUserRef(userId));
            em.persist(meal);
            return meal;
        } else {
            if (get(meal.id(), userId) == null) {
                return null;
            } else {
                meal.setUser(getUserRef(userId));
                return em.merge(meal);
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createQuery("DELETE FROM Meal meal WHERE meal.id=:id AND meal.user.id=:userid")
                .setParameter("id", id)
                .setParameter("userid", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createQuery("select meal FROM Meal meal WHERE meal.user.id=:userid and meal.id=:id", Meal.class)
                .setParameter("userid", userId)
                .setParameter("id", id)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery("Select meal FROM Meal meal where meal.user.id=:userid ORDER BY meal.dateTime DESC", Meal.class)
                .setParameter("userid", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createQuery("select meal from Meal meal " +
                        "where meal.user.id=:userid and meal.dateTime>=:startDateTime AND meal.dateTime<:endDateTime " +
                        "ORDER BY meal.dateTime DESC", Meal.class)
                .setParameter("userid", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }

    private User getUserRef(int userId) {
        return em.getReference(User.class, userId);
    }
}