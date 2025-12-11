package com.restaurant.views;

import com.restaurant.entity.*;
import com.restaurant.services.Impl.*;
import com.restaurant.services.*;
import java.time.LocalDate;
import java.util.*;

public class GestionnaireView {
    private Scanner scanner = new Scanner(System.in);
    private BurgerService burgerService = new BurgerServiceImpl();
    private MenuService menuService = new MenuServiceImpl();
    private ComplementService complementService = new ComplementServiceImpl();
    private ClientService clientService = new ClientServiceImpl();
    private CommandeService commandeService = new CommandeServiceImpl();
    private PaiementService paiementService = new PaiementServiceImpl();
    private StatistiqueService statistiqueService = new StatistiqueServiceImpl();
    
    public void afficherMenuPrincipal() {
        while (true) {
            System.out.println("\n=== GESTIONNAIRE ===");
            System.out.println("1. Gestion des burgers");
            System.out.println("2. Gestion des menus");
            System.out.println("3. Gestion des compléments");
            System.out.println("4. Gestion des clients");
            System.out.println("5. Gestion des commandes");
            System.out.println("6. Tableau de bord");
            System.out.println("7. Se déconnecter");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 : 
                    afficherMenuBurgers();
                    break;
                case 2 : 
                    afficherMenuMenus();
                    break;
                case 3 : 
                    afficherMenuComplements();
                    break;
                case 4 : 
                    afficherMenuClients();
                    break;
                case 5 : 
                    afficherMenuCommandes();
                    break;
                case 6 : 
                    afficherTableauDeBord();
                    break;
                case 7 : {
                    System.out.println("Déconnexion...");
                    return;
                }
                default : 
                    System.out.println("Choix invalide !");
            }
        }
    }
    
    private void afficherMenuBurgers() {
        while (true) {
            System.out.println("\n=== GESTION DES BURGERS ===");
            System.out.println("1. Ajouter un burger");
            System.out.println("2. Modifier un burger");
            System.out.println("3. Archiver un burger");
            System.out.println("4. Lister tous les burgers");
            System.out.println("5. Lister les burgers actifs");
            System.out.println("6. Rechercher un burger");
            System.out.println("7. Retour");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 :
                    ajouterBurger();
                    break;
                case 2 :
                    modifierBurger();
                    break;
                case 3 :
                    archiverBurger();
                    break;
                case 4 :
                    listerTousBurgers();
                    break;
                case 5 :
                    listerBurgersActifs();
                    break;
                case 6 :
                    rechercherBurger();
                    break;
                case 7 :
                    return;
                default :
                    System.out.println("Choix invalide !");
            }
        }
    }
    
    private void ajouterBurger() {
        System.out.println("\n--- AJOUTER UN BURGER ---");
        
        Burger burger = new Burger();
        
        System.out.print("Nom : ");
        burger.setNom(scanner.nextLine());
        
        System.out.print("Prix : ");
        burger.setPrix(scanner.nextDouble());
        scanner.nextLine();
        
        System.out.print("Description : ");
        burger.setDescription(scanner.nextLine());
        
        System.out.print("Image (URL) : ");
        burger.setImage(scanner.nextLine());
        
        burgerService.ajouterBurger(burger);
        System.out.println("Burger ajouté avec succès !");
    }
    
    private void modifierBurger() {
        System.out.print("\nID du burger à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Burger burger = burgerService.getBurgerById(id);
        if (burger == null) {
            System.out.println("Burger non trouvé !");
            return;
        }
        
        System.out.println("Modification du burger : " + burger.getNom());
        System.out.print("Nouveau nom (actuel: " + burger.getNom() + ") : ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) burger.setNom(nom);
        
        System.out.print("Nouveau prix (actuel: " + burger.getPrix() + ") : ");
        String prixStr = scanner.nextLine();
        if (!prixStr.isEmpty()) burger.setPrix(Double.parseDouble(prixStr));
        
        System.out.print("Nouvelle description : ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) burger.setDescription(description);
        
        burgerService.modifierBurger(burger);
        System.out.println("Burger modifié avec succès !");
    }
    
    private void archiverBurger() {
        System.out.print("\nID du burger à archiver : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        if (burgerService.archiverBurger(id)) {
            System.out.println("Burger archivé avec succès !");
        } else {
            System.out.println("Erreur lors de l'archivage !");
        }
    }
    
    private void listerTousBurgers() {
        System.out.println("\n--- TOUS LES BURGERS ---");
        List<Burger> burgers = burgerService.getAllBurgers();
        
        burgers.forEach(b -> {
            System.out.printf("#%d - %s - %.0f FCFA - %s%n",
                b.getId(), b.getNom(), b.getPrix(),
                b.isArchived() ? "❌ Archivé" : "✅ Actif");
        });
    }
    
    private void listerBurgersActifs() {
        System.out.println("\n--- BURGERS ACTIFS ---");
        List<Burger> burgers = burgerService.getBurgersActifs();
        
        burgers.forEach(b -> {
            System.out.printf("#%d - %s - %.0f FCFA%n",
                b.getId(), b.getNom(), b.getPrix());
        });
    }
    
    private void rechercherBurger() {
        System.out.print("\nNom du burger à rechercher : ");
        String nom = scanner.nextLine();
        
        List<Burger> burgers = burgerService.rechercherBurgers(nom);
        
        if (burgers.isEmpty()) {
            System.out.println("Aucun burger trouvé.");
        } else {
            System.out.println("\nRésultats de la recherche :");
            burgers.forEach(b -> {
                System.out.printf("#%d - %s - %.0f FCFA%n",
                    b.getId(), b.getNom(), b.getPrix());
            });
        }
    }
    
    private void afficherMenuMenus() {
        while (true) {
            System.out.println("\n=== GESTION DES MENUS ===");
            System.out.println("1. Créer un menu");
            System.out.println("2. Modifier un menu");
            System.out.println("3. Archiver un menu");
            System.out.println("4. Lister tous les menus");
            System.out.println("5. Lister les menus actifs");
            System.out.println("6. Calculer prix d'un menu");
            System.out.println("7. Retour");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 : 
                    creerMenu();
                    break;
                case 2 :
                    modifierMenu();
                    break;
                case 3 :
                    archiverMenu();
                    break;
                case 4 :
                    listerTousMenus();
                    break;
                case 5 :
                    listerMenusActifs();
                    break;
                case 6 :
                    calculerPrixMenu();
                    break;
                case 7 :
                    return;
                default :
                    System.out.println("Choix invalide !");
            }
        }
    }
    
    private void creerMenu() {
        System.out.println("\n--- CRÉER UN MENU ---");
        
        // Lister les burgers disponibles
        System.out.println("\nBurgers disponibles :");
        List<Burger> burgers = burgerService.getBurgersActifs();
        burgers.forEach(b -> System.out.printf("#%d - %s%n", b.getId(), b.getNom()));
        
        System.out.print("\nID du burger pour le menu : ");
        int burgerId = scanner.nextInt();
        scanner.nextLine();
        
        Burger burger = burgerService.getBurgerById(burgerId);
        if (burger == null) {
            System.out.println("Burger non trouvé !");
            return;
        }
        
        // Choisir les compléments
        System.out.println("\nFrites disponibles :");
        List<Complement> frites = complementService.getFrites();
        frites.forEach(f -> System.out.printf("#%d - %s (%.0f FCFA)%n", f.getId(), f.getNom(), f.getPrix()));
        
        System.out.print("ID de la frite (0 pour aucun) : ");
        int friteId = scanner.nextInt();
        scanner.nextLine();
        
        Complement frite = null;
        if (friteId > 0) {
            frite = complementService.getComplementById(friteId);
        }
        
        System.out.println("\nBoissons disponibles :");
        List<Complement> boissons = complementService.getBoissons();
        boissons.forEach(b -> System.out.printf("#%d - %s (%.0f FCFA)%n", b.getId(), b.getNom(), b.getPrix()));
        
        System.out.print("ID de la boisson (0 pour aucun) : ");
        int boissonId = scanner.nextInt();
        scanner.nextLine();
        
        Complement boisson = null;
        if (boissonId > 0) {
            boisson = complementService.getComplementById(boissonId);
        }
        
        Menu menu = new Menu();
        System.out.print("Nom du menu : ");
        menu.setNom(scanner.nextLine());
        
        menu.setBurger(burger);
        menu.setFrite(frite);
        menu.setBoisson(boisson);
        
        System.out.print("Image (URL) : ");
        menu.setImage(scanner.nextLine());
        
        menuService.creerMenu(menu);
        System.out.println("Menu créé avec succès ! Prix : " + menu.getPrix() + " FCFA");
    }
    
    private void modifierMenu() {
        System.out.print("\nID du menu à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Menu menu = menuService.getMenuById(id);
        if (menu == null) {
            System.out.println("Menu non trouvé !");
            return;
        }
        
        System.out.println("Modification du menu : " + menu.getNom());
        System.out.print("Nouveau nom (actuel: " + menu.getNom() + ") : ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) menu.setNom(nom);
        
        menuService.modifierMenu(menu);
        System.out.println("Menu modifié avec succès !");
    }
    
    private void archiverMenu() {
        System.out.print("\nID du menu à archiver : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        if (menuService.archiverMenu(id)) {
            System.out.println("Menu archivé avec succès !");
        } else {
            System.out.println("Erreur lors de l'archivage !");
        }
    }
    
    private void listerTousMenus() {
        System.out.println("\n--- TOUS LES MENUS ---");
        List<Menu> menus = menuService.getAllMenus();
        
        menus.forEach(m -> {
            System.out.printf("#%d - %s - %.0f FCFA - %s%n",
                m.getId(), m.getNom(), m.getPrix(),
                m.isArchived() ? "❌ Archivé" : "✅ Actif");
            System.out.println("   Burger: " + m.getBurger().getNom());
            if (m.getFrite() != null) System.out.println("   Frites: " + m.getFrite().getNom());
            if (m.getBoisson() != null) System.out.println("   Boisson: " + m.getBoisson().getNom());
        });
    }
    
    private void listerMenusActifs() {
        System.out.println("\n--- MENUS ACTIFS ---");
        List<Menu> menus = menuService.getMenusActifs();
        
        menus.forEach(m -> {
            System.out.printf("#%d - %s - %.0f FCFA%n",
                m.getId(), m.getNom(), m.getPrix());
            System.out.println("   Burger: " + m.getBurger().getNom());
            if (m.getFrite() != null) System.out.println("   Frites: " + m.getFrite().getNom());
            if (m.getBoisson() != null) System.out.println("   Boisson: " + m.getBoisson().getNom());
        });
    }
    
    private void calculerPrixMenu() {
        System.out.print("\nID du menu pour calculer le prix : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        double prix = menuService.calculerPrixMenu(id);
        System.out.println("Prix calculé du menu : " + prix + " FCFA");
    }
    
    private void afficherMenuComplements() {
        while (true) {
            System.out.println("\n=== GESTION DES COMPLÉMENTS ===");
            System.out.println("1. Ajouter un complément");
            System.out.println("2. Modifier un complément");
            System.out.println("3. Archiver un complément");
            System.out.println("4. Lister tous les compléments");
            System.out.println("5. Lister les frites");
            System.out.println("6. Lister les boissons");
            System.out.println("7. Retour");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 : 
                    ajouterComplement();
                    break;
                case 2 : 
                    modifierComplement();
                    break;
                case 3 : 
                    archiverComplement();
                    break;
                case 4 : 
                    listerTousComplements();
                    break;
                case 5 : 
                    listerFrites();
                    break;
                case 6 : 
                    listerBoissons();
                    break;
                case 7 : 
                    return;
                default : 
                    System.out.println("Choix invalide !");
            }
        }
    }
    
    private void ajouterComplement() {
        System.out.println("\n--- AJOUTER UN COMPLÉMENT ---");
        
        Complement complement = new Complement();
        
        System.out.print("Nom : ");
        complement.setNom(scanner.nextLine());
        
        System.out.print("Prix : ");
        complement.setPrix(scanner.nextDouble());
        scanner.nextLine();
        
        System.out.println("Type :");
        System.out.println("1. Frite");
        System.out.println("2. Boisson");
        System.out.print("Choix : ");
        int typeChoix = scanner.nextInt();
        scanner.nextLine();
        
        complement.setType(typeChoix == 1 ? "FRITE" : "BOISSON");
        
        System.out.print("Image (URL) : ");
        complement.setImage(scanner.nextLine());
        
        complementService.ajouterComplement(complement);
        System.out.println("Complément ajouté avec succès !");
    }
    
    private void modifierComplement() {
        System.out.print("\nID du complément à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Complement complement = complementService.getComplementById(id);
        if (complement == null) {
            System.out.println("Complément non trouvé !");
            return;
        }
        
        System.out.println("Modification du complément : " + complement.getNom());
        System.out.print("Nouveau nom (actuel: " + complement.getNom() + ") : ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) complement.setNom(nom);
        
        System.out.print("Nouveau prix (actuel: " + complement.getPrix() + ") : ");
        String prixStr = scanner.nextLine();
        if (!prixStr.isEmpty()) complement.setPrix(Double.parseDouble(prixStr));
        
        complementService.modifierComplement(complement);
        System.out.println("Complément modifié avec succès !");
    }
    
    private void archiverComplement() {
        System.out.print("\nID du complément à archiver : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        if (complementService.archiverComplement(id)) {
            System.out.println("Complément archivé avec succès !");
        } else {
            System.out.println("Erreur lors de l'archivage !");
        }
    }
    
    private void listerTousComplements() {
        System.out.println("\n--- TOUS LES COMPLÉMENTS ---");
        List<Complement> complements = complementService.getAllComplements();
        
        complements.forEach(c -> {
            System.out.printf("#%d - %s (%s) - %.0f FCFA - %s%n",
                c.getId(), c.getNom(), c.getType(), c.getPrix(),
                c.isArchived() ? "❌ Archivé" : "✅ Actif");
        });
    }
    
    private void listerFrites() {
        System.out.println("\n--- FRITES ---");
        List<Complement> frites = complementService.getFrites();
        
        frites.forEach(f -> {
            System.out.printf("#%d - %s - %.0f FCFA%n",
                f.getId(), f.getNom(), f.getPrix());
        });
    }
    
    private void listerBoissons() {
        System.out.println("\n--- BOISSONS ---");
        List<Complement> boissons = complementService.getBoissons();
        
        boissons.forEach(b -> {
            System.out.printf("#%d - %s - %.0f FCFA%n",
                b.getId(), b.getNom(), b.getPrix());
        });
    }
    
    private void afficherMenuClients() {
        while (true) {
            System.out.println("\n=== GESTION DES CLIENTS ===");
            System.out.println("1. Lister tous les clients");
            System.out.println("2. Rechercher un client");
            System.out.println("3. Archiver un client");
            System.out.println("4. Retour");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 : 
                    listerTousClients();
                    break;
                case 2 : 
                    rechercherClient();
                    break;
                case 3 : 
                    archiverClient();
                    break;
                case 4 : 
                    return;
                default : 
                    System.out.println("Choix invalide !");
            }
        }
    }
    
    private void listerTousClients() {
        System.out.println("\n--- TOUS LES CLIENTS ---");
        List<Client> clients = clientService.getAllClients();
        
        clients.forEach(c -> {
            System.out.printf("#%d - %s - %s - %s - %s%n",
                c.getId(), c.getNomComplet(), c.getTelephone(), c.getEmail(),
                c.isArchived() ? "❌ Archivé" : "✅ Actif");
        });
    }
    
    private void rechercherClient() {
        System.out.print("\nNom du client à rechercher : ");
        String nom = scanner.nextLine();
        
        List<Client> clients = clientService.rechercherClients(nom);
        
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
        } else {
            System.out.println("\nRésultats de la recherche :");
            clients.forEach(c -> {
                System.out.printf("#%d - %s - %s - %s%n",
                    c.getId(), c.getNomComplet(), c.getTelephone(), c.getEmail());
            });
        }
    }
    
    private void archiverClient() {
        System.out.print("\nID du client à archiver : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        if (clientService.archiverClient(id)) {
            System.out.println("Client archivé avec succès !");
        } else {
            System.out.println("Erreur lors de l'archivage !");
        }
    }
    
    private void afficherMenuCommandes() {
        while (true) {
            System.out.println("\n=== GESTION DES COMMANDES ===");
            System.out.println("1. Lister toutes les commandes");
            System.out.println("2. Lister les commandes du jour");
            System.out.println("3. Lister les commandes en cours");
            System.out.println("4. Valider une commande");
            System.out.println("5. Terminer une commande");
            System.out.println("6. Annuler une commande");
            System.out.println("7. Filtrer par burger");
            System.out.println("8. Filtrer par menu");
            System.out.println("9. Filtrer par date");
            System.out.println("10. Filtrer par état");
            System.out.println("11. Retour");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 :
                    listerToutesCommandes();
                    break;
                case 2 :
                    listerCommandesDuJour();
                    break;
                case 3 :
                    listerCommandesEnCours();
                    break;
                case 4 :
                    validerCommande();
                    break;
                case 5 :
                    terminerCommande();
                    break;
                case 6 :
                    annulerCommande();
                    break;
                case 7 :
                    filtrerCommandesParBurger();
                    break;
                case 8 :
                    filtrerCommandesParMenu();
                    break;
                case 9 :
                    filtrerCommandesParDate();
                    break;
                case 10 :
                    filtrerCommandesParEtat();
                    break;
                case 11 :
                    return;
                default :
                    System.out.println("Choix invalide !");
            }
        }
    }
    
    private void listerToutesCommandes() {
        System.out.println("\n--- TOUTES LES COMMANDES ---");
        List<Commande> commandes = commandeService.getAllCommandes();
        
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée.");
            return;
        }
        
        for (Commande commande : commandes) {
            System.out.printf("#%d - %s - Client: %s - Total: %.0f FCFA - État: %s - Payée: %s%n",
                commande.getId(),
                commande.getDateCommande().toLocalDate(),
                commande.getClient().getNomComplet(),
                commande.getTotal(),
                commande.getEtat(),
                commande.isPayee() ? "✅" : "❌");
        }
    }
    
    private void listerCommandesDuJour() {
        System.out.println("\n--- COMMANDES DU JOUR ---");
        List<Commande> commandes = commandeService.getCommandesDuJour();
        
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande aujourd'hui.");
            return;
        }
        
        for (Commande commande : commandes) {
            System.out.printf("#%d - %s - Client: %s - Total: %.0f FCFA - État: %s%n",
                commande.getId(),
                commande.getDateCommande().toLocalTime(),
                commande.getClient().getNomComplet(),
                commande.getTotal(),
                commande.getEtat());
        }
    }
    
    private void listerCommandesEnCours() {
        System.out.println("\n--- COMMANDES EN COURS ---");
        List<Commande> commandes = commandeService.getCommandesEnCours();
        
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande en cours.");
            return;
        }
        
        for (Commande commande : commandes) {
            System.out.printf("#%d - %s - Client: %s - Total: %.0f FCFA - État: %s%n",
                commande.getId(),
                commande.getDateCommande().toLocalTime(),
                commande.getClient().getNomComplet(),
                commande.getTotal(),
                commande.getEtat());
        }
    }
    
    private void validerCommande() {
        System.out.print("\nID de la commande à valider : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        if (commandeService.validerCommande(id)) {
            System.out.println("Commande validée avec succès !");
        } else {
            System.out.println("Erreur lors de la validation !");
        }
    }
    
    private void terminerCommande() {
        System.out.print("\nID de la commande à terminer : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        if (commandeService.terminerCommande(id)) {
            System.out.println("Commande terminée avec succès !");
        } else {
            System.out.println("Erreur !");
        }
    }
    
    private void annulerCommande() {
        System.out.print("\nID de la commande à annuler : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Confirmer l'annulation ? (oui/non) : ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("oui")) {
            if (commandeService.annulerCommande(id)) {
                System.out.println("Commande annulée avec succès !");
            } else {
                System.out.println("Erreur lors de l'annulation !");
            }
        }
    }
    
    private void filtrerCommandesParBurger() {
        System.out.print("\nID du burger : ");
        int burgerId = scanner.nextInt();
        scanner.nextLine();
        
        List<Commande> commandes = commandeService.filtrerCommandesParBurger(burgerId);
        
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée pour ce burger.");
            return;
        }
        
        System.out.println("\nCommandes contenant ce burger :");
        for (Commande commande : commandes) {
            System.out.printf("#%d - %s - Client: %s - Total: %.0f FCFA - État: %s%n",
                commande.getId(),
                commande.getDateCommande().toLocalDate(),
                commande.getClient().getNomComplet(),
                commande.getTotal(),
                commande.getEtat());
        }
    }
    
    private void filtrerCommandesParMenu() {
        System.out.print("\nID du menu : ");
        int menuId = scanner.nextInt();
        scanner.nextLine();
        
        List<Commande> commandes = commandeService.filtrerCommandesParMenu(menuId);
        
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée pour ce menu.");
            return;
        }
        
        System.out.println("\nCommandes contenant ce menu :");
        for (Commande commande : commandes) {
            System.out.printf("#%d - %s - Client: %s - Total: %.0f FCFA - État: %s%n",
                commande.getId(),
                commande.getDateCommande().toLocalDate(),
                commande.getClient().getNomComplet(),
                commande.getTotal(),
                commande.getEtat());
        }
    }
    
    private void filtrerCommandesParDate() {
        System.out.print("\nDate (AAAA-MM-JJ) : ");
        String dateStr = scanner.nextLine();
        
        try {
            LocalDate date = LocalDate.parse(dateStr);
            List<Commande> commandes = commandeService.filtrerCommandesParDate(date);
            
            if (commandes.isEmpty()) {
                System.out.println("Aucune commande trouvée pour cette date.");
                return;
            }
            
            System.out.println("\nCommandes du " + date + " :");
            for (Commande commande : commandes) {
                System.out.printf("#%d - %s - Client: %s - Total: %.0f FCFA - État: %s%n",
                    commande.getId(),
                    commande.getDateCommande().toLocalTime(),
                    commande.getClient().getNomComplet(),
                    commande.getTotal(),
                    commande.getEtat());
            }
        } catch (Exception e) {
            System.out.println("Format de date invalide !");
        }
    }
    
    private void filtrerCommandesParEtat() {
        System.out.println("\nÉtats disponibles :");
        for (EtatCommande etat : EtatCommande.values()) {
            System.out.println("- " + etat);
        }
        
        System.out.print("\nÉtat : ");
        String etatStr = scanner.nextLine().toUpperCase();
        
        try {
            EtatCommande etat = EtatCommande.valueOf(etatStr);
            List<Commande> commandes = commandeService.getCommandesParEtat(etat);
            
            if (commandes.isEmpty()) {
                System.out.println("Aucune commande trouvée avec cet état.");
                return;
            }
            
            System.out.println("\nCommandes avec l'état " + etat + " :");
            for (Commande commande : commandes) {
                System.out.printf("#%d - %s - Client: %s - Total: %.0f FCFA%n",
                    commande.getId(),
                    commande.getDateCommande().toLocalDate(),
                    commande.getClient().getNomComplet(),
                    commande.getTotal());
            }
        } catch (Exception e) {
            System.out.println("État invalide !");
        }
    }
    
    private void afficherTableauDeBord() {
        System.out.println("\n=== TABLEAU DE BORD ===");
        
        LocalDate aujourdhui = LocalDate.now();
        Map<String, Object> stats = statistiqueService.getTableauDeBord(aujourdhui);
        
        System.out.println("\n--- STATISTIQUES DU JOUR (" + aujourdhui + ") ---");
        System.out.println("Commandes en cours : " + stats.get("commandes_en_cours"));
        System.out.println("Commandes validées : " + stats.get("commandes_validees"));
        System.out.println("Recettes journalières : " + stats.get("recettes_journalieres") + " FCFA");
        System.out.println("Commandes annulées : " + stats.get("commandes_annulees"));
        System.out.println("Nouveaux clients : " + stats.get("nouveaux_clients"));
        
        System.out.println("\n--- BURGERS LES PLUS VENDUS ---");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> burgersVendus = (List<Map<String, Object>>) stats.get("burgers_plus_vendus");
        if (burgersVendus.isEmpty()) {
            System.out.println("Aucune vente aujourd'hui.");
        } else {
            for (Map<String, Object> burger : burgersVendus) {
                System.out.printf("- %s : %d ventes%n", burger.get("nom"), burger.get("total_vendu"));
            }
        }
        
        System.out.println("\n--- VENTES PAR TYPE DE PRODUIT ---");
        @SuppressWarnings("unchecked")
        Map<String, Integer> ventesParType = (Map<String, Integer>) stats.get("ventes_par_type");
        ventesParType.forEach((type, quantite) -> {
            System.out.printf("- %s : %d unités%n", type, quantite);
        });
        
        System.out.println("\n--- CHIFFRE D'AFFAIRE PAR MOIS ---");
        Map<String, Double> caParMois = statistiqueService.getChiffreAffaireParMois(aujourdhui.getYear());
        if (caParMois.isEmpty()) {
            System.out.println("Aucun chiffre d'affaire cette année.");
        } else {
            caParMois.forEach((mois, montant) -> {
                System.out.printf("- %s : %.0f FCFA%n", mois, montant);
            });
        }
    }
}