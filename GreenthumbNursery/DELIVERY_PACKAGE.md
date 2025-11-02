# Greenthumb Nursery Management System - Delivery Package

## Package Contents

This delivery package contains the complete Greenthumb Nursery Management System, a standalone desktop application built according to SE4 5 OOMD evaluation requirements.

### 📁 Project Structure
```
GreenthumbNursery/
├── src/main/java/com/greenthumb/
│   ├── controller/                    # MVC Controllers
│   │   ├── AdminController.java
│   │   ├── CustomerController.java
│   │   ├── LoginController.java
│   │   └── StaffController.java
│   ├── dao/                          # Data Access Objects
│   │   ├── OrderDAO.java
│   │   ├── OrderDAOImpl.java
│   │   ├── OrderItemDAO.java
│   │   ├── OrderItemDAOImpl.java
│   │   ├── PlantDAO.java
│   │   ├── PlantDAOImpl.java
│   │   ├── UserDAO.java
│   │   └── UserDAOImpl.java
│   ├── model/                        # Entity Models
│   │   ├── Admin.java
│   │   ├── Customer.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Plant.java
│   │   ├── Staff.java
│   │   └── User.java
│   ├── util/                         # Utility Classes
│   │   ├── DBConnection.java
│   │   ├── ErrorHandler.java
│   │   └── ValidationUtil.java
│   ├── view/                         # Swing UI Views
│   │   ├── AdminDashboardView.java
│   │   ├── CustomerDashboardView.java
│   │   ├── LoginView.java
│   │   └── StaffDashboardView.java
│   └── GreenthumbNurseryApp.java     # Main Application Class
├── pom.xml                           # Maven Configuration
├── greenthumb_nursery.sql            # Database Schema & Sample Data
├── README.md                         # Project Overview
├── INSTALLATION_GUIDE.md             # Setup Instructions
├── USER_MANUAL.md                    # User Guide
├── TECHNICAL_DOCUMENTATION.md        # Developer Documentation
└── DELIVERY_PACKAGE.md              # This File
```

### 📊 UML Diagrams
```
Design Documentation/
├── class_diagram.png                 # Class Diagram
├── use_case_diagram.png             # Use Case Diagram
├── sequence_diagram_login.png       # Login Sequence Diagram
├── sequence_diagram_place_order.png # Order Placement Sequence
├── deployment_diagram.png           # Deployment Diagram
├── component_diagram.png            # Component Diagram
└── state_chart_order.png           # Order State Chart
```

## 🎯 Evaluation Requirements Compliance

### ✅ Object-Oriented Programming (OOP) Concepts

#### 1. Inheritance
- **User Class Hierarchy**: Abstract `User` class with concrete subclasses:
  - `Admin` extends `User`
  - `Staff` extends `User` 
  - `Customer` extends `User`
- **Polymorphism**: Abstract methods implemented differently in each subclass
- **Method Overriding**: Each user type has specific implementations of `login()`, `logout()`, and `getRole()`

#### 2. Encapsulation
- **Private Fields**: All entity classes use private fields with public getters/setters
- **Data Hiding**: Internal implementation details hidden from external classes
- **Access Modifiers**: Proper use of public, private, and protected modifiers

#### 3. Abstraction
- **Abstract Classes**: `User` class defines common interface for all user types
- **Interfaces**: DAO interfaces abstract database operations
- **Information Hiding**: Complex database operations hidden behind simple method calls

#### 4. Polymorphism
- **Runtime Polymorphism**: User objects treated uniformly regardless of specific type
- **Interface Implementation**: Multiple implementations of DAO interfaces
- **Method Overriding**: Subclass-specific behavior for inherited methods

### ✅ Model-View-Controller (MVC) Architecture

#### Model Layer
- **Entity Classes**: `User`, `Plant`, `Order`, `OrderItem` represent business data
- **Data Access Objects**: DAO pattern for database operations
- **Business Logic**: Validation and business rules in model classes

#### View Layer
- **Swing Components**: Complete UI using Java Swing framework
- **User Interface**: Separate views for each user type (Admin, Staff, Customer)
- **Event Handling**: UI events properly handled and delegated to controllers

#### Controller Layer
- **Business Logic**: Controllers coordinate between Model and View
- **Input Validation**: User input validated before processing
- **Workflow Management**: Controllers manage application flow and user interactions

### ✅ Database Integration (CRUD Operations)

#### Create Operations
- Add new users (Admin functionality)
- Add new plants to inventory (Admin functionality)
- Create new orders (Customer functionality)
- Add items to orders (Customer functionality)

#### Read Operations
- User authentication and profile retrieval
- Plant inventory browsing and searching
- Order history and status tracking
- Comprehensive reporting functionality

#### Update Operations
- User profile updates
- Plant information and quantity updates
- Order status modifications
- Password changes

#### Delete Operations
- User account deletion (Admin functionality)
- Plant removal from inventory (Admin functionality)
- Order cancellation (Customer functionality)
- Shopping cart item removal

### ✅ User Types Implementation

#### 1. Admin User
- **Complete System Control**: Full access to all functionality
- **User Management**: Create, update, delete user accounts
- **Plant Management**: Add, modify, remove plants from inventory
- **Order Management**: View and update all orders
- **Reporting**: Generate comprehensive system reports

#### 2. Staff User
- **Inventory Management**: Update plant information and quantities
- **Order Processing**: Process customer orders and update status
- **Customer Support**: View and update customer information
- **Operational Reports**: Generate inventory and order reports

#### 3. Customer User
- **Plant Browsing**: Search and view available plants
- **Shopping Cart**: Add/remove items, manage quantities
- **Order Placement**: Place orders and track status
- **Profile Management**: Update personal information

### ✅ Swing Framework Implementation

#### User Interface Components
- **JFrame**: Main application windows
- **JPanel**: Layout organization and grouping
- **JTable**: Data display with sorting and selection
- **JTextField/JPasswordField**: User input fields
- **JButton**: Action triggers and navigation
- **JComboBox**: Dropdown selections
- **JTextArea**: Multi-line text display
- **JScrollPane**: Scrollable content areas

#### Layout Management
- **BorderLayout**: Main window organization
- **GridBagLayout**: Complex form layouts
- **FlowLayout**: Button groupings
- **BoxLayout**: Vertical component stacking

#### Event Handling
- **ActionListener**: Button click handling
- **WindowListener**: Window close events
- **TableSelectionListener**: Table row selection
- **DocumentListener**: Text field changes

### ✅ Standalone Desktop Application

#### No Internet Dependency
- **Local Database**: MySQL database runs locally
- **Self-Contained**: All dependencies included in package
- **Offline Operation**: Full functionality without internet connection

#### Desktop Integration
- **Native Look and Feel**: System-appropriate UI appearance
- **Window Management**: Proper window sizing and positioning
- **Keyboard Shortcuts**: Standard desktop application shortcuts
- **File System Access**: Local file operations for reports

## 🚀 Quick Start Guide

### Prerequisites
1. **Java Development Kit (JDK) 11+**
2. **MySQL Server 8.0+**
3. **MySQL Workbench** (recommended)

### Installation Steps
1. **Extract Package**: Unzip the delivery package
2. **Setup Database**: 
   ```bash
   mysql -u root -p < greenthumb_nursery.sql
   ```
3. **Configure Database Connection**: Update credentials in `DBConnection.java`
4. **Build Application**:
   ```bash
   cd GreenthumbNursery
   mvn clean package
   ```
5. **Run Application**:
   ```bash
   java -jar target/greenthumb-nursery-1.0.0.jar
   ```

### Default Login Credentials
- **Admin**: username=`admin`, password=`admin123`
- **Staff**: username=`staff1`, password=`staff123`
- **Customer**: username=`customer1`, password=`customer123`

## 📋 Testing Checklist

### Functional Testing
- [ ] User authentication for all user types
- [ ] CRUD operations for all entities
- [ ] Role-based access control
- [ ] Input validation and error handling
- [ ] Database connectivity and transactions

### User Interface Testing
- [ ] All forms display correctly
- [ ] Tables show data properly
- [ ] Buttons and menus function
- [ ] Error messages appear appropriately
- [ ] Navigation between screens works

### Integration Testing
- [ ] Database operations complete successfully
- [ ] User workflows function end-to-end
- [ ] Data consistency maintained
- [ ] Concurrent user operations handled

## 📚 Documentation

### For Users
- **README.md**: Project overview and basic setup
- **INSTALLATION_GUIDE.md**: Detailed installation instructions
- **USER_MANUAL.md**: Comprehensive user guide for all user types

### For Developers
- **TECHNICAL_DOCUMENTATION.md**: Architecture, design patterns, and implementation details
- **Source Code**: Fully commented Java classes with JavaDoc
- **UML Diagrams**: Visual representation of system design

## 🔧 Technical Specifications

### Development Environment
- **Language**: Java 11+
- **Framework**: Swing for GUI
- **Database**: MySQL 8.0+
- **Build Tool**: Maven 3.6+
- **IDE Compatibility**: NetBeans, IntelliJ IDEA, Eclipse

### Architecture Patterns
- **MVC**: Model-View-Controller separation
- **DAO**: Data Access Object pattern
- **Singleton**: Database connection management
- **Factory**: User object creation

### Quality Assurance
- **Input Validation**: Comprehensive validation for all user inputs
- **Error Handling**: Graceful error handling with user-friendly messages
- **Security**: SQL injection prevention, password protection
- **Performance**: Efficient database queries and UI responsiveness

## 📞 Support Information

### Troubleshooting
1. **Database Connection Issues**: Check MySQL server status and credentials
2. **Application Won't Start**: Verify Java version and classpath
3. **UI Display Problems**: Update Java runtime and check display settings
4. **Performance Issues**: Increase JVM memory allocation

### Common Solutions
- **MySQL Connection**: Ensure MySQL service is running
- **Java Version**: Use JDK/JRE 11 or higher
- **Memory Issues**: Run with `-Xmx2g` flag for more memory
- **Display Issues**: Try different look and feel settings

## 📝 Submission Notes

### Academic Requirements Met
- ✅ **Standalone Desktop Application**: No internet dependency
- ✅ **Object-Oriented Programming**: Full OOP implementation with inheritance, encapsulation, polymorphism, and abstraction
- ✅ **Multiple User Types**: Admin, Staff, and Customer with role-based access
- ✅ **MVC Architecture**: Clear separation of Model, View, and Controller layers
- ✅ **Swing Framework**: Complete UI implementation using Java Swing
- ✅ **Database Integration**: Full CRUD operations with MySQL
- ✅ **Comprehensive Documentation**: User manual, technical documentation, and setup guides

### Deliverables Included
1. **Complete Source Code**: All Java classes with proper documentation
2. **Database Schema**: SQL script with tables and sample data
3. **UML Diagrams**: Class, Use Case, Sequence, Deployment, Component, and State diagrams
4. **Documentation**: Installation guide, user manual, and technical documentation
5. **Build Configuration**: Maven POM file for easy compilation and packaging
6. **Test Data**: Sample users, plants, and orders for demonstration

This delivery package represents a complete, professional-grade desktop application that fully satisfies the SE4 5 OOMD evaluation requirements while demonstrating best practices in software engineering and object-oriented design.

