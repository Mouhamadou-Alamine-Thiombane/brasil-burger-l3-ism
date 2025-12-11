package com.restaurant.repository;

import com.restaurant.entity.Complement;
import java.util.List;

public interface ComplementRepository extends CrudRepository<Complement> {
    List<Complement> findByType(String type);
    List<Complement> findFrites();
    List<Complement> findBoissons();
}
