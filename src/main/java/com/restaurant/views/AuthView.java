package com.restaurant.views;

import com.restaurant.entity.Client;
import com.restaurant.services.Impl.ClientServiceImpl;
import com.restaurant.services.ClientService;
import java.util.Scanner;

public class AuthView {
    private Scanner scanner = new Scanner(System.in);
    private ClientService clientService = new ClientServiceImpl();
    private Client clientConnecte = null;
    
    public Client afficherMenuAuth() {
        while (clientConnecte == null) {
            System.out.println("\n=== AUTHENTIFICATION ===");
            System.out.println("1. Se connecter");
            System.out.println("2. S'inscrire");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
            
            switch (choix) {
                case 1 :
                    seConnecter();
                    break;
                case 2 : 
                    sInscrire();
                    break;
                case 3 :
                    System.exit(0);
                    break;
                default :
                    System.out.println("Choix invalide !");
            }
        }
        return clientConnecte;
    }
    
    private void seConnecter() {
        System.out.println("\n--- CONNEXION ---");
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();
        
        if (clientService.authentifierClient(email, motDePasse)) {
            clientConnecte = clientService.getClientByEmail(email).orElse(null);
            System.out.println("Connexion réussie ! Bienvenue " + clientConnecte.getNomComplet());
        } else {
            System.out.println("Email ou mot de passe incorrect !");
        }
    }
    
    private void sInscrire() {
        System.out.println("\n--- INSCRIPTION ---");
        
        Client client = new Client();
        
        System.out.print("Nom : ");
        client.setNom(scanner.nextLine());
        
        System.out.print("Prénom : ");
        client.setPrenom(scanner.nextLine());
        
        System.out.print("Téléphone : ");
        client.setTelephone(scanner.nextLine());
        
        System.out.print("Email : ");
        client.setEmail(scanner.nextLine());
        
        System.out.print("Mot de passe : ");
        client.setMotDePasse(scanner.nextLine());
        
        System.out.print("Adresse : ");
        client.setAdresse(scanner.nextLine());
        
        clientService.inscrireClient(client);
        System.out.println("Inscription réussie ! Vous pouvez maintenant vous connecter.");
    }
    
    public void deconnecter() {
        clientConnecte = null;
        System.out.println("Déconnexion réussie.");
    }
    
    public boolean estConnecte() {
        return clientConnecte != null;
    }
    
    public Client getClientConnecte() {
        return clientConnecte;
    }
}
