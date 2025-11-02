package com.greenthumb.dao;

import com.greenthumb.model.User;
import java.util.List;

/**
 * Data Access Object interface for User entity.
 * Defines CRUD operations for User management.
 */
public interface UserDAO {
    
    /**
     * Create a new user in the database
     * @param user User object to create
     * @return true if creation successful, false otherwise
     */
    boolean createUser(User user);
    
    /**
     * Retrieve user by user ID
     * @param userId User ID to search for
     * @return User object if found, null otherwise
     */
    User getUserById(String userId);
    
    /**
     * Retrieve user by username
     * @param username Username to search for
     * @return User object if found, null otherwise
     */
    User getUserByUsername(String username);
    
    /**
     * Update existing user information
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateUser(User user);
    
    /**
     * Delete user from database
     * @param userId User ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteUser(String userId);
    
    /**
     * Get all users from database
     * @return List of all users
     */
    List<User> getAllUsers();
    
    /**
     * Get users by role
     * @param role User role to filter by
     * @return List of users with specified role
     */
    List<User> getUsersByRole(String role);
    
    /**
     * Authenticate user login
     * @param username Username
     * @param password Password
     * @return User object if authentication successful, null otherwise
     */
    User authenticateUser(String username, String password);
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    boolean usernameExists(String username);
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPassword New password
     * @return true if update successful, false otherwise
     */
    boolean updatePassword(String userId, String newPassword);
}

