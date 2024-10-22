package ru.rmntim.web.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.rmntim.web.models.Point;

import java.util.Collection;

public class PointsRepository implements Repository<Point, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Point entity) {
        var tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(entity);
        tx.commit();
    }

    @Override
    public Point findOne(Long aLong) {
        return entityManager.find(Point.class, aLong);
    }

    @Override
    public Collection<Point> findAll() {
        return entityManager.createQuery("select p from Point p", Point.class).getResultList();
    }

    @Override
    public Point update(Point entity) {
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
        var point = entityManager.find(Point.class, aLong);
        entityManager.remove(point);
        tx.commit();
    }
}
