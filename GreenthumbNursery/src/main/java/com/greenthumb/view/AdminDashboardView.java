package com.greenthumb.view;

import com.greenthumb.controller.AdminController;
import com.greenthumb.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Admin dashboard view for the Greenthumb Nursery application.
 * Provides comprehensive management interface for administrators.
 */
public class AdminDashboardView extends JFrame {
    private AdminController adminController;
    private JTabbedPane tabbedPane;
    
    // User Management Components
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JTextField userIdField, usernameField, passwordField;
    private JComboBox<String> roleComboBox;
    
    // Plant Management Components
    private JTable plantTable;
    private DefaultTableModel plantTableModel;
    private JTextField plantIdField, plantNameField, plantTypeField, plantPriceField, plantQuantityField;
    private JTextArea plantDescriptionArea;
    
    // Order Management Components
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JComboBox<String> orderStatusComboBox;
    
    // Report Components
    private JTextArea reportArea;

    public AdminDashboardView(AdminController adminController) {
        this.adminController = adminController;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
        loadInitialData();
    }

    /**
     * Initialize all Swing components
     */
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Initialize table models
        userTableModel = new DefaultTableModel(new String[]{"User ID", "Username", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(userTableModel);
        
        plantTableModel = new DefaultTableModel(new String[]{"Plant ID", "Name", "Type", "Price", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        plantTable = new JTable(plantTableModel);
        
        orderTableModel = new DefaultTableModel(new String[]{"Order ID", "Customer ID", "Date", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        
        // Initialize form components
        userIdField = new JTextField(15);
        usernameField = new JTextField(15);
        passwordField = new JTextField(15);
        roleComboBox = new JComboBox<>(new String[]{"Admin", "Staff", "Customer"});
        
        plantIdField = new JTextField(15);
        plantNameField = new JTextField(15);
        plantTypeField = new JTextField(15);
        plantPriceField = new JTextField(15);
        plantQuantityField = new JTextField(15);
        plantDescriptionArea = new JTextArea(3, 15);
        plantDescriptionArea.setLineWrap(true);
        plantDescriptionArea.setWrapStyleWord(true);
        
        orderStatusComboBox = new JComboBox<>(new String[]{"Pending", "Processing", "Shipped", "Delivered", "Cancelled", "Returned"});
        
        reportArea = new JTextArea(20, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        // Setup event handlers for menu items
        logoutItem.addActionListener(e -> adminController.logout());
        exitItem.addActionListener(e -> System.exit(0));
        
        // Create tabs
        tabbedPane.addTab("User Management", createUserManagementPanel());
        tabbedPane.addTab("Plant Management", createPlantManagementPanel());
        tabbedPane.addTab("Order Management", createOrderManagementPanel());
        tabbedPane.addTab("Reports", createReportsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("Welcome, " + adminController.getCurrentUser().getUsername() + " (Admin)");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Create user management panel
     */
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(roleComboBox, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addUserBtn = new JButton("Add User");
        JButton updateUserBtn = new JButton("Update User");
        JButton deleteUserBtn = new JButton("Delete User");
        JButton refreshUserBtn = new JButton("Refresh");
        
        buttonPanel.add(addUserBtn);
        buttonPanel.add(updateUserBtn);
        buttonPanel.add(deleteUserBtn);
        buttonPanel.add(refreshUserBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Setup button actions
        addUserBtn.addActionListener(e -> addUser());
        updateUserBtn.addActionListener(e -> updateUser());
        deleteUserBtn.addActionListener(e -> deleteUser());
        refreshUserBtn.addActionListener(e -> loadUserData());
        
        // Setup table selection listener
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateUserForm();
            }
        });
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create plant management panel
     */
    private JPanel createPlantManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(plantTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Plant Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Plant ID:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantTypeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantPriceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantQuantityField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JScrollPane(plantDescriptionArea), gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPlantBtn = new JButton("Add Plant");
        JButton updatePlantBtn = new JButton("Update Plant");
        JButton deletePlantBtn = new JButton("Delete Plant");
        JButton refreshPlantBtn = new JButton("Refresh");
        
        buttonPanel.add(addPlantBtn);
        buttonPanel.add(updatePlantBtn);
        buttonPanel.add(deletePlantBtn);
        buttonPanel.add(refreshPlantBtn);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Setup button actions
        addPlantBtn.addActionListener(e -> addPlant());
        updatePlantBtn.addActionListener(e -> updatePlant());
        deletePlantBtn.addActionListener(e -> deletePlant());
        refreshPlantBtn.addActionListener(e -> loadPlantData());
        
        // Setup table selection listener
        plantTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populatePlantForm();
            }
        });
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create order management panel
     */
    private JPanel createOrderManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(orderTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 400));
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Order Management"));
        
        JLabel statusLabel = new JLabel("Update Status:");
        JButton updateStatusBtn = new JButton("Update Status");
        JButton refreshOrderBtn = new JButton("Refresh");
        JButton viewDetailsBtn = new JButton("View Details");
        
        controlPanel.add(statusLabel);
        controlPanel.add(orderStatusComboBox);
        controlPanel.add(updateStatusBtn);
        controlPanel.add(viewDetailsBtn);
        controlPanel.add(refreshOrderBtn);
        
        // Setup button actions
        updateStatusBtn.addActionListener(e -> updateOrderStatus());
        refreshOrderBtn.addActionListener(e -> loadOrderData());
        viewDetailsBtn.addActionListener(e -> viewOrderDetails());
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create reports panel
     */
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton userReportBtn = new JButton("User Report");
        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton salesReportBtn = new JButton("Sales Report");
        JButton clearReportBtn = new JButton("Clear");
        
        buttonPanel.add(userReportBtn);
        buttonPanel.add(inventoryReportBtn);
        buttonPanel.add(salesReportBtn);
        buttonPanel.add(clearReportBtn);
        
        // Setup button actions
        userReportBtn.addActionListener(e -> generateUserReport());
        inventoryReportBtn.addActionListener(e -> generateInventoryReport());
        salesReportBtn.addActionListener(e -> generateSalesReport());
        clearReportBtn.addActionListener(e -> reportArea.setText(""));
        
        // Report display area
        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        reportScrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(reportScrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Window closing event
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(
                    AdminDashboardView.this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Set frame properties
     */
    private void setFrameProperties() {
        setTitle("Greenthumb Nursery - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }

    /**
     * Load initial data
     */
    private void loadInitialData() {
        loadUserData();
        loadPlantData();
        loadOrderData();
    }

    // User Management Methods
    private void loadUserData() {
        userTableModel.setRowCount(0);
        List<User> users = adminController.getAllUsers();
        if (users != null) {
            for (User user : users) {
                userTableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole()
                });
            }
        }
    }

    private void populateUserForm() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            userIdField.setText((String) userTableModel.getValueAt(selectedRow, 0));
            usernameField.setText((String) userTableModel.getValueAt(selectedRow, 1));
            roleComboBox.setSelectedItem(userTableModel.getValueAt(selectedRow, 2));
            passwordField.setText(""); // Don't populate password for security
        }
    }

    private void addUser() {
        String userId = userIdField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        
        if (userId.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (adminController.createUser(userId, username, password, role)) {
            clearUserForm();
            loadUserData();
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String userId = userIdField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        
        // Create user object based on role
        User user;
        switch (role) {
            case "Admin":
                user = new Admin(userId, username, password);
                break;
            case "Staff":
                user = new Staff(userId, username, password);
                break;
            case "Customer":
                user = new Customer(userId, username, password, null, null, null);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid role selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }
        
        if (adminController.updateUser(user)) {
            clearUserForm();
            loadUserData();
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String userId = (String) userTableModel.getValueAt(selectedRow, 0);
        if (adminController.deleteUser(userId)) {
            clearUserForm();
            loadUserData();
        }
    }

    private void clearUserForm() {
        userIdField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    // Plant Management Methods
    private void loadPlantData() {
        plantTableModel.setRowCount(0);
        List<Plant> plants = adminController.getAllPlants();
        if (plants != null) {
            for (Plant plant : plants) {
                plantTableModel.addRow(new Object[]{
                    plant.getPlantId(),
                    plant.getName(),
                    plant.getType(),
                    String.format("$%.2f", plant.getPrice()),
                    plant.getQuantity()
                });
            }
        }
    }

    private void populatePlantForm() {
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow >= 0) {
            plantIdField.setText((String) plantTableModel.getValueAt(selectedRow, 0));
            plantNameField.setText((String) plantTableModel.getValueAt(selectedRow, 1));
            plantTypeField.setText((String) plantTableModel.getValueAt(selectedRow, 2));
            String priceStr = (String) plantTableModel.getValueAt(selectedRow, 3);
            plantPriceField.setText(priceStr.replace("$", ""));
            plantQuantityField.setText(plantTableModel.getValueAt(selectedRow, 4).toString());
            
            // Load description from database
            String plantId = (String) plantTableModel.getValueAt(selectedRow, 0);
            List<Plant> plants = adminController.getAllPlants();
            if (plants != null) {
                for (Plant plant : plants) {
                    if (plant.getPlantId().equals(plantId)) {
                        plantDescriptionArea.setText(plant.getDescription());
                        break;
                    }
                }
            }
        }
    }

    private void addPlant() {
        try {
            String plantId = plantIdField.getText().trim();
            String name = plantNameField.getText().trim();
            String type = plantTypeField.getText().trim();
            double price = Double.parseDouble(plantPriceField.getText().trim());
            int quantity = Integer.parseInt(plantQuantityField.getText().trim());
            String description = plantDescriptionArea.getText().trim();
            
            if (plantId.isEmpty() || name.isEmpty() || type.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (adminController.createPlant(plantId, name, type, price, quantity, description)) {
                clearPlantForm();
                loadPlantData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePlant() {
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a plant to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String plantId = plantIdField.getText().trim();
            String name = plantNameField.getText().trim();
            String type = plantTypeField.getText().trim();
            double price = Double.parseDouble(plantPriceField.getText().trim());
            int quantity = Integer.parseInt(plantQuantityField.getText().trim());
            String description = plantDescriptionArea.getText().trim();
            
            Plant plant = new Plant(plantId, name, type, price, quantity, description);
            
            if (adminController.updatePlant(plant)) {
                clearPlantForm();
                loadPlantData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePlant() {
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a plant to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String plantId = (String) plantTableModel.getValueAt(selectedRow, 0);
        if (adminController.deletePlant(plantId)) {
            clearPlantForm();
            loadPlantData();
        }
    }

    private void clearPlantForm() {
        plantIdField.setText("");
        plantNameField.setText("");
        plantTypeField.setText("");
        plantPriceField.setText("");
        plantQuantityField.setText("");
        plantDescriptionArea.setText("");
    }

    // Order Management Methods
    private void loadOrderData() {
        orderTableModel.setRowCount(0);
        List<Order> orders = adminController.getAllOrders();
        if (orders != null) {
            for (Order order : orders) {
                orderTableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getOrderDate(),
                    String.format("$%.2f", order.getTotalAmount()),
                    order.getStatus()
                });
            }
        }
    }

    private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String orderId = (String) orderTableModel.getValueAt(selectedRow, 0);
        String newStatus = (String) orderStatusComboBox.getSelectedItem();
        
        if (adminController.updateOrderStatus(orderId, newStatus)) {
            loadOrderData();
        }
    }

    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to view details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String orderId = (String) orderTableModel.getValueAt(selectedRow, 0);
        // This would open a detailed order view dialog
        JOptionPane.showMessageDialog(this, "Order details for: " + orderId + "\n(Detailed view would be implemented here)", "Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // Report Methods
    private void generateUserReport() {
        String report = adminController.generateUserReport();
        reportArea.setText(report);
    }

    private void generateInventoryReport() {
        String report = adminController.generateInventoryReport();
        reportArea.setText(report);
    }

    private void generateSalesReport() {
        String report = adminController.generateSalesReport();
        reportArea.setText(report);
    }
}

