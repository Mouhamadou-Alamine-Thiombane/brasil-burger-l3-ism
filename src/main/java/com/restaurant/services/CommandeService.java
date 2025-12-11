package com.restaurant.services;

import com.restaurant.entity.*;
import java.time.LocalDate;
import java.util.List;

public interface CommandeService {
    Commande creerCommande(Commande commande);
    Commande modifierCommande(Commande commande);
    boolean annulerCommande(int commandeId);
    boolean validerCommande(int commandeId);
    boolean terminerCommande(int commandeId);
    Commande getCommandeById(int id);
    List<Commande> getAllCommandes();
    List<Commande> getCommandesActives();
    List<Commande> getCommandesParClient(int clientId);
    List<Commande> getCommandesParEtat(EtatCommande etat);
    List<Commande> getCommandesDuJour();
    List<Commande> getCommandesEnCours();
    List<Commande> filtrerCommandesParBurger(int burgerId);
    List<Commande> filtrerCommandesParMenu(int menuId);
    List<Commande> filtrerCommandesParDate(LocalDate date);
    List<Commande> filtrerCommandesParDateRange(LocalDate start, LocalDate end);
    boolean changerEtatCommande(int commandeId, EtatCommande etat);
}
