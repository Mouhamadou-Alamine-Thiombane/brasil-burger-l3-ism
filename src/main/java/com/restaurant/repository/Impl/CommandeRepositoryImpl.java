package com.restaurant.repository.Impl;

import com.restaurant.entity.*;
import com.restaurant.repository.CommandeRepository;
import com.restaurant.repository.ClientRepository;
import com.restaurant.config.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandeRepositoryImpl implements CommandeRepository {
    
    private ClientRepository clientRepository = new ClientRepositoryImpl();
    
    @Override
    public Commande save(Commande commande) {
        String sql = "INSERT INTO commandes (client_id, etat, type_livraison, date_commande, " +
                     "zone_id, livreur_id, adresse_livraison, total, payee, archived) " +
                     "VALUES (?, ?::etat_commande, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commande.getClient().getId());
            stmt.setString(2, commande.getEtat().name());
            stmt.setString(3, commande.getTypeLivraison());
            stmt.setTimestamp(4, Timestamp.valueOf(commande.getDateCommande()));
            
            if (commande.getZone() != null) {
                stmt.setInt(5, commande.getZone().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            if (commande.getLivreur() != null) {
                stmt.setInt(6, commande.getLivreur().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setString(7, commande.getAdresseLivraison());
            stmt.setDouble(8, commande.getTotal());
            stmt.setBoolean(9, commande.isPayee());
            stmt.setBoolean(10, commande.isArchived());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                commande.setId(rs.getInt("id"));
                saveCommandeItems(commande);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commande;
    }
    
    private void saveCommandeItems(Commande commande) {
        String sql = "INSERT INTO commande_items (commande_id, burger_id, menu_id, complement_id, " +
                     "quantite, prix_unitaire) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (CommandeItem item : commande.getItems()) {
                stmt.setInt(1, commande.getId());
                
                if (item.getBurger() != null) {
                    stmt.setInt(2, item.getBurger().getId());
                } else {
                    stmt.setNull(2, Types.INTEGER);
                }
                
                if (item.getMenu() != null) {
                    stmt.setInt(3, item.getMenu().getId());
                } else {
                    stmt.setNull(3, Types.INTEGER);
                }
                
                if (item.getComplement() != null) {
                    stmt.setInt(4, item.getComplement().getId());
                } else {
                    stmt.setNull(4, Types.INTEGER);
                }
                
                stmt.setInt(5, item.getQuantite());
                stmt.setDouble(6, item.getPrixUnitaire());
                
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Optional<Commande> findById(int id) {
        String sql = "SELECT * FROM commandes WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Commande> findAll() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public List<Commande> findAllActive() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE archived = false ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public Commande update(Commande commande) {
        String sql = "UPDATE commandes SET client_id = ?, etat = ?::etat_commande, " +
                     "type_livraison = ?, date_commande = ?, zone_id = ?, livreur_id = ?, " +
                     "adresse_livraison = ?, total = ?, payee = ?, archived = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commande.getClient().getId());
            stmt.setString(2, commande.getEtat().name());
            stmt.setString(3, commande.getTypeLivraison());
            stmt.setTimestamp(4, Timestamp.valueOf(commande.getDateCommande()));
            
            if (commande.getZone() != null) {
                stmt.setInt(5, commande.getZone().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            if (commande.getLivreur() != null) {
                stmt.setInt(6, commande.getLivreur().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setString(7, commande.getAdresseLivraison());
            stmt.setDouble(8, commande.getTotal());
            stmt.setBoolean(9, commande.isPayee());
            stmt.setBoolean(10, commande.isArchived());
            stmt.setInt(11, commande.getId());
            
            stmt.executeUpdate();
            
            // Mettre à jour les items
            updateCommandeItems(commande);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commande;
    }
    
    private void updateCommandeItems(Commande commande) {
        // Supprimer les anciens items
        String deleteSql = "DELETE FROM commande_items WHERE commande_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            
            deleteStmt.setInt(1, commande.getId());
            deleteStmt.executeUpdate();
            
            // Insérer les nouveaux items
            saveCommandeItems(commande);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM commandes WHERE id = ?";
        
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
        String sql = "UPDATE commandes SET archived = true WHERE id = ?";
        
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
    public List<Commande> findByClientId(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE client_id = ? AND archived = false ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public List<Commande> findByEtat(EtatCommande etat) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE etat = ?::etat_commande AND archived = false ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, etat.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public List<Commande> findByDate(LocalDate date) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE DATE(date_commande) = ? AND archived = false ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public List<Commande> findByClientAndEtat(int clientId, EtatCommande etat) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE client_id = ? AND etat = ?::etat_commande " +
                     "AND archived = false ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            stmt.setString(2, etat.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public boolean updateEtat(int commandeId, EtatCommande etat) {
        String sql = "UPDATE commandes SET etat = ?::etat_commande, updated_at = NOW() WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, etat.name());
            stmt.setInt(2, commandeId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public List<Commande> findCommandesEnCours() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE etat IN ('EN_ATTENTE', 'VALIDEE', 'EN_PREPARATION', 'PRETE') " +
                     "AND archived = false AND DATE(date_commande) = CURRENT_DATE ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public List<Commande> findCommandesDuJour() {
        return findByDate(LocalDate.now());
    }
    
    @Override
    public List<Commande> filterByBurger(int burgerId) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* FROM commandes c " +
                     "JOIN commande_items ci ON c.id = ci.commande_id " +
                     "WHERE ci.burger_id = ? AND c.archived = false ORDER BY c.date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, burgerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public List<Commande> filterByMenu(int menuId) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* FROM commandes c " +
                     "JOIN commande_items ci ON c.id = ci.commande_id " +
                     "WHERE ci.menu_id = ? AND c.archived = false ORDER BY c.date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    @Override
    public List<Commande> filterByDateRange(LocalDate start, LocalDate end) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE DATE(date_commande) BETWEEN ? AND ? " +
                     "AND archived = false ORDER BY date_commande DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commandes;
    }
    
    private Commande mapResultSetToCommande(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("id"));
        
        // Récupérer le client
        int clientId = rs.getInt("client_id");
        clientRepository.findById(clientId).ifPresent(commande::setClient);
        
        commande.setEtat(EtatCommande.valueOf(rs.getString("etat")));
        commande.setTypeLivraison(rs.getString("type_livraison"));
        commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
        
        Timestamp dateLivraison = rs.getTimestamp("date_livraison");
        if (dateLivraison != null) {
            commande.setDateLivraison(dateLivraison.toLocalDateTime());
        }
        
        commande.setAdresseLivraison(rs.getString("adresse_livraison"));
        commande.setTotal(rs.getDouble("total"));
        commande.setPayee(rs.getBoolean("payee"));
        commande.setArchived(rs.getBoolean("archived"));
        commande.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            commande.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Charger les items de la commande
        loadCommandeItems(commande);
        
        return commande;
    }
    
    private void loadCommandeItems(Commande commande) {
        String sql = "SELECT ci.*, b.nom as burger_nom, m.nom as menu_nom, cp.nom as complement_nom " +
                     "FROM commande_items ci " +
                     "LEFT JOIN burgers b ON ci.burger_id = b.id " +
                     "LEFT JOIN menus m ON ci.menu_id = m.id " +
                     "LEFT JOIN complements cp ON ci.complement_id = cp.id " +
                     "WHERE ci.commande_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commande.getId());
            ResultSet rs = stmt.executeQuery();
            
            BurgerRepositoryImpl burgerRepo = new BurgerRepositoryImpl();
            MenuRepositoryImpl menuRepo = new MenuRepositoryImpl();
            ComplementRepositoryImpl complementRepo = new ComplementRepositoryImpl();
            
            while (rs.next()) {
                CommandeItem item = new CommandeItem();
                item.setId(rs.getInt("id"));
                item.setQuantite(rs.getInt("quantite"));
                item.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                
                int burgerId = rs.getInt("burger_id");
                if (!rs.wasNull()) {
                    burgerRepo.findById(burgerId).ifPresent(item::setBurger);
                }
                
                int menuId = rs.getInt("menu_id");
                if (!rs.wasNull()) {
                    menuRepo.findById(menuId).ifPresent(item::setMenu);
                }
                
                int complementId = rs.getInt("complement_id");
                if (!rs.wasNull()) {
                    complementRepo.findById(complementId).ifPresent(item::setComplement);
                }
                
                commande.addItem(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
