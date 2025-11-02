package com.greenthumb.model;

/**
 * Staff class extending User, demonstrating inheritance.
 * Staff members can manage plant inventory and process orders.
 */
public class Staff extends User {

    public Staff() {
        super();
    }

    public Staff(String userId, String username, String password) {
        super(userId, username, password, "Staff");
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
        System.out.println("Staff " + getUsername() + " logged out.");
    }

    /**
     * Staff-specific method for managing plant inventory
     */
    public void managePlantInventory() {
        System.out.println("Staff " + getUsername() + " is managing plant inventory.");
    }

    /**
     * Staff-specific method for processing orders
     */
    public void processOrders() {
        System.out.println("Staff " + getUsername() + " is processing orders.");
    }

    /**
     * Staff-specific method for updating customer information
     */
    public void updateCustomerInfo() {
        System.out.println("Staff " + getUsername() + " is updating customer information.");
    }

    /**
     * Staff-specific method for generating inventory reports
     */
    public void generateInventoryReport() {
        System.out.println("Staff " + getUsername() + " is generating inventory report.");
    }

    @Override
    public String toString() {
        return "Staff{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}

