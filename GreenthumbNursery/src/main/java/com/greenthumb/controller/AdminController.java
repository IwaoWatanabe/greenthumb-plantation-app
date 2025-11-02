package com.greenthumb.controller;

import com.greenthumb.dao.*;
import com.greenthumb.model.*;
import com.greenthumb.view.AdminDashboardView;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.UUID;

/**
 * Controller class for handling admin operations.
 * Implements MVC pattern by coordinating between AdminDashboardView and model classes.
 */
public class AdminController {
    private AdminDashboardView dashboardView;
    private User currentUser;
    private UserDAO userDAO;
    private PlantDAO plantDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    public AdminController(User currentUser) {
        this.currentUser = currentUser;
        this.userDAO = new UserDAOImpl();
        this.plantDAO = new PlantDAOImpl();
        this.orderDAO = new OrderDAOImpl();
        this.orderItemDAO = new OrderItemDAOImpl();
        this.dashboardView = new AdminDashboardView(this);
    }

    /**
     * Show admin dashboard
     */
    public void showDashboard() {
        if (dashboardView != null) {
            dashboardView.setVisible(true);
            refreshDashboard();
        }
    }

    /**
     * Refresh dashboard data
     */
    public void refreshDashboard() {
        // This method will be called by the view to refresh data
        // Implementation will be completed when view is created
    }

    // User Management Methods
    /**
     * Create a new user
     * @param userId User ID
     * @param username Username
     * @param password Password
     * @param role User role
     * @return true if creation successful, false otherwise
     */
    public boolean createUser(String userId, String username, String password, String role) {
        try {
            // Validate input
            if (userId == null || userId.trim().isEmpty()) {
                showErrorMessage("User ID cannot be empty.");
                return false;
            }

            if (username == null || username.trim().isEmpty()) {
                showErrorMessage("Username cannot be empty.");
                return false;
            }

            if (userDAO.usernameExists(username)) {
                showErrorMessage("Username already exists.");
                return false;
            }

            // Create appropriate user type based on role
            User newUser;
            switch (role) {
                case "Admin":
                    newUser = new Admin(userId, username, password);
                    break;
                case "Staff":
                    newUser = new Staff(userId, username, password);
                    break;
                case "Customer":
                    newUser = new Customer(userId, username, password, null, null, null);
                    break;
                default:
                    showErrorMessage("Invalid user role.");
                    return false;
            }

            boolean success = userDAO.createUser(newUser);
            if (success) {
                showSuccessMessage("User created successfully.");
                return true;
            } else {
                showErrorMessage("Failed to create user.");
                return false;
            }

        } catch (Exception e) {
            showErrorMessage("Error creating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update existing user
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateUser(User user) {
        try {
            boolean success = userDAO.updateUser(user);
            if (success) {
                showSuccessMessage("User updated successfully.");
                return true;
            } else {
                showErrorMessage("Failed to update user.");
                return false;
            }
        } catch (Exception e) {
            showErrorMessage("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete user
     * @param userId User ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteUser(String userId) {
        try {
            int confirm = JOptionPane.showConfirmDialog(
                dashboardView,
                "Are you sure you want to delete this user?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userDAO.deleteUser(userId);
                if (success) {
                    showSuccessMessage("User deleted successfully.");
                    return true;
                } else {
                    showErrorMessage("Failed to delete user.");
                    return false;
                }
            }
            return false;

        } catch (Exception e) {
            showErrorMessage("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (Exception e) {
            showErrorMessage("Error retrieving users: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get users by role
     * @param role User role
     * @return List of users with specified role
     */
    public List<User> getUsersByRole(String role) {
        try {
            return userDAO.getUsersByRole(role);
        } catch (Exception e) {
            showErrorMessage("Error retrieving users by role: " + e.getMessage());
            return null;
        }
    }

    // Plant Management Methods
    /**
     * Create a new plant
     * @param plantId Plant ID
     * @param name Plant name
     * @param type Plant type
     * @param price Plant price
     * @param quantity Plant quantity
     * @param description Plant description
     * @return true if creation successful, false otherwise
     */
    public boolean createPlant(String plantId, String name, String type, double price, int quantity, String description) {
        try {
            Plant plant = new Plant(plantId, name, type, price, quantity, description);
            boolean success = plantDAO.createPlant(plant);
            
            if (success) {
                showSuccessMessage("Plant added successfully.");
                return true;
            } else {
                showErrorMessage("Failed to add plant.");
                return false;
            }

        } catch (Exception e) {
            showErrorMessage("Error adding plant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update existing plant
     * @param plant Plant object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updatePlant(Plant plant) {
        try {
            boolean success = plantDAO.updatePlant(plant);
            if (success) {
                showSuccessMessage("Plant updated successfully.");
                return true;
            } else {
                showErrorMessage("Failed to update plant.");
                return false;
            }
        } catch (Exception e) {
            showErrorMessage("Error updating plant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete plant
     * @param plantId Plant ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deletePlant(String plantId) {
        try {
            int confirm = JOptionPane.showConfirmDialog(
                dashboardView,
                "Are you sure you want to delete this plant?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = plantDAO.deletePlant(plantId);
                if (success) {
                    showSuccessMessage("Plant deleted successfully.");
                    return true;
                } else {
                    showErrorMessage("Failed to delete plant.");
                    return false;
                }
            }
            return false;

        } catch (Exception e) {
            showErrorMessage("Error deleting plant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all plants
     * @return List of all plants
     */
    public List<Plant> getAllPlants() {
        try {
            return plantDAO.getAllPlants();
        } catch (Exception e) {
            showErrorMessage("Error retrieving plants: " + e.getMessage());
            return null;
        }
    }

    /**
     * Search plants by criteria
     * @param name Plant name (can be null)
     * @param type Plant type (can be null)
     * @param minPrice Minimum price (can be null)
     * @param maxPrice Maximum price (can be null)
     * @return List of plants matching criteria
     */
    public List<Plant> searchPlants(String name, String type, Double minPrice, Double maxPrice) {
        try {
            return plantDAO.searchPlants(name, type, minPrice, maxPrice);
        } catch (Exception e) {
            showErrorMessage("Error searching plants: " + e.getMessage());
            return null;
        }
    }

    // Order Management Methods
    /**
     * Get all orders
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        try {
            return orderDAO.getAllOrders();
        } catch (Exception e) {
            showErrorMessage("Error retrieving orders: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update order status
     * @param orderId Order ID
     * @param newStatus New status
     * @return true if update successful, false otherwise
     */
    public boolean updateOrderStatus(String orderId, String newStatus) {
        try {
            boolean success = orderDAO.updateOrderStatus(orderId, newStatus);
            if (success) {
                showSuccessMessage("Order status updated successfully.");
                return true;
            } else {
                showErrorMessage("Failed to update order status.");
                return false;
            }
        } catch (Exception e) {
            showErrorMessage("Error updating order status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get orders by status
     * @param status Order status
     * @return List of orders with specified status
     */
    public List<Order> getOrdersByStatus(String status) {
        try {
            return orderDAO.getOrdersByStatus(status);
        } catch (Exception e) {
            showErrorMessage("Error retrieving orders by status: " + e.getMessage());
            return null;
        }
    }

    // Report Generation Methods
    /**
     * Generate user report
     * @return Report data as string
     */
    public String generateUserReport() {
        try {
            List<User> allUsers = userDAO.getAllUsers();
            List<User> admins = userDAO.getUsersByRole("Admin");
            List<User> staff = userDAO.getUsersByRole("Staff");
            List<User> customers = userDAO.getUsersByRole("Customer");

            StringBuilder report = new StringBuilder();
            report.append("=== USER REPORT ===\n");
            report.append("Total Users: ").append(allUsers.size()).append("\n");
            report.append("Admins: ").append(admins.size()).append("\n");
            report.append("Staff: ").append(staff.size()).append("\n");
            report.append("Customers: ").append(customers.size()).append("\n\n");

            return report.toString();

        } catch (Exception e) {
            showErrorMessage("Error generating user report: " + e.getMessage());
            return "Error generating report.";
        }
    }

    /**
     * Generate inventory report
     * @return Report data as string
     */
    public String generateInventoryReport() {
        try {
            List<Plant> allPlants = plantDAO.getAllPlants();
            List<Plant> lowStock = plantDAO.getLowStockPlants(10);

            StringBuilder report = new StringBuilder();
            report.append("=== INVENTORY REPORT ===\n");
            report.append("Total Plants: ").append(allPlants.size()).append("\n");
            report.append("Low Stock Items: ").append(lowStock.size()).append("\n\n");

            if (!lowStock.isEmpty()) {
                report.append("Low Stock Plants:\n");
                for (Plant plant : lowStock) {
                    report.append("- ").append(plant.getName())
                          .append(" (").append(plant.getQuantity()).append(" remaining)\n");
                }
            }

            return report.toString();

        } catch (Exception e) {
            showErrorMessage("Error generating inventory report: " + e.getMessage());
            return "Error generating report.";
        }
    }

    /**
     * Generate sales report
     * @return Report data as string
     */
    public String generateSalesReport() {
        try {
            List<Order> allOrders = orderDAO.getAllOrders();
            List<Order> recentOrders = orderDAO.getRecentOrders(30);

            double totalSales = 0;
            for (Order order : allOrders) {
                if (!"Cancelled".equals(order.getStatus())) {
                    totalSales += order.getTotalAmount();
                }
            }

            StringBuilder report = new StringBuilder();
            report.append("=== SALES REPORT ===\n");
            report.append("Total Orders: ").append(allOrders.size()).append("\n");
            report.append("Recent Orders (30 days): ").append(recentOrders.size()).append("\n");
            report.append("Total Sales: $").append(String.format("%.2f", totalSales)).append("\n\n");

            return report.toString();

        } catch (Exception e) {
            showErrorMessage("Error generating sales report: " + e.getMessage());
            return "Error generating report.";
        }
    }

    /**
     * Get current user
     * @return Current admin user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logout current user
     */
    public void logout() {
        if (dashboardView != null) {
            dashboardView.setVisible(false);
        }
        
        // Return to login
        LoginController loginController = new LoginController(null);
        loginController.logout();
    }

    /**
     * Show error message to user
     * @param message Error message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(dashboardView, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show success message to user
     * @param message Success message
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(dashboardView, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show info message to user
     * @param message Info message
     */
    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(dashboardView, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}

