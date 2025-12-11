package com.restaurant.repository;

import com.restaurant.entity.Commande;
import com.restaurant.entity.EtatCommande;
import java.time.LocalDate;
import java.util.List;

public interface CommandeRepository extends CrudRepository<Commande> {
    List<Commande> findByClientId(int clientId);
    List<Commande> findByEtat(EtatCommande etat);
    List<Commande> findByDate(LocalDate date);
    List<Commande> findByClientAndEtat(int clientId, EtatCommande etat);
    boolean updateEtat(int commandeId, EtatCommande etat);
    List<Commande> findCommandesEnCours();
    List<Commande> findCommandesDuJour();
    List<Commande> filterByBurger(int burgerId);
    List<Commande> filterByMenu(int menuId);
    List<Commande> filterByDateRange(LocalDate start, LocalDate end);
}
