package com.greenthumb.dao;

import com.greenthumb.model.Plant;
import java.util.List;

/**
 * Data Access Object interface for Plant entity.
 * Defines CRUD operations for Plant management.
 */
public interface PlantDAO {
    
    /**
     * Create a new plant in the database
     * @param plant Plant object to create
     * @return true if creation successful, false otherwise
     */
    boolean createPlant(Plant plant);
    
    /**
     * Retrieve plant by plant ID
     * @param plantId Plant ID to search for
     * @return Plant object if found, null otherwise
     */
    Plant getPlantById(String plantId);
    
    /**
     * Update existing plant information
     * @param plant Plant object with updated information
     * @return true if update successful, false otherwise
     */
    boolean updatePlant(Plant plant);
    
    /**
     * Delete plant from database
     * @param plantId Plant ID to delete
     * @return true if deletion successful, false otherwise
     */
    boolean deletePlant(String plantId);
    
    /**
     * Get all plants from database
     * @return List of all plants
     */
    List<Plant> getAllPlants();
    
    /**
     * Search plants by name (partial match)
     * @param name Plant name to search for
     * @return List of plants matching the name
     */
    List<Plant> searchPlantsByName(String name);
    
    /**
     * Search plants by type
     * @param type Plant type to search for
     * @return List of plants of specified type
     */
    List<Plant> searchPlantsByType(String type);
    
    /**
     * Search plants by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of plants within price range
     */
    List<Plant> searchPlantsByPriceRange(double minPrice, double maxPrice);
    
    /**
     * Get plants with low stock (quantity below threshold)
     * @param threshold Stock threshold
     * @return List of plants with low stock
     */
    List<Plant> getLowStockPlants(int threshold);
    
    /**
     * Update plant quantity
     * @param plantId Plant ID
     * @param newQuantity New quantity
     * @return true if update successful, false otherwise
     */
    boolean updatePlantQuantity(String plantId, int newQuantity);
    
    /**
     * Get available plants (quantity > 0)
     * @return List of available plants
     */
    List<Plant> getAvailablePlants();
    
    /**
     * Search plants by multiple criteria
     * @param name Plant name (can be null)
     * @param type Plant type (can be null)
     * @param minPrice Minimum price (can be null)
     * @param maxPrice Maximum price (can be null)
     * @return List of plants matching criteria
     */
    List<Plant> searchPlants(String name, String type, Double minPrice, Double maxPrice);
}

