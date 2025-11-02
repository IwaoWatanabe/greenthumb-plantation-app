package com.greenthumb.model;

import java.util.List;

/**
 * Customer class extending User, demonstrating inheritance.
 * Customers can browse plants, place orders, and view order history.
 */
public class Customer extends User {
    private String customerId;
    private String address;
    private String phone;

    public Customer() {
        super();
    }

    public Customer(String userId, String username, String password, String customerId, String address, String phone) {
        super(userId, username, password, "Customer");
        this.customerId = customerId;
        this.address = address;
        this.phone = phone;
    }

    // Getters and Setters for Customer-specific attributes
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        System.out.println("Customer " + getUsername() + " logged out.");
    }

    /**
     * Customer-specific method for browsing plants
     * @return List of available plants (implementation will be in controller/DAO)
     */
    public List<Plant> browsePlants() {
        System.out.println("Customer " + getUsername() + " is browsing plants.");
        return null; // Implementation will be in controller/DAO layer
    }

    /**
     * Customer-specific method for placing an order
     * @param cartItems List of items to order
     * @return Order object (implementation will be in controller/DAO)
     */
    public Order placeOrder(List<OrderItem> cartItems) {
        System.out.println("Customer " + getUsername() + " is placing an order.");
        return null; // Implementation will be in controller/DAO layer
    }

    /**
     * Customer-specific method for viewing order history
     * @return List of customer's orders (implementation will be in controller/DAO)
     */
    public List<Order> viewOrderHistory() {
        System.out.println("Customer " + getUsername() + " is viewing order history.");
        return null; // Implementation will be in controller/DAO layer
    }

    /**
     * Customer-specific method for updating profile
     */
    public void updateProfile(String address, String phone) {
        this.address = address;
        this.phone = phone;
        System.out.println("Customer " + getUsername() + " updated profile.");
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", customerId='" + customerId + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}

