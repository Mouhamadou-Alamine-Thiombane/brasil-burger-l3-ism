package com.restaurant;

import com.restaurant.entity.Client;
import com.restaurant.views.AuthView;
import com.restaurant.views.ClientView;
import com.restaurant.views.GestionnaireView;
import java.util.Scanner;

public class MenuPrincipal {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthView authView = new AuthView();
    
    public static void main(String[] args) {
        System.out.println("=== BRASIL BURGER - GESTION DES COMMANDES ===\n");
        
        while (true) {
            System.out.println("=== MENU PRINCIPAL ===");
            System.out.println("1. Espace Client");
            System.out.println("2. Espace Gestionnaire");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 :
                     espaceClient();
                     break;
                case 2 :
                     espaceGestionnaire();
                     break;
                case 3 :
                    {
                        System.out.println("Au revoir !");
                        System.exit(0);
                    }
                default : System.out.println("Choix invalide !");
            }
        }
    }
    
    private static void espaceClient() {
        Client client = authView.afficherMenuAuth();
        
        if (client != null) {
            ClientView clientView = new ClientView(client);
            clientView.afficherMenuPrincipal();
            authView.deconnecter();
        }
    }
    
    private static void espaceGestionnaire() {
        System.out.println("\n=== CONNEXION GESTIONNAIRE ===");
        System.out.print("Nom d'utilisateur : ");
        String username = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();
        
        // Authentification simple pour le gestionnaire
        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Connexion réussie !");
            GestionnaireView gestionnaireView = new GestionnaireView();
            gestionnaireView.afficherMenuPrincipal();
        } else {
            System.out.println("Identifiants incorrects !");
        }
    }
}
