package com.restaurant.entity;

public class Complement extends BaseEntity {
    private String nom;
    private double prix;
    private String image;
    private String type; // "FRITE" ou "BOISSON"
    
    public Complement() {}
    
    public Complement(String nom, double prix, String image, String type) {
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.type = type;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public double getPrix() {
        return prix;
    }
    
    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "Complement{" +
               "id=" + getId() +
               ", nom='" + nom + '\'' +
               ", prix=" + prix +
               ", type='" + type + '\'' +
               ", archived=" + isArchived() +
               '}';
    }
}