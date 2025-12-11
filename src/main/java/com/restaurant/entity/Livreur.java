package com.restaurant.entity;

public class Livreur extends BaseEntity {
    private String nom;
    private String prenom;
    private String telephone;
    private String vehicule;
    private boolean disponible;
    private Zone zone;
    
    public Livreur() {}
    
    public Livreur(String nom, String prenom, String telephone, String vehicule, Zone zone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.vehicule = vehicule;
        this.disponible = true;
        this.zone = zone;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public Zone getZone() {
        return zone;
    }
    
    public void setZone(Zone zone) {
        this.zone = zone;
    }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    @Override
    public String toString() {
        return "Livreur{" +
               "id=" + getId() +
               ", nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               ", telephone='" + telephone + '\'' +
               ", vehicule='" + vehicule + '\'' +
               ", disponible=" + disponible +
               ", zone=" + (zone != null ? zone.getNom() : "null") +
               ", archived=" + isArchived() +
               '}';
    }
}