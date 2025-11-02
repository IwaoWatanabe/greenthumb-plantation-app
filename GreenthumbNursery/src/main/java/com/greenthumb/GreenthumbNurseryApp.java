package com.greenthumb;

import com.greenthumb.view.LoginView;
import com.greenthumb.util.DBConnection;

import javax.swing.*;
import java.sql.Connection;

/**
 * Main application class for Greenthumb Nursery Management System.
 * This class serves as the entry point and integrates all components.
 */
public class GreenthumbNurseryApp {
    
    /**
     * Main method - entry point of the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set system properties for better UI appearance
        System.setProperty("java.awt.headless", "false");
        
        // Set look and feel to system default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel. Using default.");
        }
        
        // Initialize application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initializeApplication();
            }
        });
    }
    
    /**
     * Initialize the application
     */
    private static void initializeApplication() {
        try {
            // Show splash screen
            showSplashScreen();
            
            // Test database connection
            if (!testDatabaseConnection()) {
                showDatabaseErrorDialog();
                return;
            }
            
            // Hide splash screen and show login
            showLoginScreen();
            
        } catch (Exception e) {
            showErrorDialog("Application Initialization Error", 
                "Failed to initialize application: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Show splash screen
     */
    private static void showSplashScreen() {
        JWindow splashScreen = new JWindow();
        
        // Create splash content
        JPanel splashPanel = new JPanel();
        splashPanel.setLayout(new BoxLayout(splashPanel, BoxLayout.Y_AXIS));
        splashPanel.setBackground(new java.awt.Color(34, 139, 34));
        splashPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Title
        JLabel titleLabel = new JLabel("Greenthumb Nursery");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 32));
        titleLabel.setForeground(java.awt.Color.WHITE);
        titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
        subtitleLabel.setForeground(java.awt.Color.WHITE);
        subtitleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        // Version
        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        versionLabel.setForeground(java.awt.Color.LIGHT_GRAY);
        versionLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        // Loading label
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 14));
        loadingLabel.setForeground(java.awt.Color.WHITE);
        loadingLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        // Add components
        splashPanel.add(Box.createVerticalGlue());
        splashPanel.add(titleLabel);
        splashPanel.add(Box.createVerticalStrut(10));
        splashPanel.add(subtitleLabel);
        splashPanel.add(Box.createVerticalStrut(20));
        splashPanel.add(versionLabel);
        splashPanel.add(Box.createVerticalGlue());
        splashPanel.add(loadingLabel);
        splashPanel.add(Box.createVerticalStrut(20));
        
        splashScreen.add(splashPanel);
        splashScreen.setSize(400, 300);
        splashScreen.setLocationRelativeTo(null);
        splashScreen.setVisible(true);
        
        // Show splash for 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        splashScreen.dispose();
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    private static boolean testDatabaseConnection() {
        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) {
                connection.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Show database error dialog
     */
    private static void showDatabaseErrorDialog() {
        String message = "Failed to connect to the database.\n\n" +
                        "Please ensure that:\n" +
                        "1. MySQL server is running\n" +
                        "2. Database 'greenthumb_nursery' exists\n" +
                        "3. Database credentials are correct\n" +
                        "4. MySQL JDBC driver is available\n\n" +
                        "Check the database configuration in DBConnection.java";
        
        JOptionPane.showMessageDialog(
            null,
            message,
            "Database Connection Error",
            JOptionPane.ERROR_MESSAGE
        );
        
        // Ask user if they want to continue anyway (for testing UI)
        int option = JOptionPane.showConfirmDialog(
            null,
            "Do you want to continue without database connection?\n" +
            "(Application will have limited functionality)",
            "Continue Without Database?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            showLoginScreen();
        } else {
            System.exit(1);
        }
    }
    
    /**
     * Show login screen
     */
    private static void showLoginScreen() {
        try {
            LoginView loginView = new LoginView();
            loginView.showView();
        } catch (Exception e) {
            showErrorDialog("Login Screen Error", 
                "Failed to show login screen: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Show error dialog
     * @param title Dialog title
     * @param message Error message
     */
    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Show information dialog
     * @param title Dialog title
     * @param message Information message
     */
    public static void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Show confirmation dialog
     * @param title Dialog title
     * @param message Confirmation message
     * @return true if user confirmed, false otherwise
     */
    public static boolean showConfirmDialog(String title, String message) {
        int option = JOptionPane.showConfirmDialog(
            null,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return option == JOptionPane.YES_OPTION;
    }
    
    /**
     * Get application information
     * @return Application info string
     */
    public static String getApplicationInfo() {
        return "Greenthumb Nursery Management System v1.0\n" +
               "A comprehensive desktop application for nursery management\n" +
               "Built with Java Swing and MySQL\n" +
               "© 2025 Greenthumb Nursery";
    }
    
    /**
     * Exit application gracefully
     */
    public static void exitApplication() {
        int option = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            // Cleanup resources if needed
            try {
                // Close any open database connections
                DBConnection.closeAllConnections();
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
            
            System.exit(0);
        }
    }
}

