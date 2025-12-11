package com.restaurant.repository.Impl;

import com.restaurant.entity.Complement;
import com.restaurant.repository.ComplementRepository;
import com.restaurant.config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplementRepositoryImpl implements ComplementRepository {
    
    @Override
    public Complement save(Complement complement) {
        String sql = "INSERT INTO complements (nom, prix, type, image, archived) VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, complement.getNom());
            stmt.setDouble(2, complement.getPrix());
            stmt.setString(3, complement.getType());
            stmt.setString(4, complement.getImage());
            stmt.setBoolean(5, complement.isArchived());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                complement.setId(rs.getInt("id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complement;
    }
    
    @Override
    public Optional<Complement> findById(int id) {
        String sql = "SELECT * FROM complements WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToComplement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Complement> findAll() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complements ORDER BY type, nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complements;
    }
    
    @Override
    public List<Complement> findAllActive() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complements WHERE archived = false ORDER BY type, nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complements;
    }
    
    @Override
    public Complement update(Complement complement) {
        String sql = "UPDATE complements SET nom = ?, prix = ?, type = ?, image = ?, archived = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, complement.getNom());
            stmt.setDouble(2, complement.getPrix());
            stmt.setString(3, complement.getType());
            stmt.setString(4, complement.getImage());
            stmt.setBoolean(5, complement.isArchived());
            stmt.setInt(6, complement.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complement;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM complements WHERE id = ?";
        
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
        String sql = "UPDATE complements SET archived = true WHERE id = ?";
        
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
    public List<Complement> findByType(String type) {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complements WHERE type = ? AND archived = false ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complements;
    }
    
    @Override
    public List<Complement> findFrites() {
        return findByType("FRITE");
    }
    
    @Override
    public List<Complement> findBoissons() {
        return findByType("BOISSON");
    }
    
    private Complement mapResultSetToComplement(ResultSet rs) throws SQLException {
        Complement complement = new Complement();
        complement.setId(rs.getInt("id"));
        complement.setNom(rs.getString("nom"));
        complement.setPrix(rs.getDouble("prix"));
        complement.setType(rs.getString("type"));
        complement.setImage(rs.getString("image"));
        complement.setArchived(rs.getBoolean("archived"));
        complement.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            complement.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return complement;
    }
}
