package com.restaurant.services;

import com.restaurant.entity.Paiement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaiementService {
    Paiement effectuerPaiement(Paiement paiement);
    Optional<Paiement> getPaiementById(int id);
    Optional<Paiement> getPaiementParCommande(int commandeId);
    List<Paiement> getAllPaiements();
    List<Paiement> getPaiementsParDate(LocalDate date);
    List<Paiement> getPaiementsParMethode(String methode);
    double getTotalRecettes(LocalDate date);
    boolean commandeEstPayee(int commandeId);
}
