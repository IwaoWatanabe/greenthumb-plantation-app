package com.greenthumb.dao;

import com.greenthumb.model.OrderItem;
import com.greenthumb.model.Plant;
import com.greenthumb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of OrderItemDAO interface for database operations.
 * Handles CRUD operations for OrderItem entities.
 */
public class OrderItemDAOImpl implements OrderItemDAO {

    private PlantDAO plantDAO = new PlantDAOImpl();

    @Override
    public boolean createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_item_id, order_id, plant_id, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderItem.getOrderItemId());
            pstmt.setString(2, orderItem.getOrderId());
            pstmt.setString(3, orderItem.getPlantId());
            pstmt.setInt(4, orderItem.getQuantity());
            pstmt.setDouble(5, orderItem.getSubtotal());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating order item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public OrderItem getOrderItemById(String orderItemId) {
        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderItemId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createOrderItemFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving order item by ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE order_items SET order_id = ?, plant_id = ?, quantity = ?, subtotal = ? WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderItem.getOrderId());
            pstmt.setString(2, orderItem.getPlantId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setDouble(4, orderItem.getSubtotal());
            pstmt.setString(5, orderItem.getOrderItemId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteOrderItem(String orderItemId) {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderItemId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting order item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                OrderItem orderItem = createOrderItemFromResultSet(rs);
                if (orderItem != null) {
                    orderItems.add(orderItem);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all order items: " + e.getMessage());
        }
        
        return orderItems;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(String orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                OrderItem orderItem = createOrderItemFromResultSet(rs);
                if (orderItem != null) {
                    orderItems.add(orderItem);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving order items by order ID: " + e.getMessage());
        }
        
        return orderItems;
    }

    @Override
    public List<OrderItem> getOrderItemsByPlantId(String plantId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE plant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plantId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                OrderItem orderItem = createOrderItemFromResultSet(rs);
                if (orderItem != null) {
                    orderItems.add(orderItem);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving order items by plant ID: " + e.getMessage());
        }
        
        return orderItems;
    }

    @Override
    public boolean deleteOrderItemsByOrderId(String orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting order items by order ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getTotalQuantitySoldByPlant(String plantId) {
        String sql = "SELECT SUM(quantity) FROM order_items WHERE plant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plantId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total quantity sold by plant: " + e.getMessage());
        }
        
        return 0;
    }

    @Override
    public double getTotalRevenueByPlant(String plantId) {
        String sql = "SELECT SUM(subtotal) FROM order_items WHERE plant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plantId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total revenue by plant: " + e.getMessage());
        }
        
        return 0.0;
    }

    @Override
    public List<OrderItem> getOrderItemsAboveQuantity(int threshold) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE quantity > ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, threshold);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                OrderItem orderItem = createOrderItemFromResultSet(rs);
                if (orderItem != null) {
                    orderItems.add(orderItem);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving order items above quantity: " + e.getMessage());
        }
        
        return orderItems;
    }

    @Override
    public boolean updateOrderItemQuantity(String orderItemId, int newQuantity) {
        // First get the order item to recalculate subtotal
        OrderItem orderItem = getOrderItemById(orderItemId);
        if (orderItem == null) {
            return false;
        }
        
        // Get plant to calculate new subtotal
        Plant plant = plantDAO.getPlantById(orderItem.getPlantId());
        if (plant == null) {
            return false;
        }
        
        double newSubtotal = plant.getPrice() * newQuantity;
        
        String sql = "UPDATE order_items SET quantity = ?, subtotal = ? WHERE order_item_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newQuantity);
            pstmt.setDouble(2, newSubtotal);
            pstmt.setString(3, orderItemId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order item quantity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsWithPlantDetails(String orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT oi.*, p.name, p.type, p.price, p.description " +
                    "FROM order_items oi " +
                    "JOIN plants p ON oi.plant_id = p.plant_id " +
                    "WHERE oi.order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                OrderItem orderItem = createOrderItemFromResultSet(rs);
                if (orderItem != null) {
                    // Create plant object with details from join
                    Plant plant = new Plant(
                        rs.getString("plant_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        0, // Quantity not needed for order item display
                        rs.getString("description")
                    );
                    orderItem.setPlant(plant);
                    orderItems.add(orderItem);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving order items with plant details: " + e.getMessage());
        }
        
        return orderItems;
    }

    @Override
    public double calculateOrderTotal(String orderId) {
        String sql = "SELECT SUM(subtotal) FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating order total: " + e.getMessage());
        }
        
        return 0.0;
    }

    /**
     * Helper method to create OrderItem object from ResultSet
     * @param rs ResultSet containing order item data
     * @return OrderItem object
     * @throws SQLException if error reading from ResultSet
     */
    private OrderItem createOrderItemFromResultSet(ResultSet rs) throws SQLException {
        String orderItemId = rs.getString("order_item_id");
        String orderId = rs.getString("order_id");
        String plantId = rs.getString("plant_id");
        int quantity = rs.getInt("quantity");
        double subtotal = rs.getDouble("subtotal");
        
        OrderItem orderItem = new OrderItem(orderItemId, orderId, plantId, quantity, subtotal);
        
        // Try to load plant details if available
        Plant plant = plantDAO.getPlantById(plantId);
        if (plant != null) {
            orderItem.setPlant(plant);
        }
        
        return orderItem;
    }
}

