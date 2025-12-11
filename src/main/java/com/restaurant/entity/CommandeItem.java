package com.restaurant.entity;

public class CommandeItem {
    private int id;
    private Commande commande;
    private Burger burger;
    private Menu menu;
    private Complement complement;
    private int quantite;
    private double prixUnitaire;
    
    public CommandeItem() {}
    
    public CommandeItem(Commande commande, Burger burger, int quantite, double prixUnitaire) {
        this.commande = commande;
        this.burger = burger;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }
    
    public CommandeItem(Commande commande, Menu menu, int quantite, double prixUnitaire) {
        this.commande = commande;
        this.menu = menu;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Commande getCommande() {
        return commande;
    }
    
    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    
    public Burger getBurger() {
        return burger;
    }
    
    public void setBurger(Burger burger) {
        this.burger = burger;
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    public Complement getComplement() {
        return complement;
    }
    
    public void setComplement(Complement complement) {
        this.complement = complement;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }
    
    public String getProduitNom() {
        if (burger != null) return burger.getNom();
        if (menu != null) return menu.getNom();
        if (complement != null) return complement.getNom();
        return "Inconnu";
    }
    
    @Override
    public String toString() {
        return "CommandeItem{" +
               "produit=" + getProduitNom() +
               ", quantite=" + quantite +
               ", prixUnitaire=" + prixUnitaire +
               ", sousTotal=" + getSousTotal() +
               '}';
    }
}