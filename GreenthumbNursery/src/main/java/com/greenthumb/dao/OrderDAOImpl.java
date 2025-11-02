package com.greenthumb.dao;

import com.greenthumb.model.Order;
import com.greenthumb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of OrderDAO interface for database operations.
 * Handles CRUD operations for Order entities.
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (order_id, customer_id, order_date, total_amount, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, order.getOrderId());
            pstmt.setString(2, order.getCustomerId());
            pstmt.setTimestamp(3, new Timestamp(order.getOrderDate().getTime()));
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setString(5, order.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Order getOrderById(String orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createOrderFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving order by ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET customer_id = ?, order_date = ?, total_amount = ?, status = ? WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, order.getCustomerId());
            pstmt.setTimestamp(2, new Timestamp(order.getOrderDate().getTime()));
            pstmt.setDouble(3, order.getTotalAmount());
            pstmt.setString(4, order.getStatus());
            pstmt.setString(5, order.getOrderId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteOrder(String orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                if (order != null) {
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all orders: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public List<Order> getOrdersByCustomerId(String customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                if (order != null) {
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving orders by customer ID: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ? ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                if (order != null) {
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving orders by status: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public List<Order> getOrdersByDateRange(Date startDate, Date endDate) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE order_date BETWEEN ? AND ? ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            pstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                if (order != null) {
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving orders by date range: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public boolean updateOrderStatus(String orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, orderId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Order> getOrdersAboveAmount(double threshold) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE total_amount > ? ORDER BY total_amount DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, threshold);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                if (order != null) {
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving orders above amount: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public List<Order> getRecentOrders(int days) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE order_date >= DATE_SUB(NOW(), INTERVAL ? DAY) ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, days);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                if (order != null) {
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving recent orders: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public int getOrderCountByCustomer(String customerId) {
        String sql = "SELECT COUNT(*) FROM orders WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order count by customer: " + e.getMessage());
        }
        
        return 0;
    }

    @Override
    public double getTotalSalesByCustomer(String customerId) {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE customer_id = ? AND status != 'Cancelled'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total sales by customer: " + e.getMessage());
        }
        
        return 0.0;
    }

    @Override
    public List<Order> searchOrders(String customerId, String status, Date startDate, Date endDate) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        
        if (customerId != null && !customerId.trim().isEmpty()) {
            sql.append(" AND customer_id = ?");
            parameters.add(customerId);
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }
        
        if (startDate != null) {
            sql.append(" AND order_date >= ?");
            parameters.add(new Timestamp(startDate.getTime()));
        }
        
        if (endDate != null) {
            sql.append(" AND order_date <= ?");
            parameters.add(new Timestamp(endDate.getTime()));
        }
        
        sql.append(" ORDER BY order_date DESC");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                if (order != null) {
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching orders with criteria: " + e.getMessage());
        }
        
        return orders;
    }

    /**
     * Helper method to create Order object from ResultSet
     * @param rs ResultSet containing order data
     * @return Order object
     * @throws SQLException if error reading from ResultSet
     */
    private Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        String orderId = rs.getString("order_id");
        String customerId = rs.getString("customer_id");
        Date orderDate = rs.getTimestamp("order_date");
        double totalAmount = rs.getDouble("total_amount");
        String status = rs.getString("status");
        
        return new Order(orderId, customerId, orderDate, totalAmount, status);
    }
}

