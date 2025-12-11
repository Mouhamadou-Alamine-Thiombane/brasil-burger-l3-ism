package com.restaurant.repository.Impl;

import com.restaurant.repository.StatistiqueRepository;
import com.restaurant.config.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class StatistiqueRepositoryImpl implements StatistiqueRepository {
    
    @Override
    public int getCommandesEnCoursDuJour() {
        String sql = "SELECT COUNT(*) as count FROM commandes " +
                     "WHERE DATE(date_commande) = CURRENT_DATE " +
                     "AND etat IN ('EN_ATTENTE', 'VALIDEE', 'EN_PREPARATION') " +
                     "AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    @Override
    public int getCommandesValideesDuJour() {
        String sql = "SELECT COUNT(*) as count FROM commandes " +
                     "WHERE DATE(date_commande) = CURRENT_DATE " +
                     "AND etat = 'VALIDEE' " +
                     "AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    @Override
    public double getRecettesJournalieres(LocalDate date) {
        String sql = "SELECT COALESCE(SUM(p.montant), 0) as total " +
                     "FROM paiements p " +
                     "WHERE DATE(p.date_paiement) = ? " +
                     "AND p.archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Utiliser java.sql.Date.valueOf() au lieu de Date.valueOf()
            stmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    @Override
    public List<Map<String, Object>> getBurgersPlusVendusDuJour() {
        List<Map<String, Object>> burgers = new ArrayList<>();
        String sql = "SELECT b.id, b.nom, SUM(ci.quantite) as total_vendu " +
                     "FROM commande_items ci " +
                     "JOIN burgers b ON ci.burger_id = b.id " +
                     "JOIN commandes c ON ci.commande_id = c.id " +
                     "WHERE DATE(c.date_commande) = CURRENT_DATE " +
                     "AND c.archived = false " +
                     "GROUP BY b.id, b.nom " +
                     "ORDER BY total_vendu DESC " +
                     "LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> burger = new HashMap<>();
                burger.put("id", rs.getInt("id"));
                burger.put("nom", rs.getString("nom"));
                burger.put("total_vendu", rs.getInt("total_vendu"));
                burgers.add(burger);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return burgers;
    }
    
    @Override
    public int getCommandesAnnuleesDuJour() {
        String sql = "SELECT COUNT(*) as count FROM commandes " +
                     "WHERE DATE(date_commande) = CURRENT_DATE " +
                     "AND etat = 'ANNULEE' " +
                     "AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    @Override
    public Map<String, Double> getChiffreAffaireParMois(int annee) {
        Map<String, Double> chiffreAffaire = new LinkedHashMap<>();
        String sql = "SELECT TO_CHAR(date_paiement, 'Month') as mois, " +
                     "EXTRACT(MONTH FROM date_paiement) as mois_num, " +
                     "SUM(montant) as total " +
                     "FROM paiements " +
                     "WHERE EXTRACT(YEAR FROM date_paiement) = ? " +
                     "AND archived = false " +
                     "GROUP BY mois, mois_num " +
                     "ORDER BY mois_num";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, annee);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String mois = rs.getString("mois").trim();
                double total = rs.getDouble("total");
                chiffreAffaire.put(mois, total);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chiffreAffaire;
    }
    
    @Override
    public int getNombreClientsInscrits(LocalDate date) {
        String sql = "SELECT COUNT(*) as count FROM clients " +
                     "WHERE DATE(created_at) = ? " +
                     "AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    @Override
    public Map<String, Integer> getVentesParTypeProduit(LocalDate date) {
        Map<String, Integer> ventes = new HashMap<>();
        String sql = "SELECT 'Burger' as type, COALESCE(SUM(ci.quantite), 0) as total " +
                     "FROM commande_items ci " +
                     "JOIN commandes c ON ci.commande_id = c.id " +
                     "WHERE ci.burger_id IS NOT NULL " +
                     "AND DATE(c.date_commande) = ? " +
                     "AND c.archived = false " +
                     "UNION ALL " +
                     "SELECT 'Menu' as type, COALESCE(SUM(ci.quantite), 0) as total " +
                     "FROM commande_items ci " +
                     "JOIN commandes c ON ci.commande_id = c.id " +
                     "WHERE ci.menu_id IS NOT NULL " +
                     "AND DATE(c.date_commande) = ? " +
                     "AND c.archived = false " +
                     "UNION ALL " +
                     "SELECT 'Complement' as type, COALESCE(SUM(ci.quantite), 0) as total " +
                     "FROM commande_items ci " +
                     "JOIN commandes c ON ci.commande_id = c.id " +
                     "WHERE ci.complement_id IS NOT NULL " +
                     "AND DATE(c.date_commande) = ? " +
                     "AND c.archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Utiliser java.sql.Date.valueOf() au lieu de Date.valueOf()
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);
            stmt.setDate(1, sqlDate);
            stmt.setDate(2, sqlDate);
            stmt.setDate(3, sqlDate);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String type = rs.getString("type");
                int total = rs.getInt("total");
                ventes.put(type, total);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventes;
    }
}