package com.restaurant.entity;

import java.time.LocalDateTime;

public class Paiement extends BaseEntity {
    private Commande commande;
    private double montant;
    private String methode; // "WAVE", "OM", "CARTE", "ESPECES"
    private LocalDateTime datePaiement;
    private String reference;
    
    public Paiement() {
        this.datePaiement = LocalDateTime.now();
    }
    
    public Paiement(Commande commande, double montant, String methode, String reference) {
        this();
        this.commande = commande;
        this.montant = montant;
        this.methode = methode;
        this.reference = reference;
    }
    
    public Commande getCommande() {
        return commande;
    }
    
    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    
    public double getMontant() {
        return montant;
    }
    
    public void setMontant(double montant) {
        this.montant = montant;
    }
    
    public String getMethode() {
        return methode;
    }
    
    public void setMethode(String methode) {
        this.methode = methode;
    }
    
    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }
    
    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    @Override
    public String toString() {
        return "Paiement{" +
               "id=" + getId() +
               ", commandeId=" + (commande != null ? commande.getId() : "null") +
               ", montant=" + montant +
               ", methode='" + methode + '\'' +
               ", datePaiement=" + datePaiement +
               ", reference='" + reference + '\'' +
               ", archived=" + isArchived() +
               '}';
    }
}