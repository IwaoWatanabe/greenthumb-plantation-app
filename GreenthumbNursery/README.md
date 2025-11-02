# Greenthumb Nursery Management System

A comprehensive desktop application for nursery management built with Java Swing and MySQL database. This application demonstrates proper Object-Oriented Programming (OOP) concepts, Model-View-Controller (MVC) architecture, and complete CRUD operations.

## Features

### User Management
- **Three User Types**: Admin, Staff, and Customer with role-based access control
- **Secure Authentication**: Login system with password protection
- **User Profile Management**: Update personal information and change passwords

### Admin Features
- Complete user management (Create, Read, Update, Delete users)
- Full plant inventory management
- Order management and status updates
- Comprehensive reporting (User, Inventory, Sales reports)
- System administration capabilities

### Staff Features
- Plant inventory management and stock updates
- Order processing and status management
- Customer information management
- Inventory and order reports
- Low stock alerts

### Customer Features
- Browse available plants with search functionality
- Shopping cart management
- Place and track orders
- Order history and details
- Profile management

## Technical Architecture

### Design Patterns
- **MVC (Model-View-Controller)**: Clear separation of concerns
- **DAO (Data Access Object)**: Database abstraction layer
- **Singleton**: Database connection management

### OOP Concepts Demonstrated
- **Inheritance**: User class hierarchy (Admin, Staff, Customer)
- **Encapsulation**: Private fields with public getters/setters
- **Polymorphism**: Abstract methods implemented differently in subclasses
- **Abstraction**: Interface-based design for DAO layer

### Technology Stack
- **Frontend**: Java Swing for desktop GUI
- **Backend**: Java with JDBC for database connectivity
- **Database**: MySQL for data persistence
- **Build Tool**: Maven for dependency management
- **Architecture**: Standalone desktop application (no internet dependency)

## Prerequisites

### Software Requirements
- Java Development Kit (JDK) 11 or higher
- MySQL Server 8.0 or higher
- Maven 3.6 or higher (optional, for building from source)
- MySQL Workbench (recommended for database management)

### Hardware Requirements
- Minimum 4GB RAM
- 500MB free disk space
- 1024x768 screen resolution or higher

## Installation and Setup

### 1. Database Setup

#### Step 1: Install MySQL Server
- Download and install MySQL Server from [MySQL Official Website](https://dev.mysql.com/downloads/mysql/)
- During installation, set the root password (remember this for configuration)

#### Step 2: Create Database
1. Open MySQL Workbench or command line
2. Connect to MySQL server as root
3. Execute the SQL script provided in `greenthumb_nursery.sql`:

```sql
-- Run the entire greenthumb_nursery.sql file
-- This will create the database, tables, and sample data
```

#### Step 3: Verify Database Setup
```sql
USE greenthumb_nursery;
SHOW TABLES;
SELECT * FROM users;
```

### 2. Application Configuration

#### Step 1: Update Database Configuration
Edit the file `src/main/java/com/greenthumb/util/DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/greenthumb_nursery";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_mysql_password"; // Update this
```

#### Step 2: Download MySQL JDBC Driver
If not using Maven, download `mysql-connector-java-8.0.33.jar` and add to classpath.

### 3. Building and Running

#### Option A: Using Pre-built JAR (Recommended)
1. Download the pre-built JAR file: `greenthumb-nursery-1.0.0.jar`
2. Ensure MySQL server is running
3. Run the application:
```bash
java -jar greenthumb-nursery-1.0.0.jar
```

#### Option B: Building from Source
1. Clone or download the source code
2. Navigate to the project directory
3. Build using Maven:
```bash
mvn clean compile
mvn package
```
4. Run the application:
```bash
java -cp target/greenthumb-nursery-1.0.0.jar com.greenthumb.GreenthumbNurseryApp
```

#### Option C: Using IDE (NetBeans/IntelliJ/Eclipse)
1. Import the project as a Maven project
2. Ensure MySQL JDBC driver is in classpath
3. Run the main class: `com.greenthumb.GreenthumbNurseryApp`

## Default Login Credentials

### Admin User
- **Username**: admin
- **Password**: admin123

### Staff User
- **Username**: staff1
- **Password**: staff123

### Customer User
- **Username**: customer1
- **Password**: customer123

## Usage Guide

### First Time Setup
1. Start the application
2. The splash screen will appear, followed by database connection test
3. If database connection fails, check your MySQL server and configuration
4. Use the default login credentials to access the system

### Admin Workflow
1. Login as admin
2. Manage users: Create staff and customer accounts
3. Manage plants: Add new plants to inventory
4. Process orders: Monitor and update order status
5. Generate reports: View system analytics

### Staff Workflow
1. Login as staff
2. Update plant inventory and quantities
3. Process customer orders
4. Update order status (Pending → Processing → Shipped)
5. Generate inventory and order reports

### Customer Workflow
1. Login as customer
2. Browse available plants
3. Add plants to shopping cart
4. Place orders
5. Track order status
6. Update profile information

## Database Schema

### Tables
- **users**: User authentication and basic information
- **plants**: Plant inventory with details and pricing
- **orders**: Customer orders with status tracking
- **order_items**: Individual items within orders

### Relationships
- Users (1) → Orders (Many)
- Orders (1) → Order Items (Many)
- Plants (1) → Order Items (Many)

## Troubleshooting

### Common Issues

#### Database Connection Error
**Problem**: "Failed to connect to the database"
**Solutions**:
1. Verify MySQL server is running
2. Check database credentials in `DBConnection.java`
3. Ensure database `greenthumb_nursery` exists
4. Verify MySQL JDBC driver is in classpath

#### Login Failed
**Problem**: "Invalid username or password"
**Solutions**:
1. Use default credentials provided above
2. Check if sample data was inserted correctly
3. Reset database using the SQL script

#### Application Won't Start
**Problem**: Application crashes on startup
**Solutions**:
1. Verify Java version (JDK 11+)
2. Check all dependencies are available
3. Review console output for error messages

#### UI Display Issues
**Problem**: Interface appears broken or misaligned
**Solutions**:
1. Ensure screen resolution is at least 1024x768
2. Try different look and feel settings
3. Update Java to latest version

### Performance Optimization
- Increase JVM heap size for large datasets: `-Xmx2g`
- Optimize MySQL configuration for better performance
- Use connection pooling for high-concurrency scenarios

## Development

### Project Structure
```
GreenthumbNursery/
├── src/main/java/com/greenthumb/
│   ├── controller/          # MVC Controllers
│   ├── dao/                # Data Access Objects
│   ├── model/              # Entity Models
│   ├── util/               # Utility Classes
│   ├── view/               # Swing UI Views
│   └── GreenthumbNurseryApp.java
├── pom.xml                 # Maven Configuration
├── greenthumb_nursery.sql  # Database Schema
└── README.md              # This file
```

### Adding New Features
1. Create model classes in `model` package
2. Implement DAO interfaces and implementations
3. Create controller classes for business logic
4. Design Swing views for user interface
5. Update main application class if needed

### Testing
- Unit tests can be added in `src/test/java`
- Use JUnit 5 framework (already configured in Maven)
- Test database operations with test database

## Contributing

### Code Style
- Follow Java naming conventions
- Use proper JavaDoc comments
- Maintain MVC architecture separation
- Implement proper error handling

### Submission Requirements
- Include all source code files
- Provide complete database schema
- Document any configuration changes
- Test on clean environment before submission

## License

This project is developed for educational purposes as part of SE4 5 OOMD coursework.

## Support

For technical support or questions:
1. Check the troubleshooting section above
2. Verify all prerequisites are met
3. Review console output for error messages
4. Ensure database setup is correct

## Version History

### Version 1.0.0
- Initial release
- Complete MVC implementation
- Full CRUD operations
- Three user types with role-based access
- Comprehensive Swing UI
- MySQL database integration
- Standalone desktop application

