package com.greenthumb.model;

/**
 * Admin class extending User, demonstrating inheritance.
 * Admins have full control over the system including user management.
 */
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String userId, String username, String password) {
        super(userId, username, password, "Admin");
    }

    @Override
    public boolean login(String username, String password) {
        // Implementation will be handled by the controller/DAO layer
        // This method signature satisfies the abstract method requirement
        return false;
    }

    @Override
    public void logout() {
        // Implementation will be handled by the controller layer
        System.out.println("Admin " + getUsername() + " logged out.");
    }

    /**
     * Admin-specific method for managing staff accounts
     */
    public void manageStaffAccounts() {
        System.out.println("Admin " + getUsername() + " is managing staff accounts.");
    }

    /**
     * Admin-specific method for viewing system reports
     */
    public void viewSystemReports() {
        System.out.println("Admin " + getUsername() + " is viewing system reports.");
    }

    /**
     * Admin-specific method for managing plant inventory
     */
    public void manageAllInventory() {
        System.out.println("Admin " + getUsername() + " is managing all inventory.");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}

