package com.restaurant.repository;

import com.restaurant.entity.Zone;
import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends CrudRepository<Zone> {
    Optional<Zone> findByNom(String nom);
    List<Zone> findByQuartier(String quartier);
    double getPrixLivraisonByZone(int zoneId);
}
