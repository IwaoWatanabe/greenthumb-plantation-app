package com.greenthumb.dao;

import com.greenthumb.model.Order;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object interface for Order entity.
 * Defines CRUD operations for Order management.
 */
public interface OrderDAO {
    
    /**
     * Create a new order in the database
     * @param order Order object to create
     * @return true if creation successful, false otherwise
     */
    boolean createOrder(Order order);
    
    /**
     * Retrieve order by order ID
     * @param orderId Order ID to search for
     * @return Order object if found, null otherwise
     */
    Order getOrderById(String orderId);
    
    /**
     * Update existing order information
     * @param order Order object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updateOrder(Order order);
    
    /**
     * Delete order from database
     * @param orderId Order ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deleteOrder(String orderId);
    
    /**
     * Get all orders from database
     * @return List of all orders
     */
    List<Order> getAllOrders();
    
    /**
     * Get orders by customer ID
     * @param customerId Customer ID to search for
     * @return List of orders for the customer
     */
    List<Order> getOrdersByCustomerId(String customerId);
    
    /**
     * Get orders by status
     * @param status Order status to filter by
     * @return List of orders with specified status
     */
    List<Order> getOrdersByStatus(String status);
    
    /**
     * Get orders by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of orders within date range
     */
    List<Order> getOrdersByDateRange(Date startDate, Date endDate);
    
    /**
     * Update order status
     * @param orderId Order ID
     * @param newStatus New status
     * @return true if update successful, false otherwise
     */
    boolean updateOrderStatus(String orderId, String newStatus);
    
    /**
     * Get orders with total amount above threshold
     * @param threshold Amount threshold
     * @return List of orders above threshold
     */
    List<Order> getOrdersAboveAmount(double threshold);
    
    /**
     * Get recent orders (within specified days)
     * @param days Number of days to look back
     * @return List of recent orders
     */
    List<Order> getRecentOrders(int days);
    
    /**
     * Get order count by customer
     * @param customerId Customer ID
     * @return Number of orders for the customer
     */
    int getOrderCountByCustomer(String customerId);
    
    /**
     * Get total sales amount by customer
     * @param customerId Customer ID
     * @return Total amount spent by customer
     */
    double getTotalSalesByCustomer(String customerId);
    
    /**
     * Search orders by multiple criteria
     * @param customerId Customer ID (can be null)
     * @param status Order status (can be null)
     * @param startDate Start date (can be null)
     * @param endDate End date (can be null)
     * @return List of orders matching criteria
     */
    List<Order> searchOrders(String customerId, String status, Date startDate, Date endDate);
}

