package com.restaurant.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties properties = new Properties();
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        try {
            // Essayer de charger depuis le classpath
            InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties");
            
            if (input == null) {
                System.out.println("⚠️  Fichier application.properties non trouvé dans classpath");
                System.out.println("🔍 Recherche dans d'autres emplacements...");
                
                // Essayer un autre chemin
                input = DatabaseConfig.class.getResourceAsStream("/application.properties");
                
                if (input == null) {
                    System.out.println("❌ Impossible de trouver application.properties");
                    System.out.println("📌 Utilisation des valeurs par défaut...");
                    
                    // Valeurs par défaut (à adapter)
                    properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/brasil_burger");
                    properties.setProperty("db.username", "postgres");
                    properties.setProperty("db.password", "RAMAdiop@1975");
                    properties.setProperty("db.driver", "org.postgresql.Driver");
                    return;
                }
            }
            
            // Charger les propriétés
            properties.load(input);
            input.close();
            
            System.out.println("✅ Fichier application.properties chargé avec succès");
            System.out.println("📌 URL : " + getUrl());
            
        } catch (IOException ex) {
            System.out.println("❌ Erreur lors du chargement du fichier de configuration");
            ex.printStackTrace();
            
            // Valeurs par défaut en cas d'erreur
            properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/brasil_burger");
            properties.setProperty("db.username", "postgres");
            properties.setProperty("db.password", "RAMAdiop@1975");
            properties.setProperty("db.driver", "org.postgresql.Driver");
        }
    }
    
    public static String getUrl() {
        return properties.getProperty("db.url", "jdbc:postgresql://localhost:5432/brasil_burger");
    }
    
    public static String getUsername() {
        return properties.getProperty("db.username", "postgres");
    }
    
    public static String getPassword() {
        return properties.getProperty("db.password", "RAMAdiop@1975");
    }
    
    public static String getDriver() {
        return properties.getProperty("db.driver", "org.postgresql.Driver");
    }
    
    // Méthode pour afficher la configuration
    public static void printConfig() {
        System.out.println("\n=== CONFIGURATION DATABASE ===");
        System.out.println("URL : " + getUrl());
        System.out.println("Username : " + getUsername());
        System.out.println("Driver : " + getDriver());
        System.out.println("=============================\n");
    }
}