package com.greenthumb.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Order class representing customer orders in the Greenthumb Nursery system.
 * Demonstrates encapsulation and business logic implementation.
 */
public class Order {
    private String orderId;
    private String customerId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private List<OrderItem> orderItems;

    // Order status constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_PROCESSING = "Processing";
    public static final String STATUS_SHIPPED = "Shipped";
    public static final String STATUS_DELIVERED = "Delivered";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_RETURNED = "Returned";

    // Default constructor
    public Order() {
        this.orderItems = new ArrayList<>();
        this.orderDate = new Date();
        this.status = STATUS_PENDING;
    }

    // Parameterized constructor
    public Order(String orderId, String customerId, Date orderDate, double totalAmount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate != null ? orderDate : new Date();
        this.totalAmount = totalAmount;
        this.status = status != null ? status : STATUS_PENDING;
        this.orderItems = new ArrayList<>();
    }

    // Getters and Setters (Encapsulation)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
    }

    // Business methods
    /**
     * Calculate total amount based on order items
     * @return Calculated total amount
     */
    public double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            total += item.getSubtotal();
        }
        this.totalAmount = total;
        return total;
    }

    /**
     * Update order status with validation
     * @param newStatus New status to set
     * @return true if status update is valid, false otherwise
     */
    public boolean updateStatus(String newStatus) {
        if (isValidStatusTransition(this.status, newStatus)) {
            this.status = newStatus;
            return true;
        }
        return false;
    }

    /**
     * Add an order item to the order
     * @param orderItem Item to add
     */
    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null) {
            this.orderItems.add(orderItem);
            calculateTotal(); // Recalculate total
        }
    }

    /**
     * Remove an order item from the order
     * @param orderItem Item to remove
     * @return true if item was removed, false otherwise
     */
    public boolean removeOrderItem(OrderItem orderItem) {
        if (orderItems.remove(orderItem)) {
            calculateTotal(); // Recalculate total
            return true;
        }
        return false;
    }

    /**
     * Check if the order can be cancelled
     * @return true if order can be cancelled, false otherwise
     */
    public boolean canBeCancelled() {
        return STATUS_PENDING.equals(status) || STATUS_PROCESSING.equals(status);
    }

    /**
     * Check if the order can be returned
     * @return true if order can be returned, false otherwise
     */
    public boolean canBeReturned() {
        return STATUS_DELIVERED.equals(status);
    }

    /**
     * Validate status transition
     * @param currentStatus Current order status
     * @param newStatus New status to transition to
     * @return true if transition is valid, false otherwise
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus == null || newStatus == null) return false;
        
        switch (currentStatus) {
            case STATUS_PENDING:
                return STATUS_PROCESSING.equals(newStatus) || STATUS_CANCELLED.equals(newStatus);
            case STATUS_PROCESSING:
                return STATUS_SHIPPED.equals(newStatus) || STATUS_CANCELLED.equals(newStatus);
            case STATUS_SHIPPED:
                return STATUS_DELIVERED.equals(newStatus) || STATUS_RETURNED.equals(newStatus);
            case STATUS_DELIVERED:
                return STATUS_RETURNED.equals(newStatus);
            case STATUS_CANCELLED:
            case STATUS_RETURNED:
                return false; // Terminal states
            default:
                return false;
        }
    }

    /**
     * Get order summary
     * @return Formatted string with order summary
     */
    public String getOrderSummary() {
        return String.format("Order ID: %s\nCustomer ID: %s\nDate: %s\nStatus: %s\nTotal: $%.2f\nItems: %d",
                orderId, customerId, orderDate, status, totalAmount, orderItems.size());
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", orderItems=" + orderItems.size() + " items" +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderId != null ? orderId.equals(order.orderId) : order.orderId == null;
    }

    @Override
    public int hashCode() {
        return orderId != null ? orderId.hashCode() : 0;
    }
}

