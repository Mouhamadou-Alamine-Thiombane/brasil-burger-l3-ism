package com.restaurant.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Commande extends BaseEntity {
    private Client client;
    private List<CommandeItem> items;
    private EtatCommande etat;
    private String typeLivraison; // "SUR_PLACE", "A_EMPORTER", "LIVRAISON"
    private LocalDateTime dateCommande;
    private LocalDateTime dateLivraison;
    private Zone zone;
    private Livreur livreur;
    private String adresseLivraison;
    private double total;
    private boolean payee;
    
    public Commande() {
        this.items = new ArrayList<>();
        this.etat = EtatCommande.EN_ATTENTE;
        this.dateCommande = LocalDateTime.now();
        this.payee = false;
    }
    
    public Commande(Client client, String typeLivraison) {
        this();
        this.client = client;
        this.typeLivraison = typeLivraison;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public List<CommandeItem> getItems() {
        return items;
    }
    
    public void setItems(List<CommandeItem> items) {
        this.items = items;
        calculerTotal();
    }
    
    public void addItem(CommandeItem item) {
        this.items.add(item);
        calculerTotal();
    }
    
    public EtatCommande getEtat() {
        return etat;
    }
    
    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }
    
    public String getTypeLivraison() {
        return typeLivraison;
    }
    
    public void setTypeLivraison(String typeLivraison) {
        this.typeLivraison = typeLivraison;
    }
    
    public LocalDateTime getDateCommande() {
        return dateCommande;
    }
    
    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }
    
    public LocalDateTime getDateLivraison() {
        return dateLivraison;
    }
    
    public void setDateLivraison(LocalDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
    }
    
    public Zone getZone() {
        return zone;
    }
    
    public void setZone(Zone zone) {
        this.zone = zone;
    }
    
    public Livreur getLivreur() {
        return livreur;
    }
    
    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }
    
    public String getAdresseLivraison() {
        return adresseLivraison;
    }
    
    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public boolean isPayee() {
        return payee;
    }
    
    public void setPayee(boolean payee) {
        this.payee = payee;
    }
    
    private void calculerTotal() {
        total = items.stream()
                     .mapToDouble(CommandeItem::getSousTotal)
                     .sum();
        
        if (typeLivraison.equals("LIVRAISON") && zone != null) {
            total += zone.getPrixLivraison();
        }
    }
    
    public String getResume() {
        return "Commande #" + getId() + 
               " - Client: " + client.getNomComplet() + 
               " - Total: " + total + " FCFA" +
               " - État: " + etat;
    }
    
    @Override
    public String toString() {
        return "Commande{" +
               "id=" + getId() +
               ", client=" + client.getNomComplet() +
               ", nbItems=" + items.size() +
               ", etat=" + etat +
               ", typeLivraison='" + typeLivraison + '\'' +
               ", total=" + total +
               ", payee=" + payee +
               ", archived=" + isArchived() +
               '}';
    }
}