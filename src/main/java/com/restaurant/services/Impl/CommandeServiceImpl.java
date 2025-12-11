package com.restaurant.services.Impl;

import com.restaurant.entity.*;
import com.restaurant.repository.CommandeRepository;
import com.restaurant.repository.Impl.CommandeRepositoryImpl;
import com.restaurant.services.CommandeService;
import java.time.LocalDate;
import java.util.List;

public class CommandeServiceImpl implements CommandeService {
    
    private CommandeRepository commandeRepository = new CommandeRepositoryImpl();
    
    @Override
    public Commande creerCommande(Commande commande) {
        // Validation
        if (commande.getClient() == null) {
            throw new IllegalArgumentException("Une commande doit avoir un client");
        }
        
        if (commande.getItems() == null || commande.getItems().isEmpty()) {
            throw new IllegalArgumentException("Une commande doit contenir au moins un item");
        }
        
        // Calculer le total
        double total = commande.getItems().stream()
                               .mapToDouble(item -> item.getQuantite() * item.getPrixUnitaire())
                               .sum();
        commande.setTotal(total);
        
        // Définir l'état initial
        commande.setEtat(EtatCommande.EN_ATTENTE);
        
        return commandeRepository.save(commande);
    }
    
    @Override
    public Commande modifierCommande(Commande commande) {
        return commandeRepository.update(commande);
    }
    
    @Override
    public boolean annulerCommande(int commandeId) {
        return commandeRepository.updateEtat(commandeId, EtatCommande.ANNULEE);
    }
    
    @Override
    public boolean validerCommande(int commandeId) {
        return commandeRepository.updateEtat(commandeId, EtatCommande.VALIDEE);
    }
    
    @Override
    public boolean terminerCommande(int commandeId) {
        return commandeRepository.updateEtat(commandeId, EtatCommande.TERMINEE);
    }
    
    @Override
    public Commande getCommandeById(int id) {
        return commandeRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }
    
    @Override
    public List<Commande> getCommandesActives() {
        return commandeRepository.findAllActive();
    }
    
    @Override
    public List<Commande> getCommandesParClient(int clientId) {
        return commandeRepository.findByClientId(clientId);
    }
    
    @Override
    public List<Commande> getCommandesParEtat(EtatCommande etat) {
        return commandeRepository.findByEtat(etat);
    }
    
    @Override
    public List<Commande> getCommandesDuJour() {
        return commandeRepository.findCommandesDuJour();
    }
    
    @Override
    public List<Commande> getCommandesEnCours() {
        return commandeRepository.findCommandesEnCours();
    }
    
    @Override
    public List<Commande> filtrerCommandesParBurger(int burgerId) {
        return commandeRepository.filterByBurger(burgerId);
    }
    
    @Override
    public List<Commande> filtrerCommandesParMenu(int menuId) {
        return commandeRepository.filterByMenu(menuId);
    }
    
    @Override
    public List<Commande> filtrerCommandesParDate(LocalDate date) {
        return commandeRepository.findByDate(date);
    }
    
    @Override
    public List<Commande> filtrerCommandesParDateRange(LocalDate start, LocalDate end) {
        return commandeRepository.filterByDateRange(start, end);
    }
    
    @Override
    public boolean changerEtatCommande(int commandeId, EtatCommande etat) {
        return commandeRepository.updateEtat(commandeId, etat);
    }
}
