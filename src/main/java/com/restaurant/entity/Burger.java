package com.restaurant.entity;

public class Burger extends BaseEntity {
    private String nom;
    private double prix;
    private String image;
    private String description;
    
    public Burger() {}
    
    public Burger(String nom, double prix, String image, String description) {
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.description = description;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Burger{" +
               "id=" + getId() +
               ", nom='" + nom + '\'' +
               ", prix=" + prix +
               ", description='" + description + '\'' +
               ", archived=" + isArchived() +
               '}';
    }
}