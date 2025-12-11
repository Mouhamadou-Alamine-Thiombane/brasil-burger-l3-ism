package com.restaurant.repository.Impl;

import com.restaurant.entity.Burger;
import com.restaurant.repository.BurgerRepository;
import com.restaurant.config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurgerRepositoryImpl implements BurgerRepository {
    
    @Override
    public Burger save(Burger burger) {
        String sql = "INSERT INTO burgers (nom, prix, description, image, archived) VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, burger.getNom());
            stmt.setDouble(2, burger.getPrix());
            stmt.setString(3, burger.getDescription());
            stmt.setString(4, burger.getImage());
            stmt.setBoolean(5, burger.isArchived());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                burger.setId(rs.getInt("id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return burger;
    }
    
    @Override
    public Optional<Burger> findById(int id) {
        String sql = "SELECT * FROM burgers WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBurger(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Burger> findAll() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burgers ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return burgers;
    }
    
    @Override
    public List<Burger> findAllActive() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burgers WHERE archived = false ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return burgers;
    }
    
    @Override
    public Burger update(Burger burger) {
        String sql = "UPDATE burgers SET nom = ?, prix = ?, description = ?, image = ?, archived = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, burger.getNom());
            stmt.setDouble(2, burger.getPrix());
            stmt.setString(3, burger.getDescription());
            stmt.setString(4, burger.getImage());
            stmt.setBoolean(5, burger.isArchived());
            stmt.setInt(6, burger.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return burger;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM burgers WHERE id = ?";
        
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
        String sql = "UPDATE burgers SET archived = true WHERE id = ?";
        
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
    public List<Burger> findByNom(String nom) {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burgers WHERE LOWER(nom) LIKE LOWER(?) AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return burgers;
    }
    
    @Override
    public List<Burger> findByPrixBetween(double min, double max) {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burgers WHERE prix BETWEEN ? AND ? AND archived = false ORDER BY prix";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, min);
            stmt.setDouble(2, max);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return burgers;
    }
    
    private Burger mapResultSetToBurger(ResultSet rs) throws SQLException {
        Burger burger = new Burger();
        burger.setId(rs.getInt("id"));
        burger.setNom(rs.getString("nom"));
        burger.setPrix(rs.getDouble("prix"));
        burger.setDescription(rs.getString("description"));
        burger.setImage(rs.getString("image"));
        burger.setArchived(rs.getBoolean("archived"));
        burger.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            burger.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return burger;
    }
}