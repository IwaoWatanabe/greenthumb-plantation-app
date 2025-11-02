package com.greenthumb.dao;

import com.greenthumb.model.Plant;
import com.greenthumb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PlantDAO interface for database operations.
 * Handles CRUD operations for Plant entities.
 */
public class PlantDAOImpl implements PlantDAO {

    @Override
    public boolean createPlant(Plant plant) {
        String sql = "INSERT INTO plants (plant_id, name, type, price, quantity, description) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plant.getPlantId());
            pstmt.setString(2, plant.getName());
            pstmt.setString(3, plant.getType());
            pstmt.setDouble(4, plant.getPrice());
            pstmt.setInt(5, plant.getQuantity());
            pstmt.setString(6, plant.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating plant: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Plant getPlantById(String plantId) {
        String sql = "SELECT * FROM plants WHERE plant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plantId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createPlantFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving plant by ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean updatePlant(Plant plant) {
        String sql = "UPDATE plants SET name = ?, type = ?, price = ?, quantity = ?, description = ? WHERE plant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plant.getName());
            pstmt.setString(2, plant.getType());
            pstmt.setDouble(3, plant.getPrice());
            pstmt.setInt(4, plant.getQuantity());
            pstmt.setString(5, plant.getDescription());
            pstmt.setString(6, plant.getPlantId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating plant: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletePlant(String plantId) {
        String sql = "DELETE FROM plants WHERE plant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plantId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting plant: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Plant> getAllPlants() {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Plant plant = createPlantFromResultSet(rs);
                if (plant != null) {
                    plants.add(plant);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all plants: " + e.getMessage());
        }
        
        return plants;
    }

    @Override
    public List<Plant> searchPlantsByName(String name) {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants WHERE name LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Plant plant = createPlantFromResultSet(rs);
                if (plant != null) {
                    plants.add(plant);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching plants by name: " + e.getMessage());
        }
        
        return plants;
    }

    @Override
    public List<Plant> searchPlantsByType(String type) {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants WHERE type = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Plant plant = createPlantFromResultSet(rs);
                if (plant != null) {
                    plants.add(plant);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching plants by type: " + e.getMessage());
        }
        
        return plants;
    }

    @Override
    public List<Plant> searchPlantsByPriceRange(double minPrice, double maxPrice) {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants WHERE price BETWEEN ? AND ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, minPrice);
            pstmt.setDouble(2, maxPrice);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Plant plant = createPlantFromResultSet(rs);
                if (plant != null) {
                    plants.add(plant);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching plants by price range: " + e.getMessage());
        }
        
        return plants;
    }

    @Override
    public List<Plant> getLowStockPlants(int threshold) {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants WHERE quantity < ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, threshold);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Plant plant = createPlantFromResultSet(rs);
                if (plant != null) {
                    plants.add(plant);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving low stock plants: " + e.getMessage());
        }
        
        return plants;
    }

    @Override
    public boolean updatePlantQuantity(String plantId, int newQuantity) {
        String sql = "UPDATE plants SET quantity = ? WHERE plant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newQuantity);
            pstmt.setString(2, plantId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating plant quantity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Plant> getAvailablePlants() {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants WHERE quantity > 0";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Plant plant = createPlantFromResultSet(rs);
                if (plant != null) {
                    plants.add(plant);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving available plants: " + e.getMessage());
        }
        
        return plants;
    }

    @Override
    public List<Plant> searchPlants(String name, String type, Double minPrice, Double maxPrice) {
        List<Plant> plants = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM plants WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            parameters.add("%" + name + "%");
        }
        
        if (type != null && !type.trim().isEmpty()) {
            sql.append(" AND type = ?");
            parameters.add(type);
        }
        
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }
        
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Plant plant = createPlantFromResultSet(rs);
                if (plant != null) {
                    plants.add(plant);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching plants with criteria: " + e.getMessage());
        }
        
        return plants;
    }

    /**
     * Helper method to create Plant object from ResultSet
     * @param rs ResultSet containing plant data
     * @return Plant object
     * @throws SQLException if error reading from ResultSet
     */
    private Plant createPlantFromResultSet(ResultSet rs) throws SQLException {
        String plantId = rs.getString("plant_id");
        String name = rs.getString("name");
        String type = rs.getString("type");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");
        String description = rs.getString("description");
        
        return new Plant(plantId, name, type, price, quantity, description);
    }
}

