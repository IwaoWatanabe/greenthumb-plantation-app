# Greenthumb Nursery Management System - Installation Guide

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Pre-Installation Checklist](#pre-installation-checklist)
3. [MySQL Database Setup](#mysql-database-setup)
4. [Application Installation](#application-installation)
5. [Configuration](#configuration)
6. [First Run](#first-run)
7. [Troubleshooting](#troubleshooting)
8. [Verification](#verification)

## System Requirements

### Minimum Requirements
- **Operating System**: Windows 10, macOS 10.14, or Linux (Ubuntu 18.04+)
- **Java Runtime Environment**: JRE 11 or higher
- **Memory**: 4GB RAM
- **Storage**: 500MB free disk space
- **Display**: 1024x768 resolution
- **Database**: MySQL 8.0 or higher

### Recommended Requirements
- **Operating System**: Windows 11, macOS 12+, or Linux (Ubuntu 20.04+)
- **Java Runtime Environment**: JRE 17 or higher
- **Memory**: 8GB RAM
- **Storage**: 1GB free disk space
- **Display**: 1920x1080 resolution
- **Database**: MySQL 8.0.33 or higher

## Pre-Installation Checklist

Before installing the Greenthumb Nursery Management System, ensure you have:

- [ ] Administrative privileges on your computer
- [ ] Stable internet connection for downloading components
- [ ] MySQL server installation media or internet access
- [ ] Java JDK/JRE 11+ installed
- [ ] MySQL Workbench (recommended for database management)

## MySQL Database Setup

### Step 1: Install MySQL Server

#### Windows Installation
1. Download MySQL Installer from [MySQL Official Website](https://dev.mysql.com/downloads/installer/)
2. Run the installer as administrator
3. Choose "Developer Default" setup type
4. Follow the installation wizard
5. Set root password (remember this password)
6. Complete the installation

#### macOS Installation
1. Download MySQL Community Server for macOS
2. Install the .dmg package
3. Set root password during installation
4. Start MySQL server from System Preferences

#### Linux Installation (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

### Step 2: Verify MySQL Installation
1. Open command prompt/terminal
2. Test MySQL connection:
```bash
mysql -u root -p
```
3. Enter your root password
4. If successful, you'll see the MySQL prompt

### Step 3: Create Database
1. Connect to MySQL as root
2. Execute the following commands:
```sql
CREATE DATABASE greenthumb_nursery;
USE greenthumb_nursery;
```

### Step 4: Import Database Schema
1. Download the `greenthumb_nursery.sql` file
2. Import the schema:
```bash
mysql -u root -p greenthumb_nursery < greenthumb_nursery.sql
```

Or using MySQL Workbench:
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Go to Server → Data Import
4. Select "Import from Self-Contained File"
5. Choose the `greenthumb_nursery.sql` file
6. Click "Start Import"

## Application Installation

### Method 1: Using Pre-built JAR (Recommended)

#### Step 1: Download Application
1. Download `greenthumb-nursery-1.0.0.jar` from the release package
2. Create a folder for the application (e.g., `C:\GreenthumbNursery` or `~/GreenthumbNursery`)
3. Place the JAR file in this folder

#### Step 2: Verify Java Installation
```bash
java -version
```
Ensure Java 11 or higher is installed.

#### Step 3: Test Application
```bash
java -jar greenthumb-nursery-1.0.0.jar
```

### Method 2: Building from Source

#### Prerequisites
- Maven 3.6 or higher
- Git (optional, for cloning repository)

#### Step 1: Download Source Code
Either download the source code ZIP file or clone the repository:
```bash
git clone <repository-url>
cd GreenthumbNursery
```

#### Step 2: Build Application
```bash
mvn clean compile
mvn package
```

#### Step 3: Locate Built JAR
The compiled JAR will be in the `target` directory:
- `target/greenthumb-nursery-1.0.0.jar`

## Configuration

### Database Connection Configuration

#### Step 1: Locate Configuration File
The database configuration is in:
`src/main/java/com/greenthumb/util/DBConnection.java`

#### Step 2: Update Database Settings
Edit the following constants:
```java
private static final String URL = "jdbc:mysql://localhost:3306/greenthumb_nursery";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_mysql_password";
```

#### Step 3: Recompile (if building from source)
```bash
mvn clean package
```

### Alternative Configuration Methods

#### Using Environment Variables
Set the following environment variables:
```bash
export DB_URL=jdbc:mysql://localhost:3306/greenthumb_nursery
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

#### Using Command Line Arguments
```bash
java -Ddb.url=jdbc:mysql://localhost:3306/greenthumb_nursery \
     -Ddb.username=root \
     -Ddb.password=your_password \
     -jar greenthumb-nursery-1.0.0.jar
```

## First Run

### Step 1: Start MySQL Server
Ensure MySQL server is running:

**Windows**: Start MySQL service from Services panel
**macOS**: Start from System Preferences → MySQL
**Linux**: 
```bash
sudo systemctl start mysql
```

### Step 2: Launch Application
```bash
java -jar greenthumb-nursery-1.0.0.jar
```

### Step 3: Initial Login
Use the default credentials:

**Admin Login**:
- Username: `admin`
- Password: `admin123`

**Staff Login**:
- Username: `staff1`
- Password: `staff123`

**Customer Login**:
- Username: `customer1`
- Password: `customer123`

### Step 4: Change Default Passwords
1. Login as admin
2. Go to User Management
3. Update passwords for all default accounts

## Troubleshooting

### Common Issues and Solutions

#### Issue: "Database connection failed"
**Symptoms**: Error message on application startup
**Solutions**:
1. Verify MySQL server is running
2. Check database credentials in configuration
3. Ensure database `greenthumb_nursery` exists
4. Test connection manually:
```bash
mysql -u root -p -h localhost greenthumb_nursery
```

#### Issue: "MySQL JDBC Driver not found"
**Symptoms**: ClassNotFoundException on startup
**Solutions**:
1. Ensure MySQL Connector/J is in classpath
2. If building from source, run `mvn clean package`
3. Download mysql-connector-java-8.0.33.jar manually

#### Issue: "Access denied for user"
**Symptoms**: Authentication error when connecting to database
**Solutions**:
1. Verify MySQL username and password
2. Check user privileges:
```sql
GRANT ALL PRIVILEGES ON greenthumb_nursery.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

#### Issue: Application window appears corrupted
**Symptoms**: UI elements misaligned or missing
**Solutions**:
1. Update Java to latest version
2. Try different look and feel
3. Check display scaling settings
4. Increase JVM memory:
```bash
java -Xmx2g -jar greenthumb-nursery-1.0.0.jar
```

#### Issue: "Port 3306 already in use"
**Symptoms**: Cannot start MySQL server
**Solutions**:
1. Check if MySQL is already running
2. Change MySQL port in configuration
3. Stop conflicting services

#### Issue: Slow application performance
**Symptoms**: Long loading times, unresponsive UI
**Solutions**:
1. Increase JVM heap size:
```bash
java -Xms512m -Xmx2g -jar greenthumb-nursery-1.0.0.jar
```
2. Optimize MySQL configuration
3. Check available system memory

### Advanced Troubleshooting

#### Enable Debug Logging
Add the following JVM arguments:
```bash
java -Djava.util.logging.config.file=logging.properties \
     -jar greenthumb-nursery-1.0.0.jar
```

#### Database Connection Testing
Create a test script to verify database connectivity:
```java
import java.sql.*;
public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/greenthumb_nursery",
                "root", "your_password");
            System.out.println("Connection successful!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

## Verification

### Step 1: Database Verification
Connect to MySQL and verify tables exist:
```sql
USE greenthumb_nursery;
SHOW TABLES;
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM plants;
```

Expected output:
- 4 tables: users, plants, orders, order_items
- At least 3 users (admin, staff, customer)
- Sample plant data

### Step 2: Application Functionality Test
1. **Login Test**: Try logging in with each user type
2. **Admin Test**: Create a new user, add a plant
3. **Staff Test**: Update plant quantity, process an order
4. **Customer Test**: Browse plants, add to cart, place order

### Step 3: Performance Verification
- Application startup time should be under 10 seconds
- Database operations should complete within 2 seconds
- UI should be responsive without lag

### Step 4: Security Verification
1. Verify SQL injection protection
2. Test password validation
3. Confirm role-based access control

## Post-Installation Steps

### Step 1: Backup Configuration
Create backups of:
- Database configuration files
- Application JAR file
- Database dump:
```bash
mysqldump -u root -p greenthumb_nursery > backup.sql
```

### Step 2: Create Desktop Shortcut
**Windows**:
Create a batch file `start_greenthumb.bat`:
```batch
@echo off
cd /d "C:\path\to\application"
java -jar greenthumb-nursery-1.0.0.jar
```

**macOS/Linux**:
Create a shell script `start_greenthumb.sh`:
```bash
#!/bin/bash
cd /path/to/application
java -jar greenthumb-nursery-1.0.0.jar
```

### Step 3: Schedule Regular Backups
Set up automated database backups:
```bash
# Add to crontab for daily backup at 2 AM
0 2 * * * mysqldump -u root -p'password' greenthumb_nursery > /backup/greenthumb_$(date +\%Y\%m\%d).sql
```

## Support and Maintenance

### Regular Maintenance Tasks
1. **Weekly**: Backup database
2. **Monthly**: Update Java runtime if needed
3. **Quarterly**: Review and update user accounts
4. **Annually**: Update MySQL server

### Getting Help
1. Check this installation guide
2. Review application logs
3. Consult MySQL documentation
4. Check Java compatibility

### Updating the Application
1. Backup current database
2. Stop the application
3. Replace JAR file with new version
4. Run database migration scripts if provided
5. Test functionality before production use

This installation guide provides comprehensive instructions for setting up the Greenthumb Nursery Management System. Follow each step carefully and refer to the troubleshooting section if you encounter any issues.

