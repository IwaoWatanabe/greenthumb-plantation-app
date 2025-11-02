package com.greenthumb.model;

/**
 * OrderItem class representing individual items within an order.
 * Demonstrates encapsulation and business logic for order line items.
 */
public class OrderItem {
    private String orderItemId;
    private String orderId;
    private String plantId;
    private int quantity;
    private double subtotal;
    private Plant plant; // Reference to the plant object for convenience

    // Default constructor
    public OrderItem() {}

    // Parameterized constructor
    public OrderItem(String orderItemId, String orderId, String plantId, int quantity, double subtotal) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.plantId = plantId;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // Constructor with Plant object
    public OrderItem(String orderItemId, String orderId, Plant plant, int quantity) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.plant = plant;
        this.plantId = plant != null ? plant.getPlantId() : null;
        this.quantity = quantity;
        this.subtotal = calculateSubtotal();
    }

    // Getters and Setters (Encapsulation)
    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Recalculate subtotal when quantity changes
        if (plant != null) {
            this.subtotal = calculateSubtotal();
        }
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
        if (plant != null) {
            this.plantId = plant.getPlantId();
            this.subtotal = calculateSubtotal();
        }
    }

    // Business methods
    /**
     * Calculate subtotal based on plant price and quantity
     * @return Calculated subtotal
     */
    public double calculateSubtotal() {
        if (plant != null && quantity > 0) {
            this.subtotal = plant.getPrice() * quantity;
        } else {
            this.subtotal = 0.0;
        }
        return this.subtotal;
    }

    /**
     * Update quantity and recalculate subtotal
     * @param newQuantity New quantity to set
     * @return true if update successful, false if invalid quantity
     */
    public boolean updateQuantity(int newQuantity) {
        if (newQuantity > 0) {
            this.quantity = newQuantity;
            calculateSubtotal();
            return true;
        }
        return false;
    }

    /**
     * Check if the order item is valid
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return orderItemId != null && !orderItemId.trim().isEmpty() &&
               orderId != null && !orderId.trim().isEmpty() &&
               plantId != null && !plantId.trim().isEmpty() &&
               quantity > 0 && subtotal >= 0;
    }

    /**
     * Get order item details
     * @return Formatted string with order item details
     */
    public String getOrderItemDetails() {
        String plantName = plant != null ? plant.getName() : "Unknown Plant";
        double unitPrice = plant != null ? plant.getPrice() : (quantity > 0 ? subtotal / quantity : 0);
        
        return String.format("Item: %s\nPlant ID: %s\nQuantity: %d\nUnit Price: $%.2f\nSubtotal: $%.2f",
                plantName, plantId, quantity, unitPrice, subtotal);
    }

    /**
     * Create a copy of this order item
     * @return New OrderItem instance with same values
     */
    public OrderItem copy() {
        OrderItem copy = new OrderItem(orderItemId, orderId, plantId, quantity, subtotal);
        copy.setPlant(this.plant);
        return copy;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", plantId='" + plantId + '\'' +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", plantName='" + (plant != null ? plant.getName() : "N/A") + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderItem orderItem = (OrderItem) obj;
        return orderItemId != null ? orderItemId.equals(orderItem.orderItemId) : orderItem.orderItemId == null;
    }

    @Override
    public int hashCode() {
        return orderItemId != null ? orderItemId.hashCode() : 0;
    }
}

