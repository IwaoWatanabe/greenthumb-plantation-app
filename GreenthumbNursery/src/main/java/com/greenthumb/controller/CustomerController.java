package com.greenthumb.controller;

import com.greenthumb.dao.*;
import com.greenthumb.model.*;
import com.greenthumb.view.CustomerDashboardView;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Controller class for handling customer operations.
 * Implements MVC pattern by coordinating between CustomerDashboardView and model classes.
 */
public class CustomerController {
    private CustomerDashboardView dashboardView;
    private Customer currentCustomer;
    private PlantDAO plantDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private UserDAO userDAO;
    private List<OrderItem> shoppingCart;

    public CustomerController(User currentUser) {
        if (currentUser instanceof Customer) {
            this.currentCustomer = (Customer) currentUser;
        } else {
            throw new IllegalArgumentException("User must be a Customer");
        }
        
        this.plantDAO = new PlantDAOImpl();
        this.orderDAO = new OrderDAOImpl();
        this.orderItemDAO = new OrderItemDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.shoppingCart = new ArrayList<>();
        this.dashboardView = new CustomerDashboardView(this);
    }

    /**
     * Show customer dashboard
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

    // Plant Browsing Methods
    /**
     * Get all available plants
     * @return List of available plants
     */
    public List<Plant> getAvailablePlants() {
        try {
            return plantDAO.getAvailablePlants();
        } catch (Exception e) {
            showErrorMessage("Error retrieving plants: " + e.getMessage());
            return new ArrayList<>();
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
            return new ArrayList<>();
        }
    }

    /**
     * Get plant by ID
     * @param plantId Plant ID
     * @return Plant object
     */
    public Plant getPlantById(String plantId) {
        try {
            return plantDAO.getPlantById(plantId);
        } catch (Exception e) {
            showErrorMessage("Error retrieving plant: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get plants by type
     * @param type Plant type
     * @return List of plants of specified type
     */
    public List<Plant> getPlantsByType(String type) {
        try {
            return plantDAO.searchPlantsByType(type);
        } catch (Exception e) {
            showErrorMessage("Error retrieving plants by type: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Shopping Cart Methods
    /**
     * Add item to shopping cart
     * @param plantId Plant ID
     * @param quantity Quantity to add
     * @return true if addition successful, false otherwise
     */
    public boolean addToCart(String plantId, int quantity) {
        try {
            if (quantity <= 0) {
                showErrorMessage("Quantity must be greater than 0.");
                return false;
            }

            Plant plant = plantDAO.getPlantById(plantId);
            if (plant == null) {
                showErrorMessage("Plant not found.");
                return false;
            }

            if (!plant.isAvailable(quantity)) {
                showErrorMessage("Insufficient stock. Available: " + plant.getQuantity());
                return false;
            }

            // Check if item already exists in cart
            for (OrderItem item : shoppingCart) {
                if (item.getPlantId().equals(plantId)) {
                    int newQuantity = item.getQuantity() + quantity;
                    if (!plant.isAvailable(newQuantity)) {
                        showErrorMessage("Cannot add more items. Total would exceed available stock.");
                        return false;
                    }
                    item.setQuantity(newQuantity);
                    item.calculateSubtotal();
                    showSuccessMessage("Cart updated successfully.");
                    return true;
                }
            }

            // Add new item to cart
            String orderItemId = "item_" + UUID.randomUUID().toString().substring(0, 8);
            OrderItem newItem = new OrderItem(orderItemId, null, plant, quantity);
            shoppingCart.add(newItem);
            
            showSuccessMessage("Item added to cart successfully.");
            return true;

        } catch (Exception e) {
            showErrorMessage("Error adding item to cart: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove item from shopping cart
     * @param plantId Plant ID
     * @return true if removal successful, false otherwise
     */
    public boolean removeFromCart(String plantId) {
        try {
            boolean removed = shoppingCart.removeIf(item -> item.getPlantId().equals(plantId));
            if (removed) {
                showSuccessMessage("Item removed from cart.");
                return true;
            } else {
                showErrorMessage("Item not found in cart.");
                return false;
            }
        } catch (Exception e) {
            showErrorMessage("Error removing item from cart: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update item quantity in shopping cart
     * @param plantId Plant ID
     * @param newQuantity New quantity
     * @return true if update successful, false otherwise
     */
    public boolean updateCartItemQuantity(String plantId, int newQuantity) {
        try {
            if (newQuantity <= 0) {
                return removeFromCart(plantId);
            }

            Plant plant = plantDAO.getPlantById(plantId);
            if (plant == null || !plant.isAvailable(newQuantity)) {
                showErrorMessage("Insufficient stock for requested quantity.");
                return false;
            }

            for (OrderItem item : shoppingCart) {
                if (item.getPlantId().equals(plantId)) {
                    item.setQuantity(newQuantity);
                    item.calculateSubtotal();
                    showSuccessMessage("Cart updated successfully.");
                    return true;
                }
            }

            showErrorMessage("Item not found in cart.");
            return false;

        } catch (Exception e) {
            showErrorMessage("Error updating cart: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get shopping cart items
     * @return List of items in shopping cart
     */
    public List<OrderItem> getShoppingCart() {
        return new ArrayList<>(shoppingCart);
    }

    /**
     * Calculate cart total
     * @return Total amount of items in cart
     */
    public double calculateCartTotal() {
        double total = 0.0;
        for (OrderItem item : shoppingCart) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Clear shopping cart
     */
    public void clearCart() {
        shoppingCart.clear();
        showInfoMessage("Shopping cart cleared.");
    }

    // Order Management Methods
    /**
     * Place order with current cart items
     * @return true if order placement successful, false otherwise
     */
    public boolean placeOrder() {
        try {
            if (shoppingCart.isEmpty()) {
                showErrorMessage("Shopping cart is empty.");
                return false;
            }

            // Verify stock availability before placing order
            for (OrderItem item : shoppingCart) {
                Plant plant = plantDAO.getPlantById(item.getPlantId());
                if (plant == null || !plant.isAvailable(item.getQuantity())) {
                    showErrorMessage("Insufficient stock for: " + (plant != null ? plant.getName() : item.getPlantId()));
                    return false;
                }
            }

            // Create order
            String orderId = "order_" + UUID.randomUUID().toString().substring(0, 8);
            Order order = new Order(orderId, currentCustomer.getCustomerId(), new Date(), calculateCartTotal(), Order.STATUS_PENDING);

            boolean orderCreated = orderDAO.createOrder(order);
            if (!orderCreated) {
                showErrorMessage("Failed to create order.");
                return false;
            }

            // Create order items
            for (OrderItem item : shoppingCart) {
                item.setOrderId(orderId);
                item.setOrderItemId("item_" + UUID.randomUUID().toString().substring(0, 8));
                
                boolean itemCreated = orderItemDAO.createOrderItem(item);
                if (!itemCreated) {
                    showErrorMessage("Failed to create order item for: " + item.getPlant().getName());
                    // Consider rolling back the order creation here
                    return false;
                }
            }

            // Clear cart after successful order
            clearCart();
            showSuccessMessage("Order placed successfully! Order ID: " + orderId);
            return true;

        } catch (Exception e) {
            showErrorMessage("Error placing order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get customer's order history
     * @return List of customer's orders
     */
    public List<Order> getOrderHistory() {
        try {
            return orderDAO.getOrdersByCustomerId(currentCustomer.getCustomerId());
        } catch (Exception e) {
            showErrorMessage("Error retrieving order history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get order details
     * @param orderId Order ID
     * @return List of order items with plant details
     */
    public List<OrderItem> getOrderDetails(String orderId) {
        try {
            return orderItemDAO.getOrderItemsWithPlantDetails(orderId);
        } catch (Exception e) {
            showErrorMessage("Error retrieving order details: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Cancel order (if possible)
     * @param orderId Order ID
     * @return true if cancellation successful, false otherwise
     */
    public boolean cancelOrder(String orderId) {
        try {
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                showErrorMessage("Order not found.");
                return false;
            }

            if (!order.canBeCancelled()) {
                showErrorMessage("Order cannot be cancelled. Current status: " + order.getStatus());
                return false;
            }

            int confirm = JOptionPane.showConfirmDialog(
                dashboardView,
                "Are you sure you want to cancel this order?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = orderDAO.updateOrderStatus(orderId, Order.STATUS_CANCELLED);
                if (success) {
                    showSuccessMessage("Order cancelled successfully.");
                    return true;
                } else {
                    showErrorMessage("Failed to cancel order.");
                    return false;
                }
            }
            return false;

        } catch (Exception e) {
            showErrorMessage("Error cancelling order: " + e.getMessage());
            return false;
        }
    }

    // Profile Management Methods
    /**
     * Update customer profile
     * @param address New address
     * @param phone New phone number
     * @return true if update successful, false otherwise
     */
    public boolean updateProfile(String address, String phone) {
        try {
            currentCustomer.setAddress(address);
            currentCustomer.setPhone(phone);
            
            boolean success = userDAO.updateUser(currentCustomer);
            if (success) {
                showSuccessMessage("Profile updated successfully.");
                return true;
            } else {
                showErrorMessage("Failed to update profile.");
                return false;
            }
        } catch (Exception e) {
            showErrorMessage("Error updating profile: " + e.getMessage());
            return false;
        }
    }

    /**
     * Change password
     * @param currentPassword Current password
     * @param newPassword New password
     * @param confirmPassword Confirm new password
     * @return true if password change successful, false otherwise
     */
    public boolean changePassword(String currentPassword, String newPassword, String confirmPassword) {
        try {
            // Verify current password
            User verifyUser = userDAO.authenticateUser(currentCustomer.getUsername(), currentPassword);
            if (verifyUser == null) {
                showErrorMessage("Current password is incorrect.");
                return false;
            }

            // Validate new password
            if (newPassword == null || newPassword.length() < 6) {
                showErrorMessage("New password must be at least 6 characters long.");
                return false;
            }

            // Check password confirmation
            if (!newPassword.equals(confirmPassword)) {
                showErrorMessage("New passwords do not match.");
                return false;
            }

            // Update password
            boolean success = userDAO.updatePassword(currentCustomer.getUserId(), newPassword);
            if (success) {
                currentCustomer.setPassword(newPassword);
                showSuccessMessage("Password changed successfully.");
                return true;
            } else {
                showErrorMessage("Failed to change password.");
                return false;
            }

        } catch (Exception e) {
            showErrorMessage("Error changing password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get current customer
     * @return Current customer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * Logout current user
     */
    public void logout() {
        if (dashboardView != null) {
            dashboardView.setVisible(false);
        }
        
        // Clear cart on logout
        clearCart();
        
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

