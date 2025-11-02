package com.greenthumb.controller;

import com.greenthumb.dao.UserDAO;
import com.greenthumb.dao.UserDAOImpl;
import com.greenthumb.model.User;
import com.greenthumb.view.LoginView;

import javax.swing.JOptionPane;

/**
 * Controller class for handling login operations.
 * Implements MVC pattern by coordinating between LoginView and User model.
 */
public class LoginController {
    private LoginView loginView;
    private UserDAO userDAO;
    private User currentUser;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userDAO = new UserDAOImpl();
        this.currentUser = null;
    }

    /**
     * Handle user login attempt
     * @param username Username entered by user
     * @param password Password entered by user
     * @return true if login successful, false otherwise
     */
    public boolean login(String username, String password) {
        try {
            // Validate input
            if (username == null || username.trim().isEmpty()) {
                showErrorMessage("Username cannot be empty.");
                return false;
            }

            if (password == null || password.trim().isEmpty()) {
                showErrorMessage("Password cannot be empty.");
                return false;
            }

            // Authenticate user
            User user = userDAO.authenticateUser(username.trim(), password);
            
            if (user != null) {
                this.currentUser = user;
                showSuccessMessage("Login successful! Welcome, " + user.getUsername());
                
                // Navigate to appropriate dashboard based on user role
                navigateToDashboard(user);
                return true;
            } else {
                showErrorMessage("Invalid username or password. Please try again.");
                return false;
            }
            
        } catch (Exception e) {
            showErrorMessage("Login failed due to system error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Handle user logout
     */
    public void logout() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
            showInfoMessage("You have been logged out successfully.");
            
            // Return to login view
            if (loginView != null) {
                loginView.clearFields();
                loginView.setVisible(true);
            }
        }
    }

    /**
     * Navigate to appropriate dashboard based on user role
     * @param user Authenticated user
     */
    private void navigateToDashboard(User user) {
        try {
            switch (user.getRole()) {
                case "Admin":
                    // Navigate to Admin Dashboard
                    AdminController adminController = new AdminController(user);
                    adminController.showDashboard();
                    break;
                    
                case "Staff":
                    // Navigate to Staff Dashboard
                    StaffController staffController = new StaffController(user);
                    staffController.showDashboard();
                    break;
                    
                case "Customer":
                    // Navigate to Customer Dashboard
                    CustomerController customerController = new CustomerController(user);
                    customerController.showDashboard();
                    break;
                    
                default:
                    showErrorMessage("Unknown user role: " + user.getRole());
                    return;
            }
            
            // Hide login view after successful navigation
            if (loginView != null) {
                loginView.setVisible(false);
            }
            
        } catch (Exception e) {
            showErrorMessage("Failed to open dashboard: " + e.getMessage());
        }
    }

    /**
     * Validate user registration (for future implementation)
     * @param username Username
     * @param password Password
     * @param confirmPassword Confirm password
     * @param role User role
     * @return true if validation successful, false otherwise
     */
    public boolean validateRegistration(String username, String password, String confirmPassword, String role) {
        // Check if username already exists
        if (userDAO.usernameExists(username)) {
            showErrorMessage("Username already exists. Please choose a different username.");
            return false;
        }

        // Validate password
        if (password == null || password.length() < 6) {
            showErrorMessage("Password must be at least 6 characters long.");
            return false;
        }

        // Check password confirmation
        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match.");
            return false;
        }

        // Validate role
        if (role == null || (!role.equals("Admin") && !role.equals("Staff") && !role.equals("Customer"))) {
            showErrorMessage("Invalid user role selected.");
            return false;
        }

        return true;
    }

    /**
     * Change user password
     * @param currentPassword Current password
     * @param newPassword New password
     * @param confirmPassword Confirm new password
     * @return true if password change successful, false otherwise
     */
    public boolean changePassword(String currentPassword, String newPassword, String confirmPassword) {
        if (currentUser == null) {
            showErrorMessage("No user is currently logged in.");
            return false;
        }

        // Verify current password
        User verifyUser = userDAO.authenticateUser(currentUser.getUsername(), currentPassword);
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
        boolean success = userDAO.updatePassword(currentUser.getUserId(), newPassword);
        if (success) {
            currentUser.setPassword(newPassword);
            showSuccessMessage("Password changed successfully.");
            return true;
        } else {
            showErrorMessage("Failed to change password. Please try again.");
            return false;
        }
    }

    /**
     * Get current logged-in user
     * @return Current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if user is logged in
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Show error message to user
     * @param message Error message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(loginView, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show success message to user
     * @param message Success message
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(loginView, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show info message to user
     * @param message Info message
     */
    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(loginView, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Reset login form
     */
    public void resetForm() {
        if (loginView != null) {
            loginView.clearFields();
        }
    }
}

