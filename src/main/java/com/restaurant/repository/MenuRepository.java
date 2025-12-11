package com.restaurant.repository;


import com.restaurant.entity.Menu;
import java.util.List;

public interface MenuRepository extends CrudRepository<Menu> {
    List<Menu> findByBurgerId(int burgerId);
    List<Menu> findByNomContaining(String keyword);
    double calculatePrixMenu(int menuId);
}
