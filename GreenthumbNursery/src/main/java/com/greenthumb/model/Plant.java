package com.greenthumb.model;

/**
 * Plant class representing plant entities in the Greenthumb Nursery system.
 * Demonstrates encapsulation and proper data modeling.
 */
public class Plant {
    private String plantId;
    private String name;
    private String type;
    private double price;
    private int quantity;
    private String description;

    // Default constructor
    public Plant() {}

    // Parameterized constructor
    public Plant(String plantId, String name, String type, double price, int quantity, String description) {
        this.plantId = plantId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters and Setters (Encapsulation)
    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Business methods
    /**
     * Get detailed plant information
     * @return Formatted string with plant details
     */
    public String getPlantDetails() {
        return String.format("Plant: %s (%s)\nType: %s\nPrice: $%.2f\nQuantity Available: %d\nDescription: %s",
                name, plantId, type, price, quantity, description);
    }

    /**
     * Update plant quantity (for inventory management)
     * @param amount Amount to add (positive) or subtract (negative)
     * @return true if update successful, false if insufficient quantity
     */
    public boolean updateQuantity(int amount) {
        if (this.quantity + amount >= 0) {
            this.quantity += amount;
            return true;
        }
        return false;
    }

    /**
     * Check if plant is available for purchase
     * @param requestedQuantity Quantity requested
     * @return true if available, false otherwise
     */
    public boolean isAvailable(int requestedQuantity) {
        return this.quantity >= requestedQuantity && requestedQuantity > 0;
    }

    /**
     * Calculate total price for given quantity
     * @param requestedQuantity Quantity to calculate for
     * @return Total price
     */
    public double calculateTotalPrice(int requestedQuantity) {
        return this.price * requestedQuantity;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "plantId='" + plantId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Plant plant = (Plant) obj;
        return plantId != null ? plantId.equals(plant.plantId) : plant.plantId == null;
    }

    @Override
    public int hashCode() {
        return plantId != null ? plantId.hashCode() : 0;
    }
}

