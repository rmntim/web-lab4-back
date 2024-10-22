package ru.rmntim.web.repositories;

import java.util.Collection;

public interface Repository<T, ID> {
    void create(T entity);

    T findOne(ID id);

    Collection<T> findAll();

    T update(T entity);

    void delete(ID id);
}
