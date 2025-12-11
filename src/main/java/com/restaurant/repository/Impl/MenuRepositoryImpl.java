package com.restaurant.repository.Impl;

import com.restaurant.entity.Menu;
import com.restaurant.entity.Burger;
import com.restaurant.entity.Complement;
import com.restaurant.repository.MenuRepository;
import com.restaurant.repository.BurgerRepository;
import com.restaurant.repository.ComplementRepository;
import com.restaurant.config.DatabaseConnection; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepositoryImpl implements MenuRepository {
    
    private BurgerRepository burgerRepository = new BurgerRepositoryImpl();
    private ComplementRepository complementRepository = new ComplementRepositoryImpl();
    
    @Override
    public Menu save(Menu menu) {
        String sql = "INSERT INTO menus (nom, burger_id, frite_id, boisson_id, image, prix, archived) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menu.getNom());
            stmt.setInt(2, menu.getBurger().getId());
            
            if (menu.getFrite() != null) {
                stmt.setInt(3, menu.getFrite().getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            if (menu.getBoisson() != null) {
                stmt.setInt(4, menu.getBoisson().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, menu.getImage());
            stmt.setDouble(6, menu.getPrix());
            stmt.setBoolean(7, menu.isArchived());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                menu.setId(rs.getInt("id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menu;
    }
    
    @Override
    public Optional<Menu> findById(int id) {
        String sql = "SELECT * FROM menus WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToMenu(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Menu> findAll() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menus ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                menus.add(mapResultSetToMenu(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menus;
    }
    
    @Override
    public List<Menu> findAllActive() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT m.* FROM menus m " +
                     "WHERE m.archived = false " +
                     "AND EXISTS (SELECT 1 FROM burgers b WHERE b.id = m.burger_id AND b.archived = false) " +
                     "ORDER BY m.nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                menus.add(mapResultSetToMenu(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menus;
    }
    
    @Override
    public Menu update(Menu menu) {
        String sql = "UPDATE menus SET nom = ?, burger_id = ?, frite_id = ?, boisson_id = ?, " +
                     "image = ?, prix = ?, archived = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menu.getNom());
            stmt.setInt(2, menu.getBurger().getId());
            
            if (menu.getFrite() != null) {
                stmt.setInt(3, menu.getFrite().getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            if (menu.getBoisson() != null) {
                stmt.setInt(4, menu.getBoisson().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, menu.getImage());
            stmt.setDouble(6, menu.getPrix());
            stmt.setBoolean(7, menu.isArchived());
            stmt.setInt(8, menu.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menu;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM menus WHERE id = ?";
        
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
        String sql = "UPDATE menus SET archived = true WHERE id = ?";
        
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
    public List<Menu> findByBurgerId(int burgerId) {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menus WHERE burger_id = ? AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, burgerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                menus.add(mapResultSetToMenu(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menus;
    }
    
    @Override
    public List<Menu> findByNomContaining(String keyword) {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menus WHERE LOWER(nom) LIKE LOWER(?) AND archived = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                menus.add(mapResultSetToMenu(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menus;
    }
    
    @Override
    public double calculatePrixMenu(int menuId) {
        String sql = "SELECT " +
                     "b.prix + " +
                     "COALESCE(f.prix, 0) + " +
                     "COALESCE(bo.prix, 0) as total " +
                     "FROM menus m " +
                     "JOIN burgers b ON m.burger_id = b.id " +
                     "LEFT JOIN complements f ON m.frite_id = f.id " +
                     "LEFT JOIN complements bo ON m.boisson_id = bo.id " +
                     "WHERE m.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getInt("id"));
        menu.setNom(rs.getString("nom"));
        menu.setImage(rs.getString("image"));
        menu.setPrix(rs.getDouble("prix"));
        menu.setArchived(rs.getBoolean("archived"));
        menu.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            menu.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Récupérer le burger
        int burgerId = rs.getInt("burger_id");
        burgerRepository.findById(burgerId).ifPresent(menu::setBurger);
        
        // Récupérer la frite
        int friteId = rs.getInt("frite_id");
        if (!rs.wasNull()) {
            complementRepository.findById(friteId).ifPresent(menu::setFrite);
        }
        
        // Récupérer la boisson
        int boissonId = rs.getInt("boisson_id");
        if (!rs.wasNull()) {
            complementRepository.findById(boissonId).ifPresent(menu::setBoisson);
        }
        
        return menu;
    }
}
