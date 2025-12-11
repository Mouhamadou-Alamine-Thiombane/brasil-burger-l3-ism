package com.restaurant.repository;

import com.restaurant.entity.Paiement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends CrudRepository<Paiement> {
    Optional<Paiement> findByCommandeId(int commandeId);
    List<Paiement> findByDate(LocalDate date);
    List<Paiement> findByMethode(String methode);
    double getTotalRecettesByDate(LocalDate date);
    boolean commandeEstPayee(int commandeId);
}
