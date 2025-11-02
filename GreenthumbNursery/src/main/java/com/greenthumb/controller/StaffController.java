package com.greenthumb.controller;

import com.greenthumb.dao.*;
import com.greenthumb.model.*;
import com.greenthumb.view.StaffDashboardView;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Controller class for handling staff operations.
 * Implements MVC pattern by coordinating between StaffDashboardView and model classes.
 */
public class StaffController {
    private StaffDashboardView dashboardView;
    private User currentUser;
    private PlantDAO plantDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private UserDAO userDAO;

    public StaffController(User currentUser) {
        this.currentUser = currentUser;
        this.plantDAO = new PlantDAOImpl();
        this.orderDAO = new OrderDAOImpl();
        this.orderItemDAO = new OrderItemDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.dashboardView = new StaffDashboardView(this);
    }

    /**
     * Show staff dashboard
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

    // Plant Inventory Management Methods
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
     * Update plant information
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
     * Update plant quantity
     * @param plantId Plant ID
     * @param newQuantity New quantity
     * @return true if update successful, false otherwise
     */
    public boolean updatePlantQuantity(String plantId, int newQuantity) {
        try {
            if (newQuantity < 0) {
                showErrorMessage("Quantity cannot be negative.");
                return false;
            }

            boolean success = plantDAO.updatePlantQuantity(plantId, newQuantity);
            if (success) {
                showSuccessMessage("Plant quantity updated successfully.");
                return true;
            } else {
                showErrorMessage("Failed to update plant quantity.");
                return false;
            }
        } catch (Exception e) {
            showErrorMessage("Error updating plant quantity: " + e.getMessage());
            return false;
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

    /**
     * Get low stock plants
     * @param threshold Stock threshold
     * @return List of plants with low stock
     */
    public List<Plant> getLowStockPlants(int threshold) {
        try {
            return plantDAO.getLowStockPlants(threshold);
        } catch (Exception e) {
            showErrorMessage("Error retrieving low stock plants: " + e.getMessage());
            return null;
        }
    }

    // Order Processing Methods
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

    /**
     * Update order status
     * @param orderId Order ID
     * @param newStatus New status
     * @return true if update successful, false otherwise
     */
    public boolean updateOrderStatus(String orderId, String newStatus) {
        try {
            // Validate status transition
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                showErrorMessage("Order not found.");
                return false;
            }

            if (!order.updateStatus(newStatus)) {
                showErrorMessage("Invalid status transition from " + order.getStatus() + " to " + newStatus);
                return false;
            }

            boolean success = orderDAO.updateOrderStatus(orderId, newStatus);
            if (success) {
                showSuccessMessage("Order status updated successfully.");
                
                // If order is being processed, update plant quantities
                if ("Processing".equals(newStatus)) {
                    updateInventoryForOrder(orderId);
                }
                
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
     * Get order details with items
     * @param orderId Order ID
     * @return List of order items with plant details
     */
    public List<OrderItem> getOrderDetails(String orderId) {
        try {
            return orderItemDAO.getOrderItemsWithPlantDetails(orderId);
        } catch (Exception e) {
            showErrorMessage("Error retrieving order details: " + e.getMessage());
            return null;
        }
    }

    /**
     * Process order (change status to Processing and update inventory)
     * @param orderId Order ID
     * @return true if processing successful, false otherwise
     */
    public boolean processOrder(String orderId) {
        try {
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                showErrorMessage("Order not found.");
                return false;
            }

            if (!"Pending".equals(order.getStatus())) {
                showErrorMessage("Only pending orders can be processed.");
                return false;
            }

            // Check inventory availability
            List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
            for (OrderItem item : orderItems) {
                Plant plant = plantDAO.getPlantById(item.getPlantId());
                if (plant == null || !plant.isAvailable(item.getQuantity())) {
                    showErrorMessage("Insufficient stock for plant: " + (plant != null ? plant.getName() : item.getPlantId()));
                    return false;
                }
            }

            // Update order status
            boolean success = updateOrderStatus(orderId, "Processing");
            if (success) {
                showSuccessMessage("Order processed successfully.");
                return true;
            }
            
            return false;

        } catch (Exception e) {
            showErrorMessage("Error processing order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update inventory when order is processed
     * @param orderId Order ID
     */
    private void updateInventoryForOrder(String orderId) {
        try {
            List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
            
            for (OrderItem item : orderItems) {
                Plant plant = plantDAO.getPlantById(item.getPlantId());
                if (plant != null) {
                    int newQuantity = plant.getQuantity() - item.getQuantity();
                    plantDAO.updatePlantQuantity(plant.getPlantId(), newQuantity);
                }
            }
            
        } catch (Exception e) {
            showErrorMessage("Error updating inventory: " + e.getMessage());
        }
    }

    // Customer Information Management
    /**
     * Get customer information
     * @param customerId Customer ID
     * @return Customer object
     */
    public Customer getCustomerInfo(String customerId) {
        try {
            User user = userDAO.getUserById(customerId);
            if (user instanceof Customer) {
                return (Customer) user;
            }
            return null;
        } catch (Exception e) {
            showErrorMessage("Error retrieving customer information: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update customer information
     * @param customer Customer object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateCustomerInfo(Customer customer) {
        try {
            boolean success = userDAO.updateUser(customer);
            if (success) {
                showSuccessMessage("Customer information updated successfully.");
                return true;
            } else {
                showErrorMessage("Failed to update customer information.");
                return false;
            }
        } catch (Exception e) {
            showErrorMessage("Error updating customer information: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all customers
     * @return List of all customers
     */
    public List<User> getAllCustomers() {
        try {
            return userDAO.getUsersByRole("Customer");
        } catch (Exception e) {
            showErrorMessage("Error retrieving customers: " + e.getMessage());
            return null;
        }
    }

    // Report Generation Methods
    /**
     * Generate inventory report
     * @return Report data as string
     */
    public String generateInventoryReport() {
        try {
            List<Plant> allPlants = plantDAO.getAllPlants();
            List<Plant> lowStock = plantDAO.getLowStockPlants(10);
            List<Plant> availablePlants = plantDAO.getAvailablePlants();

            StringBuilder report = new StringBuilder();
            report.append("=== INVENTORY REPORT ===\n");
            report.append("Generated by: ").append(currentUser.getUsername()).append("\n");
            report.append("Total Plants: ").append(allPlants.size()).append("\n");
            report.append("Available Plants: ").append(availablePlants.size()).append("\n");
            report.append("Low Stock Items: ").append(lowStock.size()).append("\n\n");

            if (!lowStock.isEmpty()) {
                report.append("Low Stock Plants:\n");
                for (Plant plant : lowStock) {
                    report.append("- ").append(plant.getName())
                          .append(" (").append(plant.getQuantity()).append(" remaining)\n");
                }
                report.append("\n");
            }

            return report.toString();

        } catch (Exception e) {
            showErrorMessage("Error generating inventory report: " + e.getMessage());
            return "Error generating report.";
        }
    }

    /**
     * Generate order processing report
     * @return Report data as string
     */
    public String generateOrderReport() {
        try {
            List<Order> pendingOrders = orderDAO.getOrdersByStatus("Pending");
            List<Order> processingOrders = orderDAO.getOrdersByStatus("Processing");
            List<Order> shippedOrders = orderDAO.getOrdersByStatus("Shipped");
            List<Order> recentOrders = orderDAO.getRecentOrders(7);

            StringBuilder report = new StringBuilder();
            report.append("=== ORDER PROCESSING REPORT ===\n");
            report.append("Generated by: ").append(currentUser.getUsername()).append("\n");
            report.append("Pending Orders: ").append(pendingOrders.size()).append("\n");
            report.append("Processing Orders: ").append(processingOrders.size()).append("\n");
            report.append("Shipped Orders: ").append(shippedOrders.size()).append("\n");
            report.append("Recent Orders (7 days): ").append(recentOrders.size()).append("\n\n");

            return report.toString();

        } catch (Exception e) {
            showErrorMessage("Error generating order report: " + e.getMessage());
            return "Error generating report.";
        }
    }

    /**
     * Get current user
     * @return Current staff user
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

