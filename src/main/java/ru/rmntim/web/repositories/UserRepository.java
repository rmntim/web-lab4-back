package ru.rmntim.web.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.rmntim.web.models.User;

import java.util.Collection;

public class UserRepository implements Repository<User, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(User entity) {
        var tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(entity);
        tx.commit();
    }

    @Override
    public User findOne(Long aLong) {
        return entityManager.find(User.class, aLong);
    }

    @Override
    public Collection<User> findAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User update(User entity) {
        var tx = entityManager.getTransaction();
        tx.begin();
        var result = entityManager.merge(entity);
        tx.commit();
        return result;
    }

    @Override
    public void delete(Long aLong) {
        var tx = entityManager.getTransaction();
        tx.begin();
        var user = entityManager.find(User.class, aLong);
        entityManager.remove(user);
        tx.commit();
    }
}
