package com.greenthumb.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation across the application.
 * Provides common validation methods for user inputs.
 */
public class ValidationUtil {
    
    // Regular expression patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    private static final Pattern PLANT_ID_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_-]{1,20}$"
    );
    
    private static final Pattern ORDER_ID_PATTERN = Pattern.compile(
        "^order_[a-zA-Z0-9_-]{1,20}$"
    );

    /**
     * Validate if string is not null and not empty
     * @param value String to validate
     * @return true if valid, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate string length
     * @param value String to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return true if valid, false otherwise
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) return false;
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validate username format
     * @param username Username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (!isNotEmpty(username)) return false;
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validate password strength
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (!isNotEmpty(password)) return false;
        
        String trimmedPassword = password.trim();
        
        // Minimum length check
        if (trimmedPassword.length() < 6) return false;
        
        // Maximum length check
        if (trimmedPassword.length() > 50) return false;
        
        // Must contain at least one letter or number
        boolean hasAlphaNumeric = trimmedPassword.matches(".*[a-zA-Z0-9].*");
        
        return hasAlphaNumeric;
    }

    /**
     * Validate email format
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) return false;
        // Remove spaces and dashes for validation
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    /**
     * Validate plant ID format
     * @param plantId Plant ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPlantId(String plantId) {
        if (!isNotEmpty(plantId)) return false;
        return PLANT_ID_PATTERN.matcher(plantId.trim()).matches();
    }

    /**
     * Validate order ID format
     * @param orderId Order ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidOrderId(String orderId) {
        if (!isNotEmpty(orderId)) return false;
        return ORDER_ID_PATTERN.matcher(orderId.trim()).matches();
    }

    /**
     * Validate numeric input (integer)
     * @param value String value to validate
     * @return true if valid integer, false otherwise
     */
    public static boolean isValidInteger(String value) {
        if (!isNotEmpty(value)) return false;
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate numeric input (double)
     * @param value String value to validate
     * @return true if valid double, false otherwise
     */
    public static boolean isValidDouble(String value) {
        if (!isNotEmpty(value)) return false;
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate positive integer
     * @param value String value to validate
     * @return true if valid positive integer, false otherwise
     */
    public static boolean isValidPositiveInteger(String value) {
        if (!isValidInteger(value)) return false;
        try {
            int intValue = Integer.parseInt(value.trim());
            return intValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate non-negative integer
     * @param value String value to validate
     * @return true if valid non-negative integer, false otherwise
     */
    public static boolean isValidNonNegativeInteger(String value) {
        if (!isValidInteger(value)) return false;
        try {
            int intValue = Integer.parseInt(value.trim());
            return intValue >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate positive double (for prices)
     * @param value String value to validate
     * @return true if valid positive double, false otherwise
     */
    public static boolean isValidPositiveDouble(String value) {
        if (!isValidDouble(value)) return false;
        try {
            double doubleValue = Double.parseDouble(value.trim());
            return doubleValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate price format (up to 2 decimal places)
     * @param value String value to validate
     * @return true if valid price, false otherwise
     */
    public static boolean isValidPrice(String value) {
        if (!isValidPositiveDouble(value)) return false;
        
        try {
            double price = Double.parseDouble(value.trim());
            
            // Check if price has more than 2 decimal places
            String priceStr = String.valueOf(price);
            int decimalIndex = priceStr.indexOf('.');
            if (decimalIndex != -1 && priceStr.length() - decimalIndex - 1 > 2) {
                return false;
            }
            
            // Check reasonable price range (0.01 to 99999.99)
            return price >= 0.01 && price <= 99999.99;
            
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate quantity (1 to 10000)
     * @param value String value to validate
     * @return true if valid quantity, false otherwise
     */
    public static boolean isValidQuantity(String value) {
        if (!isValidPositiveInteger(value)) return false;
        
        try {
            int quantity = Integer.parseInt(value.trim());
            return quantity >= 1 && quantity <= 10000;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate user role
     * @param role Role to validate
     * @return true if valid role, false otherwise
     */
    public static boolean isValidUserRole(String role) {
        if (!isNotEmpty(role)) return false;
        String trimmedRole = role.trim();
        return "Admin".equals(trimmedRole) || "Staff".equals(trimmedRole) || "Customer".equals(trimmedRole);
    }

    /**
     * Validate order status
     * @param status Status to validate
     * @return true if valid status, false otherwise
     */
    public static boolean isValidOrderStatus(String status) {
        if (!isNotEmpty(status)) return false;
        String trimmedStatus = status.trim();
        return "Pending".equals(trimmedStatus) || 
               "Processing".equals(trimmedStatus) || 
               "Shipped".equals(trimmedStatus) || 
               "Delivered".equals(trimmedStatus) || 
               "Cancelled".equals(trimmedStatus) || 
               "Returned".equals(trimmedStatus);
    }

    /**
     * Validate plant type
     * @param type Plant type to validate
     * @return true if valid type, false otherwise
     */
    public static boolean isValidPlantType(String type) {
        if (!isNotEmpty(type)) return false;
        return isValidLength(type, 2, 50);
    }

    /**
     * Validate plant name
     * @param name Plant name to validate
     * @return true if valid name, false otherwise
     */
    public static boolean isValidPlantName(String name) {
        if (!isNotEmpty(name)) return false;
        return isValidLength(name, 2, 100);
    }

    /**
     * Validate plant description
     * @param description Plant description to validate
     * @return true if valid description, false otherwise
     */
    public static boolean isValidPlantDescription(String description) {
        // Description can be empty, but if provided, should not exceed 1000 characters
        if (description == null) return true;
        return description.length() <= 1000;
    }

    /**
     * Validate address
     * @param address Address to validate
     * @return true if valid address, false otherwise
     */
    public static boolean isValidAddress(String address) {
        // Address can be empty, but if provided, should be reasonable length
        if (address == null || address.trim().isEmpty()) return true;
        return isValidLength(address, 5, 200);
    }

    /**
     * Sanitize string input (remove potentially harmful characters)
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) return null;
        
        // Remove leading/trailing whitespace
        String sanitized = input.trim();
        
        // Remove potentially harmful characters for SQL injection prevention
        // Note: This is basic sanitization. Prepared statements are the primary defense.
        sanitized = sanitized.replaceAll("[<>\"'%;()&+]", "");
        
        return sanitized;
    }

    /**
     * Get validation error message for username
     * @param username Username to validate
     * @return Error message or null if valid
     */
    public static String getUsernameValidationError(String username) {
        if (!isNotEmpty(username)) {
            return "Username cannot be empty.";
        }
        if (!isValidLength(username, 3, 20)) {
            return "Username must be between 3 and 20 characters.";
        }
        if (!isValidUsername(username)) {
            return "Username can only contain letters, numbers, and underscores.";
        }
        return null;
    }

    /**
     * Get validation error message for password
     * @param password Password to validate
     * @return Error message or null if valid
     */
    public static String getPasswordValidationError(String password) {
        if (!isNotEmpty(password)) {
            return "Password cannot be empty.";
        }
        if (!isValidLength(password, 6, 50)) {
            return "Password must be between 6 and 50 characters.";
        }
        if (!isValidPassword(password)) {
            return "Password must contain at least one letter or number.";
        }
        return null;
    }

    /**
     * Get validation error message for price
     * @param price Price to validate
     * @return Error message or null if valid
     */
    public static String getPriceValidationError(String price) {
        if (!isNotEmpty(price)) {
            return "Price cannot be empty.";
        }
        if (!isValidDouble(price)) {
            return "Price must be a valid number.";
        }
        if (!isValidPrice(price)) {
            return "Price must be between $0.01 and $99,999.99.";
        }
        return null;
    }

    /**
     * Get validation error message for quantity
     * @param quantity Quantity to validate
     * @return Error message or null if valid
     */
    public static String getQuantityValidationError(String quantity) {
        if (!isNotEmpty(quantity)) {
            return "Quantity cannot be empty.";
        }
        if (!isValidInteger(quantity)) {
            return "Quantity must be a valid number.";
        }
        if (!isValidQuantity(quantity)) {
            return "Quantity must be between 1 and 10,000.";
        }
        return null;
    }
}

