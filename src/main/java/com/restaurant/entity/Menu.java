package com.restaurant.entity;

public class Menu extends BaseEntity {
    private String nom;
    private Burger burger;
    private Complement frite;
    private Complement boisson;
    private String image;
    private double prix; // Calculé automatiquement
    
    public Menu() {}
    
    public Menu(String nom, Burger burger, Complement frite, Complement boisson, String image) {
        this.nom = nom;
        this.burger = burger;
        this.frite = frite;
        this.boisson = boisson;
        this.image = image;
        this.prix = calculatePrix();
    }
    
    private double calculatePrix() {
        double total = burger.getPrix();
        if (frite != null) total += frite.getPrix();
        if (boisson != null) total += boisson.getPrix();
        return total;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Burger getBurger() {
        return burger;
    }
    
    public void setBurger(Burger burger) {
        this.burger = burger;
        this.prix = calculatePrix();
    }
    
    public Complement getFrite() {
        return frite;
    }
    
    public void setFrite(Complement frite) {
        this.frite = frite;
        this.prix = calculatePrix();
    }
    
    public Complement getBoisson() {
        return boisson;
    }
    
    public void setBoisson(Complement boisson) {
        this.boisson = boisson;
        this.prix = calculatePrix();
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public double getPrix() {
        return prix;
    }
    
    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    @Override
    public String toString() {
        return "Menu{" +
               "id=" + getId() +
               ", nom='" + nom + '\'' +
               ", burger=" + (burger != null ? burger.getNom() : "null") +
               ", frite=" + (frite != null ? frite.getNom() : "null") +
               ", boisson=" + (boisson != null ? boisson.getNom() : "null") +
               ", prix=" + prix +
               ", archived=" + isArchived() +
               '}';
    }
}