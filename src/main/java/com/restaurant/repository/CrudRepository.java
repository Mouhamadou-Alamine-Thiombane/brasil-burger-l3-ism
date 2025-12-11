package com.restaurant.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    T save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    List<T> findAllActive();
    T update(T entity);
    boolean delete(int id);
    boolean archive(int id);
}
