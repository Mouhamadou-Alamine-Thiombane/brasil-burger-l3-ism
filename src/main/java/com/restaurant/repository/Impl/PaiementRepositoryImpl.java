package com.restaurant.repository.Impl;

import com.restaurant.entity.Paiement;
import com.restaurant.entity.Commande;
import com.restaurant.repository.PaiementRepository;
import com.restaurant.repository.CommandeRepository;
import com.restaurant.config.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaiementRepositoryImpl implements PaiementRepository {
    
    private CommandeRepository commandeRepository = new CommandeRepositoryImpl();
    
    @Override
    public Paiement save(Paiement paiement) {
        String sql = "INSERT INTO paiements (commande_id, montant, methode, date_paiement, reference, archived) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, paiement.getCommande().getId());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setString(3, paiement.getMethode());
            stmt.setTimestamp(4, Timestamp.valueOf(paiement.getDatePaiement()));
            stmt.setString(5, paiement.getReference());
            stmt.setBoolean(6, paiement.isArchived());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                paiement.setId(rs.getInt("id"));
                
                // Mettre à jour le statut de la commande
                updateCommandePayee(paiement.getCommande().getId());
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return paiement;
    }
    
    private void updateCommandePayee(int commandeId) {
        String sql = "UPDATE commandes SET payee = true WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commandeId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Optional<Paiement> findById(int id) {
        String sql = "SELECT * FROM paiements WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToPaiement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Paiement> findAll() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements ORDER BY date_paiement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return paiements;
    }
    
    @Override
    public List<Paiement> findAllActive() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements WHERE archived = false ORDER BY date_paiement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return paiements;
    }
    
    @Override
    public Paiement update(Paiement paiement) {
        String sql = "UPDATE paiements SET commande_id = ?, montant = ?, methode = ?, " +
                     "date_paiement = ?, reference = ?, archived = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, paiement.getCommande().getId());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setString(3, paiement.getMethode());
            stmt.setTimestamp(4, Timestamp.valueOf(paiement.getDatePaiement()));
            stmt.setString(5, paiement.getReference());
            stmt.setBoolean(6, paiement.isArchived());
            stmt.setInt(7, paiement.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return paiement;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM paiements WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean archive(int id) {
        String sql = "UPDATE paiements SET archived = true WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Optional<Paiement> findByCommandeId(int commandeId) {
        String sql = "SELECT * FROM paiements WHERE commande_id = ? AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToPaiement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Paiement> findByDate(LocalDate date) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements WHERE DATE(date_paiement) = ? AND archived = false ORDER BY date_paiement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return paiements;
    }
    
    @Override
    public List<Paiement> findByMethode(String methode) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements WHERE methode = ? AND archived = false ORDER BY date_paiement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, methode);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return paiements;
    }
    
    @Override
    public double getTotalRecettesByDate(LocalDate date) {
        String sql = "SELECT COALESCE(SUM(montant), 0) as total FROM paiements WHERE DATE(date_paiement) = ? AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
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
    public boolean commandeEstPayee(int commandeId) {
        String sql = "SELECT COUNT(*) FROM paiements WHERE commande_id = ? AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Paiement mapResultSetToPaiement(ResultSet rs) throws SQLException {
        Paiement paiement = new Paiement();
        paiement.setId(rs.getInt("id"));
        
        // Récupérer la commande
        int commandeId = rs.getInt("commande_id");
        commandeRepository.findById(commandeId).ifPresent(paiement::setCommande);
        
        paiement.setMontant(rs.getDouble("montant"));
        paiement.setMethode(rs.getString("methode"));
        paiement.setDatePaiement(rs.getTimestamp("date_paiement").toLocalDateTime());
        paiement.setReference(rs.getString("reference"));
        paiement.setArchived(rs.getBoolean("archived"));
        paiement.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            paiement.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return paiement;
    }
}
