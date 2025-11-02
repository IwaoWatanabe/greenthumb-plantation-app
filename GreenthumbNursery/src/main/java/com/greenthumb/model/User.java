package com.greenthumb.model;

/**
 * Abstract User class representing the base user entity in the Greenthumb Nursery system.
 * This class demonstrates inheritance and encapsulation OOP concepts.
 */
public abstract class User {
    private String userId;
    private String username;
    private String password;
    private String role;

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters (Encapsulation)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Abstract methods to be implemented by subclasses (Polymorphism)
    public abstract boolean login(String username, String password);
    public abstract void logout();

    // Common method for all users
    public String getUserInfo() {
        return "User ID: " + userId + ", Username: " + username + ", Role: " + role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

