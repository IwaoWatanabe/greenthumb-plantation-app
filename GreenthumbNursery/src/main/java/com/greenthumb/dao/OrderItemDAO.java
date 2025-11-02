package com.greenthumb.dao;

import com.greenthumb.model.OrderItem;
import java.util.List;

/**
 * Data Access Object interface for OrderItem entity.
 * Defines CRUD operations for OrderItem management.
 */
public interface OrderItemDAO {
    
    /**
     * Create a new order item in the database
     * @param orderItem OrderItem object to create
     * @return true if creation successful, false otherwise
     */
    boolean createOrderItem(OrderItem orderItem);
    
    /**
     * Retrieve order item by order item ID
     * @param orderItemId Order item ID to search for
     * @return OrderItem object if found, null otherwise
     */
    OrderItem getOrderItemById(String orderItemId);
    
    /**
     * Update existing order item information
     * @param orderItem OrderItem object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateOrderItem(OrderItem orderItem);
    
    /**
     * Delete order item from database
     * @param orderItemId Order item ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteOrderItem(String orderItemId);
    
    /**
     * Get all order items from database
     * @return List of all order items
     */
    List<OrderItem> getAllOrderItems();
    
    /**
     * Get order items by order ID
     * @param orderId Order ID to search for
     * @return List of order items for the order
     */
    List<OrderItem> getOrderItemsByOrderId(String orderId);
    
    /**
     * Get order items by plant ID
     * @param plantId Plant ID to search for
     * @return List of order items containing the plant
     */
    List<OrderItem> getOrderItemsByPlantId(String plantId);
    
    /**
     * Delete all order items for a specific order
     * @param orderId Order ID
     * @return true if deletion successful, false otherwise
     */
    boolean deleteOrderItemsByOrderId(String orderId);
    
    /**
     * Get total quantity sold for a specific plant
     * @param plantId Plant ID
     * @return Total quantity sold
     */
    int getTotalQuantitySoldByPlant(String plantId);
    
    /**
     * Get total revenue for a specific plant
     * @param plantId Plant ID
     * @return Total revenue from the plant
     */
    double getTotalRevenueByPlant(String plantId);
    
    /**
     * Get order items with quantity above threshold
     * @param threshold Quantity threshold
     * @return List of order items above threshold
     */
    List<OrderItem> getOrderItemsAboveQuantity(int threshold);
    
    /**
     * Update order item quantity
     * @param orderItemId Order item ID
     * @param newQuantity New quantity
     * @return true if update successful, false otherwise
     */
    boolean updateOrderItemQuantity(String orderItemId, int newQuantity);
    
    /**
     * Get order items by order ID with plant details
     * @param orderId Order ID
     * @return List of order items with plant information
     */
    List<OrderItem> getOrderItemsWithPlantDetails(String orderId);
    
    /**
     * Calculate total value of order items for an order
     * @param orderId Order ID
     * @return Total value of all order items
     */
    double calculateOrderTotal(String orderId);
}

