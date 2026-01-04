package com.greenthumb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class for Greenthumb Nursery application.
 * Implements Singleton pattern for database connection management.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/greenthumb_nursery";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "highscore38"; 
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static Connection connection = null;

    // Private constructor to prevent instantiation (Singleton pattern)
    private DBConnection() {}
    static java.util.ResourceBundle
    rb = java.util.ResourceBundle.getBundle("gn");
    static boolean empty(String tt) { return tt == null || tt.length() == 0; }

    /**
     * Get database connection instance
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        String pre = rb.getString("db-prefix"),
            driver = rb.getString(pre + ".driver"), url = rb.getString(pre + ".url"),
            user = rb.getString(pre + ".user"), pass = rb.getString(pre + ".pass");
        try {
            // Load MySQL JDBC driver
            if (!(empty(driver))) Class.forName(driver);

            // Create connection if it doesn't exist or is closed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Database connection established successfully.");
            }
            
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new SQLException("Database driver not found", e);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Test database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("Database connection test successful.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get connection with custom parameters
     * @param url Database URL
     * @param username Database username
     * @param password Database password
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection(String url, String username, String password) throws SQLException {
        try {
            String pre = rb.getString("db-prefix"),
                driver = rb.getString(pre + ".driver");
            if (!(empty(driver))) Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new SQLException("Database driver not found", e);
        }
    }

    /**
     * Check if connection is active
     * @return true if connection is active, false otherwise
     */
    public static boolean isConnectionActive() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Get database URL
     * @return Database URL
     */
    public static String getDatabaseURL() {
        String pre = rb.getString("db-prefix"),
            url = rb.getString(pre + ".url");        
        return url;
    }

    /**
     * Close all database connections (for application cleanup)
     */
    public static void closeAllConnections() {
        closeConnection();
    }
}

