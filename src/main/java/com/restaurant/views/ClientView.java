package com.restaurant.views;

import com.restaurant.entity.*;
import com.restaurant.services.Impl.*;
import com.restaurant.services.*;
import java.util.*;

public class ClientView {
    private Scanner scanner = new Scanner(System.in);
    private BurgerService burgerService = new BurgerServiceImpl();
    private MenuService menuService = new MenuServiceImpl();
    private ComplementService complementService = new ComplementServiceImpl();
    private CommandeService commandeService = new CommandeServiceImpl();
    private PaiementService paiementService = new PaiementServiceImpl();
    private Client client;
    
    public ClientView(Client client) {
        this.client = client;
    }
    
    public void afficherMenuPrincipal() {
        while (true) {
            System.out.println("\n=== CLIENT - " + client.getNomComplet() + " ===");
            System.out.println("1. Voir le catalogue");
            System.out.println("2. Voir les burgers");
            System.out.println("3. Voir les menus");
            System.out.println("4. Commander");
            System.out.println("5. Voir mes commandes");
            System.out.println("6. Suivre une commande");
            System.out.println("7. Payer une commande");
            System.out.println("8. Se déconnecter");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 : 
                    afficherCatalogue();
                    break;
                case 2 : 
                    afficherBurgers();
                    break;
                case 3 : 
                    afficherMenus();
                    break;
                case 4 : 
                    creerCommande();
                    break;
                case 5 : 
                    afficherMesCommandes();
                    break;
                case 6 : 
                    suivreCommande();
                    break;
                case 7 : 
                    payerCommande();
                    break;
                case 8 : {
                    System.out.println("Déconnexion...");
                    return;
                }
                default :
                    System.out.println("Choix invalide !");
            }
        }
    }
    
    private void afficherCatalogue() {
        System.out.println("\n=== CATALOGUE ===");
        
        System.out.println("\n--- BURGERS ---");
        List<Burger> burgers = burgerService.getBurgersActifs();
        burgers.forEach(b -> System.out.printf("- %s : %.0f FCFA%n", b.getNom(), b.getPrix()));
        
        System.out.println("\n--- MENUS ---");
        List<Menu> menus = menuService.getMenusActifs();
        menus.forEach(m -> System.out.printf("- %s : %.0f FCFA%n", m.getNom(), m.getPrix()));
        
        System.out.println("\n--- COMPLEMENTS ---");
        List<Complement> complements = complementService.getComplementsActifs();
        complements.forEach(c -> System.out.printf("- %s (%s) : %.0f FCFA%n", 
            c.getNom(), c.getType(), c.getPrix()));
    }
    
    private void afficherBurgers() {
        System.out.println("\n=== BURGERS ===");
        List<Burger> burgers = burgerService.getBurgersActifs();
        
        for (int i = 0; i < burgers.size(); i++) {
            Burger b = burgers.get(i);
            System.out.printf("%d. %s - %.0f FCFA%n", i + 1, b.getNom(), b.getPrix());
            System.out.println("   " + b.getDescription());
        }
        
        System.out.print("\nVoir les détails d'un burger (numéro) ou 0 pour retour : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        if (choix > 0 && choix <= burgers.size()) {
            Burger burger = burgers.get(choix - 1);
            System.out.println("\n=== DÉTAILS BURGER ===");
            System.out.println("Nom : " + burger.getNom());
            System.out.println("Prix : " + burger.getPrix() + " FCFA");
            System.out.println("Description : " + burger.getDescription());
        }
    }
    
    private void afficherMenus() {
        System.out.println("\n=== MENUS ===");
        List<Menu> menus = menuService.getMenusActifs();
        
        for (int i = 0; i < menus.size(); i++) {
            Menu m = menus.get(i);
            System.out.printf("%d. %s - %.0f FCFA%n", i + 1, m.getNom(), m.getPrix());
            System.out.println("   Burger : " + m.getBurger().getNom());
            if (m.getFrite() != null) System.out.println("   Frites : " + m.getFrite().getNom());
            if (m.getBoisson() != null) System.out.println("   Boisson : " + m.getBoisson().getNom());
        }
        
        System.out.print("\nVoir les détails d'un menu (numéro) ou 0 pour retour : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        if (choix > 0 && choix <= menus.size()) {
            Menu menu = menus.get(choix - 1);
            System.out.println("\n=== DÉTAILS MENU ===");
            System.out.println("Nom : " + menu.getNom());
            System.out.println("Prix total : " + menu.getPrix() + " FCFA");
            System.out.println("Détail du prix :");
            System.out.println("  - Burger : " + menu.getBurger().getNom() + " (" + menu.getBurger().getPrix() + " FCFA)");
            if (menu.getFrite() != null) 
                System.out.println("  - Frites : " + menu.getFrite().getNom() + " (" + menu.getFrite().getPrix() + " FCFA)");
            if (menu.getBoisson() != null) 
                System.out.println("  - Boisson : " + menu.getBoisson().getNom() + " (" + menu.getBoisson().getPrix() + " FCFA)");
        }
    }
    
    private void creerCommande() {
        System.out.println("\n=== NOUVELLE COMMANDE ===");
        
        Commande commande = new Commande();
        commande.setClient(client);
        
        // Choisir le type de livraison
        System.out.println("Type de livraison :");
        System.out.println("1. Sur place");
        System.out.println("2. À emporter");
        System.out.println("3. Livraison");
        System.out.print("Choix : ");
        int typeLivraison = scanner.nextInt();
        scanner.nextLine();
        
        switch (typeLivraison) {
            case 1 : 
                commande.setTypeLivraison("SUR_PLACE");
                break;
            case 2 : 
                commande.setTypeLivraison("A_EMPORTER");
                break;
            case 3 : 
                {
                    commande.setTypeLivraison("LIVRAISON");
                    System.out.print("Adresse de livraison : ");
                    commande.setAdresseLivraison(scanner.nextLine());
                }
                break;
            default :
                {
                    System.out.println("Choix invalide !");
                    return;
                }
        }
        
        // Ajouter des items
        boolean continuer = true;
        while (continuer) {
            System.out.println("\nAjouter un produit :");
            System.out.println("1. Burger");
            System.out.println("2. Menu");
            System.out.println("3. Complément");
            System.out.println("4. Terminer la commande");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine();
            
            switch (choix) {
                case 1 : 
                    ajouterBurgerACommande(commande);
                    break;
                case 2 : 
                    ajouterMenuACommande(commande);
                    break;
                case 3 : 
                    ajouterComplementACommande(commande);
                    break;
                case 4 : 
                    continuer = false;
                    break;
                default :
                     System.out.println("Choix invalide !");
            }
        }
        
        if (commande.getItems().isEmpty()) {
            System.out.println("Commande annulée (aucun produit ajouté)");
            return;
        }
        
        // Confirmer la commande
        System.out.println("\n=== RÉCAPITULATIF DE LA COMMANDE ===");
        System.out.println("Client : " + commande.getClient().getNomComplet());
        System.out.println("Type de livraison : " + commande.getTypeLivraison());
        if (commande.getAdresseLivraison() != null) {
            System.out.println("Adresse : " + commande.getAdresseLivraison());
        }
        
        System.out.println("\nProduits :");
        commande.getItems().forEach(item -> {
            System.out.printf("- %s x%d : %.0f FCFA%n", 
                item.getProduitNom(), item.getQuantite(), item.getSousTotal());
        });
        
        System.out.printf("Total : %.0f FCFA%n", commande.getTotal());
        
        System.out.print("\nConfirmer la commande ? (oui/non) : ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("oui")) {
            Commande commandeCreee = commandeService.creerCommande(commande);
            System.out.println("Commande créée avec succès ! Numéro : " + commandeCreee.getId());
            
            // Proposer le paiement
            System.out.print("Voulez-vous payer maintenant ? (oui/non) : ");
            String payer = scanner.nextLine();
            
            if (payer.equalsIgnoreCase("oui")) {
                effectuerPaiement(commandeCreee);
            }
        } else {
            System.out.println("Commande annulée.");
        }
    }
    
    private void ajouterBurgerACommande(Commande commande) {
        List<Burger> burgers = burgerService.getBurgersActifs();
        
        System.out.println("\nChoisissez un burger :");
        for (int i = 0; i < burgers.size(); i++) {
            System.out.printf("%d. %s - %.0f FCFA%n", i + 1, burgers.get(i).getNom(), burgers.get(i).getPrix());
        }
        
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        if (choix > 0 && choix <= burgers.size()) {
            System.out.print("Quantité : ");
            int quantite = scanner.nextInt();
            scanner.nextLine();
            
            if (quantite > 0) {
                Burger burger = burgers.get(choix - 1);
                CommandeItem item = new CommandeItem(commande, burger, quantite, burger.getPrix());
                commande.addItem(item);
                System.out.println(quantite + " x " + burger.getNom() + " ajouté(s) à la commande.");
            }
        }
    }
    
    private void ajouterMenuACommande(Commande commande) {
        List<Menu> menus = menuService.getMenusActifs();
        
        System.out.println("\nChoisissez un menu :");
        for (int i = 0; i < menus.size(); i++) {
            System.out.printf("%d. %s - %.0f FCFA%n", i + 1, menus.get(i).getNom(), menus.get(i).getPrix());
        }
        
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        if (choix > 0 && choix <= menus.size()) {
            System.out.print("Quantité : ");
            int quantite = scanner.nextInt();
            scanner.nextLine();
            
            if (quantite > 0) {
                Menu menu = menus.get(choix - 1);
                CommandeItem item = new CommandeItem(commande, menu, quantite, menu.getPrix());
                commande.addItem(item);
                System.out.println(quantite + " x " + menu.getNom() + " ajouté(s) à la commande.");
            }
        }
    }
    
    private void ajouterComplementACommande(Commande commande) {
        List<Complement> complements = complementService.getComplementsActifs();
        
        System.out.println("\nChoisissez un complément :");
        for (int i = 0; i < complements.size(); i++) {
            System.out.printf("%d. %s (%s) - %.0f FCFA%n", 
                i + 1, complements.get(i).getNom(), complements.get(i).getType(), complements.get(i).getPrix());
        }
        
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        if (choix > 0 && choix <= complements.size()) {
            System.out.print("Quantité : ");
            int quantite = scanner.nextInt();
            scanner.nextLine();
            
            if (quantite > 0) {
                Complement complement = complements.get(choix - 1);
                CommandeItem item = new CommandeItem();
                item.setCommande(commande);
                item.setComplement(complement);
                item.setQuantite(quantite);
                item.setPrixUnitaire(complement.getPrix());
                commande.addItem(item);
                System.out.println(quantite + " x " + complement.getNom() + " ajouté(s) à la commande.");
            }
        }
    }
    
    private void afficherMesCommandes() {
        System.out.println("\n=== MES COMMANDES ===");
        List<Commande> commandes = commandeService.getCommandesParClient(client.getId());
        
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée.");
            return;
        }
        
        for (Commande commande : commandes) {
            System.out.printf("Commande #%d - %s - Total: %.0f FCFA - État: %s%n",
                commande.getId(), 
                commande.getDateCommande().toLocalDate(),
                commande.getTotal(),
                commande.getEtat());
            
            System.out.println("  Produits :");
            commande.getItems().forEach(item -> {
                System.out.printf("  - %s x%d : %.0f FCFA%n",
                    item.getProduitNom(), item.getQuantite(), item.getSousTotal());
            });
            
            if (commande.isPayee()) {
                System.out.println("  ✅ Payée");
            } else {
                System.out.println("  ❌ Non payée");
            }
            System.out.println();
        }
    }
    
    private void suivreCommande() {
        System.out.print("\nNuméro de commande à suivre : ");
        int commandeId = scanner.nextInt();
        scanner.nextLine();
        
        Commande commande = commandeService.getCommandeById(commandeId);
        
        if (commande == null || commande.getClient().getId() != client.getId()) {
            System.out.println("Commande non trouvée ou vous n'avez pas accès à cette commande.");
            return;
        }
        
        System.out.println("\n=== SUIVI COMMANDE #" + commandeId + " ===");
        System.out.println("Date : " + commande.getDateCommande());
        System.out.println("État : " + commande.getEtat());
        System.out.println("Type de livraison : " + commande.getTypeLivraison());
        
        if (commande.getAdresseLivraison() != null) {
            System.out.println("Adresse : " + commande.getAdresseLivraison());
        }
        
        System.out.println("\nProduits :");
        commande.getItems().forEach(item -> {
            System.out.printf("- %s x%d : %.0f FCFA%n",
                item.getProduitNom(), item.getQuantite(), item.getSousTotal());
        });
        
        System.out.printf("Total : %.0f FCFA%n", commande.getTotal());
        System.out.println("Payée : " + (commande.isPayee() ? "✅ Oui" : "❌ Non"));
    }
    
    private void payerCommande() {
        System.out.print("\nNuméro de commande à payer : ");
        int commandeId = scanner.nextInt();
        scanner.nextLine();
        
        Commande commande = commandeService.getCommandeById(commandeId);
        
        if (commande == null || commande.getClient().getId() != client.getId()) {
            System.out.println("Commande non trouvée ou vous n'avez pas accès à cette commande.");
            return;
        }
        
        if (commande.isPayee()) {
            System.out.println("Cette commande a déjà été payée.");
            return;
        }
        
        System.out.println("\n=== PAIEMENT COMMANDE #" + commandeId + " ===");
        System.out.printf("Montant à payer : %.0f FCFA%n", commande.getTotal());
        
        System.out.println("\nMéthode de paiement :");
        System.out.println("1. Wave");
        System.out.println("2. Orange Money");
        System.out.println("3. Carte bancaire");
        System.out.println("4. Espèces");
        System.out.print("Choix : ");
        int methodeChoix = scanner.nextInt();
        scanner.nextLine();
        
        String methode;
        switch (methodeChoix) {
            case 1 : 
                methode = "WAVE";
                break;
            case 2 : 
                methode = "OM";
                break;
            case 3 : 
                methode = "CARTE";
                break;
            case 4 : 
                methode = "ESPECES";
                break;
            default : 
               {
                    System.out.println("Choix invalide !");
                    return;
                }
        }
        
        System.out.print("Référence (numéro de transaction) : ");
        String reference = scanner.nextLine();
        
        Paiement paiement = new Paiement(commande, commande.getTotal(), methode, reference);
        
        try {
            paiementService.effectuerPaiement(paiement);
            System.out.println("Paiement effectué avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors du paiement : " + e.getMessage());
        }
    }
    
    private void effectuerPaiement(Commande commande) {
        System.out.println("\n=== PAIEMENT ===");
        System.out.printf("Montant à payer : %.0f FCFA%n", commande.getTotal());
        
        System.out.println("\nMéthode de paiement :");
        System.out.println("1. Wave");
        System.out.println("2. Orange Money");
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        String methode = (choix == 1) ? "WAVE" : "OM";
        System.out.print("Référence (numéro de transaction) : ");
        String reference = scanner.nextLine();
        
        Paiement paiement = new Paiement(commande, commande.getTotal(), methode, reference);
        
        try {
            paiementService.effectuerPaiement(paiement);
            System.out.println("Paiement effectué avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors du paiement : " + e.getMessage());
        }
    }
}
