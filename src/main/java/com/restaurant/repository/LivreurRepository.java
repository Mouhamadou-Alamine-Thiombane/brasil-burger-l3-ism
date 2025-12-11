package com.restaurant.repository;

import com.restaurant.entity.Livreur;
import java.util.List;

public interface LivreurRepository extends CrudRepository<Livreur> {
    List<Livreur> findByDisponible(boolean disponible);
    List<Livreur> findByZoneId(int zoneId);
    boolean affecterCommande(int livreurId, int commandeId);
    boolean libererLivreur(int livreurId);
}
