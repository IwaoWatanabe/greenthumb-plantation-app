# Greenthumb Nursery Management System - Technical Documentation

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Design Patterns](#design-patterns)
3. [Database Design](#database-design)
4. [Class Structure](#class-structure)
5. [API Documentation](#api-documentation)
6. [Security Implementation](#security-implementation)
7. [Error Handling](#error-handling)
8. [Performance Considerations](#performance-considerations)
9. [Testing Strategy](#testing-strategy)
10. [Deployment Guide](#deployment-guide)

## Architecture Overview

### System Architecture
The Greenthumb Nursery Management System follows a three-tier architecture pattern:

1. **Presentation Layer (View)**: Java Swing components for user interface
2. **Business Logic Layer (Controller)**: Application logic and workflow management
3. **Data Access Layer (Model)**: Database operations and data persistence

### Technology Stack
- **Frontend**: Java Swing (JDK 11+)
- **Backend**: Java with JDBC
- **Database**: MySQL 8.0+
- **Build Tool**: Maven 3.6+
- **Architecture Pattern**: Model-View-Controller (MVC)

### Component Interaction
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   View Layer    │    │ Controller Layer│    │   Model Layer   │
│   (Swing UI)    │◄──►│  (Business      │◄──►│   (Entities &   │
│                 │    │   Logic)        │    │    DAO)         │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Input    │    │   Validation    │    │   Database      │
│   & Display     │    │   & Processing  │    │   Operations    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Design Patterns

### Model-View-Controller (MVC)
The application strictly follows MVC architecture:

**Model**: Represents data and business logic
- Entity classes (User, Plant, Order, OrderItem)
- Data Access Objects (DAO interfaces and implementations)
- Database connection utilities

**View**: Handles user interface and presentation
- Swing components (JFrame, JPanel, JTable, etc.)
- Event handling for user interactions
- Data display and formatting

**Controller**: Manages application flow and coordinates between Model and View
- Business logic implementation
- Input validation
- Workflow management

### Data Access Object (DAO) Pattern
Provides abstraction layer between business logic and data persistence:

```java
public interface UserDAO {
    boolean createUser(User user);
    User getUserById(String userId);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(String userId);
}
```

### Singleton Pattern
Used for database connection management:

```java
public class DBConnection {
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Create new connection
        }
        return connection;
    }
}
```

### Factory Pattern
Implemented for user object creation based on role:

```java
public class UserFactory {
    public static User createUser(String role, String userId, String username, String password) {
        switch (role) {
            case "Admin": return new Admin(userId, username, password);
            case "Staff": return new Staff(userId, username, password);
            case "Customer": return new Customer(userId, username, password, null, null, null);
            default: throw new IllegalArgumentException("Invalid role");
        }
    }
}
```

## Database Design

### Entity Relationship Diagram
```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    Users    │     │   Orders    │     │   Plants    │
├─────────────┤     ├─────────────┤     ├─────────────┤
│ user_id (PK)│────┐│ order_id(PK)│     │plant_id (PK)│
│ username    │    ││ customer_id │     │ name        │
│ password    │    ││ order_date  │     │ type        │
│ role        │    ││ total_amount│     │ price       │
│ address     │    ││ status      │     │ quantity    │
│ phone       │    │└─────────────┘     │ description │
│ email       │    │       │            └─────────────┘
└─────────────┘    │       │                   │
                   │       │                   │
                   │       ▼                   │
                   │┌─────────────┐            │
                   ││ Order_Items │            │
                   │├─────────────┤            │
                   ││item_id (PK) │            │
                   └│order_id (FK)│            │
                    │plant_id (FK)│◄───────────┘
                    │ quantity    │
                    │ subtotal    │
                    └─────────────┘
```

### Table Specifications

#### Users Table
```sql
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Staff', 'Customer') NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Plants Table
```sql
CREATE TABLE plants (
    plant_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Orders Table
```sql
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled', 'Returned') DEFAULT 'Pending',
    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

#### Order_Items Table
```sql
CREATE TABLE order_items (
    item_id VARCHAR(50) PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL,
    plant_id VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (plant_id) REFERENCES plants(plant_id) ON DELETE RESTRICT
);
```

### Database Constraints and Indexes
```sql
-- Indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_plant ON order_items(plant_id);
CREATE INDEX idx_plants_type ON plants(type);

-- Check constraints
ALTER TABLE plants ADD CONSTRAINT chk_price_positive CHECK (price > 0);
ALTER TABLE plants ADD CONSTRAINT chk_quantity_non_negative CHECK (quantity >= 0);
ALTER TABLE order_items ADD CONSTRAINT chk_quantity_positive CHECK (quantity > 0);
ALTER TABLE order_items ADD CONSTRAINT chk_subtotal_positive CHECK (subtotal > 0);
```

## Class Structure

### Model Classes

#### User Hierarchy
```java
public abstract class User {
    protected String userId;
    protected String username;
    protected String password;
    protected String role;
    
    // Abstract methods
    public abstract boolean login(String username, String password);
    public abstract void logout();
    public abstract String getRole();
}

public class Admin extends User {
    public Admin(String userId, String username, String password) {
        super(userId, username, password, "Admin");
    }
    
    @Override
    public boolean login(String username, String password) {
        // Admin-specific login logic
    }
}

public class Staff extends User {
    // Staff-specific implementation
}

public class Customer extends User {
    private String customerId;
    private String address;
    private String phone;
    private String email;
    
    // Customer-specific methods
}
```

#### Entity Classes
```java
public class Plant {
    private String plantId;
    private String name;
    private String type;
    private double price;
    private int quantity;
    private String description;
    
    // Business methods
    public boolean isAvailable(int requestedQuantity) {
        return quantity >= requestedQuantity;
    }
    
    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }
}

public class Order {
    private String orderId;
    private String customerId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    
    // Status constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_PROCESSING = "Processing";
    public static final String STATUS_SHIPPED = "Shipped";
    public static final String STATUS_DELIVERED = "Delivered";
    public static final String STATUS_CANCELLED = "Cancelled";
    
    // Business methods
    public boolean canBeCancelled() {
        return STATUS_PENDING.equals(status);
    }
    
    public boolean updateStatus(String newStatus) {
        // Validate status transition
    }
}
```

### Controller Classes

#### Base Controller Pattern
```java
public abstract class BaseController {
    protected void showErrorMessage(String message) {
        ErrorHandler.showErrorDialog(null, message, "Error");
    }
    
    protected void showSuccessMessage(String message) {
        ErrorHandler.showSuccessMessage(null, message);
    }
    
    protected boolean validateInput(String value, String fieldName, 
                                  Function<String, String> validator) {
        return ErrorHandler.validateInput(null, value, fieldName, validator);
    }
}

public class LoginController extends BaseController {
    private LoginView loginView;
    private UserDAO userDAO;
    
    public boolean login(String username, String password) {
        // Validation
        if (!validateInput(username, "Username", ValidationUtil::getUsernameValidationError)) {
            return false;
        }
        
        // Authentication
        User user = userDAO.authenticateUser(username, password);
        if (user != null) {
            navigateToDashboard(user);
            return true;
        }
        
        showErrorMessage("Invalid credentials");
        return false;
    }
}
```

### View Classes

#### Base View Pattern
```java
public abstract class BaseView extends JFrame {
    protected void setupEventHandlers() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }
    
    protected abstract void handleWindowClosing();
    
    protected void showErrorDialog(String message) {
        ErrorHandler.showErrorDialog(this, message, "Error");
    }
}

public class LoginView extends BaseView {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private LoginController controller;
    
    @Override
    protected void handleWindowClosing() {
        System.exit(0);
    }
}
```

## API Documentation

### DAO Interface Specifications

#### UserDAO Interface
```java
public interface UserDAO {
    /**
     * Create a new user in the database
     * @param user User object to create
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean createUser(User user) throws SQLException;
    
    /**
     * Authenticate user credentials
     * @param username User's username
     * @param password User's password
     * @return User object if authenticated, null otherwise
     * @throws SQLException if database error occurs
     */
    User authenticateUser(String username, String password) throws SQLException;
    
    /**
     * Retrieve user by ID
     * @param userId Unique user identifier
     * @return User object or null if not found
     * @throws SQLException if database error occurs
     */
    User getUserById(String userId) throws SQLException;
    
    /**
     * Get all users in the system
     * @return List of all users
     * @throws SQLException if database error occurs
     */
    List<User> getAllUsers() throws SQLException;
    
    /**
     * Update existing user information
     * @param user User object with updated information
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean updateUser(User user) throws SQLException;
    
    /**
     * Delete user from system
     * @param userId ID of user to delete
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean deleteUser(String userId) throws SQLException;
    
    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean usernameExists(String username) throws SQLException;
    
    /**
     * Get users by role
     * @param role User role to filter by
     * @return List of users with specified role
     * @throws SQLException if database error occurs
     */
    List<User> getUsersByRole(String role) throws SQLException;
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPassword New password (should be hashed)
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean updatePassword(String userId, String newPassword) throws SQLException;
}
```

#### PlantDAO Interface
```java
public interface PlantDAO {
    boolean createPlant(Plant plant) throws SQLException;
    Plant getPlantById(String plantId) throws SQLException;
    List<Plant> getAllPlants() throws SQLException;
    List<Plant> getAvailablePlants() throws SQLException;
    boolean updatePlant(Plant plant) throws SQLException;
    boolean deletePlant(String plantId) throws SQLException;
    boolean updatePlantQuantity(String plantId, int newQuantity) throws SQLException;
    List<Plant> searchPlants(String name, String type, Double minPrice, Double maxPrice) throws SQLException;
    List<Plant> searchPlantsByType(String type) throws SQLException;
    List<Plant> getLowStockPlants(int threshold) throws SQLException;
}
```

### Controller Method Specifications

#### AdminController Key Methods
```java
public class AdminController {
    /**
     * Create a new user account
     * @param userId Unique user identifier
     * @param username Login username
     * @param password User password
     * @param role User role (Admin, Staff, Customer)
     * @return true if creation successful
     */
    public boolean createUser(String userId, String username, String password, String role);
    
    /**
     * Generate comprehensive user report
     * @return Formatted report string
     */
    public String generateUserReport();
    
    /**
     * Update order status with validation
     * @param orderId Order to update
     * @param newStatus New status value
     * @return true if update successful
     */
    public boolean updateOrderStatus(String orderId, String newStatus);
}
```

## Security Implementation

### Authentication
```java
public class AuthenticationService {
    /**
     * Authenticate user with password hashing
     */
    public User authenticate(String username, String password) {
        // Hash the provided password
        String hashedPassword = hashPassword(password);
        
        // Compare with stored hash
        User user = userDAO.getUserByUsername(username);
        if (user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    
    private String hashPassword(String password) {
        // Implementation using secure hashing algorithm
        // Note: In production, use BCrypt or similar
        return password; // Simplified for demo
    }
}
```

### Input Validation
```java
public class ValidationUtil {
    /**
     * Validate username format and constraints
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    /**
     * Sanitize input to prevent SQL injection
     */
    public static String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("[<>\"'%;()&+]", "");
    }
}
```

### SQL Injection Prevention
```java
public class UserDAOImpl implements UserDAO {
    @Override
    public User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createUserFromResultSet(rs);
                }
            }
        }
        return null;
    }
}
```

### Role-Based Access Control
```java
public class SecurityManager {
    public static boolean hasPermission(User user, String operation) {
        switch (user.getRole()) {
            case "Admin":
                return true; // Admin has all permissions
            case "Staff":
                return isStaffOperation(operation);
            case "Customer":
                return isCustomerOperation(operation);
            default:
                return false;
        }
    }
    
    private static boolean isStaffOperation(String operation) {
        return Arrays.asList("VIEW_PLANTS", "UPDATE_PLANTS", "PROCESS_ORDERS")
                    .contains(operation);
    }
}
```

## Error Handling

### Exception Hierarchy
```java
public class NurseryException extends Exception {
    public NurseryException(String message) { super(message); }
    public NurseryException(String message, Throwable cause) { super(message, cause); }
}

public class DatabaseException extends NurseryException {
    public DatabaseException(String message, SQLException cause) {
        super("Database operation failed: " + message, cause);
    }
}

public class ValidationException extends NurseryException {
    public ValidationException(String field, String message) {
        super("Validation failed for " + field + ": " + message);
    }
}

public class AuthenticationException extends NurseryException {
    public AuthenticationException(String message) {
        super("Authentication failed: " + message);
    }
}
```

### Error Handling Strategy
```java
public class ErrorHandler {
    public static void handleDatabaseError(Component parent, SQLException e, String operation) {
        // Log the error
        logger.log(Level.SEVERE, "Database error during: " + operation, e);
        
        // Show user-friendly message
        String userMessage = "Database operation failed. Please try again.";
        showErrorDialog(parent, userMessage, "Database Error");
        
        // Check for connection issues
        if (isDatabaseConnectionError(e)) {
            suggestRestart(parent);
        }
    }
    
    public static void handleValidationError(Component parent, String message) {
        showErrorDialog(parent, message, "Input Validation Error");
        logger.log(Level.WARNING, "Validation error: " + message);
    }
}
```

### Logging Configuration
```java
public class LoggingConfig {
    public static void setupLogging() {
        Logger rootLogger = Logger.getLogger("");
        
        // Console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        
        // File handler
        try {
            FileHandler fileHandler = new FileHandler("nursery.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to setup file logging: " + e.getMessage());
        }
        
        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.INFO);
    }
}
```

## Performance Considerations

### Database Connection Pooling
```java
public class ConnectionPool {
    private static final int MAX_POOL_SIZE = 10;
    private static final Queue<Connection> connectionPool = new LinkedList<>();
    
    public static synchronized Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            return createNewConnection();
        }
        return connectionPool.poll();
    }
    
    public static synchronized void returnConnection(Connection connection) {
        if (connectionPool.size() < MAX_POOL_SIZE && !connection.isClosed()) {
            connectionPool.offer(connection);
        } else {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error closing connection", e);
            }
        }
    }
}
```

### Lazy Loading Implementation
```java
public class OrderWithItems {
    private Order order;
    private List<OrderItem> items;
    private boolean itemsLoaded = false;
    
    public List<OrderItem> getItems() {
        if (!itemsLoaded) {
            items = orderItemDAO.getOrderItemsByOrderId(order.getOrderId());
            itemsLoaded = true;
        }
        return items;
    }
}
```

### Caching Strategy
```java
public class PlantCache {
    private static final Map<String, Plant> cache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRY = 5 * 60 * 1000; // 5 minutes
    private static long lastRefresh = 0;
    
    public static Plant getPlant(String plantId) {
        if (shouldRefreshCache()) {
            refreshCache();
        }
        return cache.get(plantId);
    }
    
    private static boolean shouldRefreshCache() {
        return System.currentTimeMillis() - lastRefresh > CACHE_EXPIRY;
    }
}
```

## Testing Strategy

### Unit Testing Framework
```java
@Test
public class UserDAOImplTest {
    private UserDAO userDAO;
    private Connection testConnection;
    
    @BeforeEach
    public void setUp() throws SQLException {
        // Setup test database connection
        testConnection = DriverManager.getConnection(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        userDAO = new UserDAOImpl(testConnection);
        createTestTables();
    }
    
    @Test
    public void testCreateUser() throws SQLException {
        User user = new Admin("admin1", "testadmin", "password123");
        boolean result = userDAO.createUser(user);
        assertTrue(result);
        
        User retrieved = userDAO.getUserById("admin1");
        assertNotNull(retrieved);
        assertEquals("testadmin", retrieved.getUsername());
    }
    
    @Test
    public void testAuthenticateUser() throws SQLException {
        // Setup test data
        User user = new Admin("admin1", "testadmin", "password123");
        userDAO.createUser(user);
        
        // Test authentication
        User authenticated = userDAO.authenticateUser("testadmin", "password123");
        assertNotNull(authenticated);
        assertEquals("Admin", authenticated.getRole());
        
        // Test invalid credentials
        User invalid = userDAO.authenticateUser("testadmin", "wrongpassword");
        assertNull(invalid);
    }
}
```

### Integration Testing
```java
@Test
public class OrderProcessingIntegrationTest {
    @Test
    public void testCompleteOrderWorkflow() {
        // 1. Customer adds items to cart
        CustomerController customerController = new CustomerController(testCustomer);
        assertTrue(customerController.addToCart("plant1", 2));
        
        // 2. Customer places order
        assertTrue(customerController.placeOrder());
        
        // 3. Staff processes order
        StaffController staffController = new StaffController(testStaff);
        List<Order> pendingOrders = staffController.getOrdersByStatus("Pending");
        assertFalse(pendingOrders.isEmpty());
        
        String orderId = pendingOrders.get(0).getOrderId();
        assertTrue(staffController.processOrder(orderId));
        
        // 4. Verify inventory updated
        Plant plant = staffController.getPlantById("plant1");
        assertEquals(8, plant.getQuantity()); // Original 10 - 2 ordered
    }
}
```

### UI Testing
```java
public class LoginViewTest {
    private LoginView loginView;
    private Robot robot;
    
    @BeforeEach
    public void setUp() throws AWTException {
        loginView = new LoginView();
        robot = new Robot();
        SwingUtilities.invokeLater(() -> loginView.setVisible(true));
    }
    
    @Test
    public void testLoginWithValidCredentials() {
        // Simulate user input
        JTextField usernameField = findComponent(loginView, JTextField.class);
        JPasswordField passwordField = findComponent(loginView, JPasswordField.class);
        JButton loginButton = findComponent(loginView, JButton.class, "Login");
        
        // Enter credentials
        usernameField.setText("admin");
        passwordField.setText("admin123");
        
        // Click login
        robot.mouseMove(loginButton.getLocationOnScreen().x, 
                       loginButton.getLocationOnScreen().y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        
        // Verify result
        // Implementation depends on expected behavior
    }
}
```

## Deployment Guide

### Build Process
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Create distribution
mvn assembly:single
```

### Maven Configuration
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>com.greenthumb.GreenthumbNurseryApp</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Deployment Checklist
1. **Pre-deployment**:
   - [ ] All tests pass
   - [ ] Database schema is up to date
   - [ ] Configuration files are correct
   - [ ] Dependencies are included

2. **Database Setup**:
   - [ ] MySQL server installed and running
   - [ ] Database created with correct schema
   - [ ] Sample data loaded (if required)
   - [ ] User permissions configured

3. **Application Deployment**:
   - [ ] JAR file copied to target system
   - [ ] Java runtime environment available
   - [ ] Database connection tested
   - [ ] Application starts successfully

4. **Post-deployment**:
   - [ ] Login functionality verified
   - [ ] Core features tested
   - [ ] Performance acceptable
   - [ ] Error handling working
   - [ ] Logs being generated

### Production Configuration
```java
public class ProductionConfig {
    // Database settings
    public static final String DB_URL = System.getProperty("db.url", 
        "jdbc:mysql://localhost:3306/greenthumb_nursery");
    public static final String DB_USERNAME = System.getProperty("db.username", "root");
    public static final String DB_PASSWORD = System.getProperty("db.password", "");
    
    // Application settings
    public static final int CONNECTION_POOL_SIZE = Integer.parseInt(
        System.getProperty("connection.pool.size", "10"));
    public static final long CACHE_EXPIRY_MS = Long.parseLong(
        System.getProperty("cache.expiry.ms", "300000")); // 5 minutes
    
    // Security settings
    public static final boolean ENABLE_LOGGING = Boolean.parseBoolean(
        System.getProperty("enable.logging", "true"));
    public static final String LOG_LEVEL = System.getProperty("log.level", "INFO");
}
```

This technical documentation provides comprehensive information for developers working with the Greenthumb Nursery Management System. It covers architecture, implementation details, and deployment procedures necessary for maintaining and extending the application.

