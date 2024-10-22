package ru.rmntim.web.repositories;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import ru.rmntim.web.models.User;

import java.util.Collection;

@RequestScoped
@NoArgsConstructor
public class UserRepository implements Repository<User, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(User entity) {
        entityManager.persist(entity);
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
        return entityManager.merge(entity);
    }

    @Override
    public void delete(Long aLong) {
        var user = entityManager.find(User.class, aLong);
        entityManager.remove(user);
    }
}
