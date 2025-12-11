package com.restaurant.entity;

import java.util.List;

public class Zone extends BaseEntity {
    private String nom;
    private double prixLivraison;
    private List<String> quartiers;
    
    public Zone() {}
    
    public Zone(String nom, double prixLivraison, List<String> quartiers) {
        this.nom = nom;
        this.prixLivraison = prixLivraison;
        this.quartiers = quartiers;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public double getPrixLivraison() {
        return prixLivraison;
    }
    
    public void setPrixLivraison(double prixLivraison) {
        this.prixLivraison = prixLivraison;
    }
    
    public List<String> getQuartiers() {
        return quartiers;
    }
    
    public void setQuartiers(List<String> quartiers) {
        this.quartiers = quartiers;
    }
    
    public boolean contientQuartier(String quartier) {
        return quartiers.contains(quartier);
    }
    
    @Override
    public String toString() {
        return "Zone{" +
               "id=" + getId() +
               ", nom='" + nom + '\'' +
               ", prixLivraison=" + prixLivraison +
               ", quartiers=" + quartiers +
               ", archived=" + isArchived() +
               '}';
    }
}