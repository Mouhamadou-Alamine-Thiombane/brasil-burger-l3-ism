package com.restaurant.repository;



import com.restaurant.entity.Burger;
import java.util.List;

public interface BurgerRepository extends CrudRepository<Burger> {
    List<Burger> findByNom(String nom);
    List<Burger> findByPrixBetween(double min, double max);
}
