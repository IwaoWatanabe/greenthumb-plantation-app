package com.greenthumb.dao;

import com.greenthumb.model.User;
import com.greenthumb.model.Admin;
import com.greenthumb.model.Staff;
import com.greenthumb.model.Customer;
import com.greenthumb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserDAO interface for database operations.
 * Handles CRUD operations for User entities.
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (user_id, username, password, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = createUserFromResultSet(rs);
                if (user != null) {
                    users.add(user);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        
        return users;
    }

    @Override
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = createUserFromResultSet(rs);
                if (user != null) {
                    users.add(user);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving users by role: " + e.getMessage());
        }
        
        return users;
    }

    @Override
    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
        }
        
        return false;
    }

    @Override
    public boolean updatePassword(String userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to create User object from ResultSet
     * @param rs ResultSet containing user data
     * @return User object (Admin, Staff, or Customer based on role)
     * @throws SQLException if error reading from ResultSet
     */
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        String userId = rs.getString("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String role = rs.getString("role");
        
        // Create appropriate user type based on role (Polymorphism)
        switch (role) {
            case "Admin":
                return new Admin(userId, username, password);
            case "Staff":
                return new Staff(userId, username, password);
            case "Customer":
                // For customers, we need to get additional info from customers table
                return getCustomerDetails(userId, username, password);
            default:
                System.err.println("Unknown user role: " + role);
                return null;
        }
    }

    /**
     * Helper method to get customer details from customers table
     * @param userId User ID
     * @param username Username
     * @param password Password
     * @return Customer object with complete details
     */
    private Customer getCustomerDetails(String userId, String username, String password) {
        String sql = "SELECT * FROM customers WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String customerId = rs.getString("customer_id");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                
                return new Customer(userId, username, password, customerId, address, phone);
            } else {
                // If no customer details found, create basic customer
                return new Customer(userId, username, password, null, null, null);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customer details: " + e.getMessage());
            return new Customer(userId, username, password, null, null, null);
        }
    }
}

