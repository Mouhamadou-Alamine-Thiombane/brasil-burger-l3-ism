package com.restaurant.entity;

public class Client extends BaseEntity {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String motDePasse;
    private String adresse;
    
    public Client() {}
    
    public Client(String nom, String prenom, String telephone, String email, String motDePasse, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.motDePasse = motDePasse;
        this.adresse = adresse;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    @Override
    public String toString() {
        return "Client{" +
               "id=" + getId() +
               ", nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               ", telephone='" + telephone + '\'' +
               ", email='" + email + '\'' +
               ", adresse='" + adresse + '\'' +
               ", archived=" + isArchived() +
               '}';
    }
}