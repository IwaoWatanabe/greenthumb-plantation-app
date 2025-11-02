package com.greenthumb.view;

import com.greenthumb.controller.StaffController;
import com.greenthumb.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Staff dashboard view for the Greenthumb Nursery application.
 * Provides interface for staff to manage inventory and process orders.
 */
public class StaffDashboardView extends JFrame {
    private StaffController staffController;
    private JTabbedPane tabbedPane;
    
    // Plant Management Components
    private JTable plantTable;
    private DefaultTableModel plantTableModel;
    private JTextField plantIdField, plantNameField, plantTypeField, plantPriceField, plantQuantityField;
    private JTextArea plantDescriptionArea;
    private JTextField searchNameField, searchTypeField, searchMinPriceField, searchMaxPriceField;
    
    // Order Management Components
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JComboBox<String> orderStatusFilter;
    private JComboBox<String> orderStatusComboBox;
    
    // Customer Management Components
    private JTable customerTable;
    private DefaultTableModel customerTableModel;
    private JTextField customerIdField, customerUsernameField, customerAddressField, customerPhoneField;
    
    // Report Components
    private JTextArea reportArea;

    public StaffDashboardView(StaffController staffController) {
        this.staffController = staffController;
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
        
        customerTableModel = new DefaultTableModel(new String[]{"Customer ID", "Username", "Address", "Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(customerTableModel);
        
        // Initialize form components
        plantIdField = new JTextField(15);
        plantNameField = new JTextField(15);
        plantTypeField = new JTextField(15);
        plantPriceField = new JTextField(15);
        plantQuantityField = new JTextField(15);
        plantDescriptionArea = new JTextArea(3, 15);
        plantDescriptionArea.setLineWrap(true);
        plantDescriptionArea.setWrapStyleWord(true);
        
        // Search components
        searchNameField = new JTextField(10);
        searchTypeField = new JTextField(10);
        searchMinPriceField = new JTextField(8);
        searchMaxPriceField = new JTextField(8);
        
        // Order components
        orderStatusFilter = new JComboBox<>(new String[]{"All", "Pending", "Processing", "Shipped", "Delivered", "Cancelled"});
        orderStatusComboBox = new JComboBox<>(new String[]{"Pending", "Processing", "Shipped", "Delivered", "Cancelled", "Returned"});
        
        // Customer components
        customerIdField = new JTextField(15);
        customerUsernameField = new JTextField(15);
        customerAddressField = new JTextField(15);
        customerPhoneField = new JTextField(15);
        
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
        logoutItem.addActionListener(e -> staffController.logout());
        exitItem.addActionListener(e -> System.exit(0));
        
        // Create tabs
        tabbedPane.addTab("Plant Inventory", createPlantManagementPanel());
        tabbedPane.addTab("Order Processing", createOrderManagementPanel());
        tabbedPane.addTab("Customer Info", createCustomerManagementPanel());
        tabbedPane.addTab("Reports", createReportsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("Welcome, " + staffController.getCurrentUser().getUsername() + " (Staff)");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Create plant management panel
     */
    private JPanel createPlantManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Plants"));
        
        searchPanel.add(new JLabel("Name:"));
        searchPanel.add(searchNameField);
        searchPanel.add(new JLabel("Type:"));
        searchPanel.add(searchTypeField);
        searchPanel.add(new JLabel("Min Price:"));
        searchPanel.add(searchMinPriceField);
        searchPanel.add(new JLabel("Max Price:"));
        searchPanel.add(searchMaxPriceField);
        
        JButton searchBtn = new JButton("Search");
        JButton clearSearchBtn = new JButton("Clear");
        JButton lowStockBtn = new JButton("Low Stock");
        
        searchPanel.add(searchBtn);
        searchPanel.add(clearSearchBtn);
        searchPanel.add(lowStockBtn);
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(plantTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 250));
        
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
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantTypeField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantPriceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(plantQuantityField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JScrollPane(plantDescriptionArea), gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updatePlantBtn = new JButton("Update Plant");
        JButton updateQuantityBtn = new JButton("Update Quantity");
        JButton refreshPlantBtn = new JButton("Refresh");
        
        buttonPanel.add(updatePlantBtn);
        buttonPanel.add(updateQuantityBtn);
        buttonPanel.add(refreshPlantBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);
        
        // Setup button actions
        searchBtn.addActionListener(e -> searchPlants());
        clearSearchBtn.addActionListener(e -> clearSearch());
        lowStockBtn.addActionListener(e -> showLowStockPlants());
        updatePlantBtn.addActionListener(e -> updatePlant());
        updateQuantityBtn.addActionListener(e -> updatePlantQuantity());
        refreshPlantBtn.addActionListener(e -> loadPlantData());
        
        // Setup table selection listener
        plantTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populatePlantForm();
            }
        });
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create order management panel
     */
    private JPanel createOrderManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Orders"));
        
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(orderStatusFilter);
        
        JButton filterBtn = new JButton("Filter");
        JButton refreshOrderBtn = new JButton("Refresh");
        
        filterPanel.add(filterBtn);
        filterPanel.add(refreshOrderBtn);
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(orderTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 350));
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Order Actions"));
        
        JLabel statusLabel = new JLabel("Update Status:");
        JButton updateStatusBtn = new JButton("Update Status");
        JButton processOrderBtn = new JButton("Process Order");
        JButton viewDetailsBtn = new JButton("View Details");
        
        controlPanel.add(statusLabel);
        controlPanel.add(orderStatusComboBox);
        controlPanel.add(updateStatusBtn);
        controlPanel.add(processOrderBtn);
        controlPanel.add(viewDetailsBtn);
        
        // Setup button actions
        filterBtn.addActionListener(e -> filterOrders());
        refreshOrderBtn.addActionListener(e -> loadOrderData());
        updateStatusBtn.addActionListener(e -> updateOrderStatus());
        processOrderBtn.addActionListener(e -> processOrder());
        viewDetailsBtn.addActionListener(e -> viewOrderDetails());
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create customer management panel
     */
    private JPanel createCustomerManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(customerTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(customerIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(customerUsernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(customerAddressField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(customerPhoneField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateCustomerBtn = new JButton("Update Customer");
        JButton refreshCustomerBtn = new JButton("Refresh");
        
        buttonPanel.add(updateCustomerBtn);
        buttonPanel.add(refreshCustomerBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Setup button actions
        updateCustomerBtn.addActionListener(e -> updateCustomer());
        refreshCustomerBtn.addActionListener(e -> loadCustomerData());
        
        // Setup table selection listener
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateCustomerForm();
            }
        });
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create reports panel
     */
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton orderReportBtn = new JButton("Order Report");
        JButton clearReportBtn = new JButton("Clear");
        
        buttonPanel.add(inventoryReportBtn);
        buttonPanel.add(orderReportBtn);
        buttonPanel.add(clearReportBtn);
        
        // Setup button actions
        inventoryReportBtn.addActionListener(e -> generateInventoryReport());
        orderReportBtn.addActionListener(e -> generateOrderReport());
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
                    StaffDashboardView.this,
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
        setTitle("Greenthumb Nursery - Staff Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }

    /**
     * Load initial data
     */
    private void loadInitialData() {
        loadPlantData();
        loadOrderData();
        loadCustomerData();
    }

    // Plant Management Methods
    private void loadPlantData() {
        plantTableModel.setRowCount(0);
        List<Plant> plants = staffController.getAllPlants();
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
            List<Plant> plants = staffController.getAllPlants();
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

    private void searchPlants() {
        String name = searchNameField.getText().trim();
        String type = searchTypeField.getText().trim();
        Double minPrice = null;
        Double maxPrice = null;
        
        try {
            if (!searchMinPriceField.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(searchMinPriceField.getText().trim());
            }
            if (!searchMaxPriceField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(searchMaxPriceField.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price range.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        plantTableModel.setRowCount(0);
        List<Plant> plants = staffController.searchPlants(
            name.isEmpty() ? null : name,
            type.isEmpty() ? null : type,
            minPrice,
            maxPrice
        );
        
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

    private void clearSearch() {
        searchNameField.setText("");
        searchTypeField.setText("");
        searchMinPriceField.setText("");
        searchMaxPriceField.setText("");
        loadPlantData();
    }

    private void showLowStockPlants() {
        plantTableModel.setRowCount(0);
        List<Plant> plants = staffController.getLowStockPlants(10);
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
            
            if (staffController.updatePlant(plant)) {
                clearPlantForm();
                loadPlantData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePlantQuantity() {
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a plant to update quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String plantId = plantIdField.getText().trim();
            int newQuantity = Integer.parseInt(plantQuantityField.getText().trim());
            
            if (staffController.updatePlantQuantity(plantId, newQuantity)) {
                loadPlantData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
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
        List<Order> orders = staffController.getAllOrders();
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

    private void filterOrders() {
        String selectedStatus = (String) orderStatusFilter.getSelectedItem();
        
        orderTableModel.setRowCount(0);
        List<Order> orders;
        
        if ("All".equals(selectedStatus)) {
            orders = staffController.getAllOrders();
        } else {
            orders = staffController.getOrdersByStatus(selectedStatus);
        }
        
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
        
        if (staffController.updateOrderStatus(orderId, newStatus)) {
            loadOrderData();
        }
    }

    private void processOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to process.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String orderId = (String) orderTableModel.getValueAt(selectedRow, 0);
        
        if (staffController.processOrder(orderId)) {
            loadOrderData();
            loadPlantData(); // Refresh plant data as quantities may have changed
        }
    }

    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to view details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String orderId = (String) orderTableModel.getValueAt(selectedRow, 0);
        List<OrderItem> orderItems = staffController.getOrderDetails(orderId);
        
        if (orderItems != null && !orderItems.isEmpty()) {
            StringBuilder details = new StringBuilder();
            details.append("Order Details for: ").append(orderId).append("\n\n");
            
            for (OrderItem item : orderItems) {
                details.append("Plant: ").append(item.getPlant().getName()).append("\n");
                details.append("Quantity: ").append(item.getQuantity()).append("\n");
                details.append("Price: $").append(String.format("%.2f", item.getPlant().getPrice())).append("\n");
                details.append("Subtotal: $").append(String.format("%.2f", item.getSubtotal())).append("\n\n");
            }
            
            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Order Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Customer Management Methods
    private void loadCustomerData() {
        customerTableModel.setRowCount(0);
        List<User> customers = staffController.getAllCustomers();
        if (customers != null) {
            for (User user : customers) {
                if (user instanceof Customer) {
                    Customer customer = (Customer) user;
                    customerTableModel.addRow(new Object[]{
                        customer.getCustomerId(),
                        customer.getUsername(),
                        customer.getAddress(),
                        customer.getPhone()
                    });
                }
            }
        }
    }

    private void populateCustomerForm() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            customerIdField.setText((String) customerTableModel.getValueAt(selectedRow, 0));
            customerUsernameField.setText((String) customerTableModel.getValueAt(selectedRow, 1));
            customerAddressField.setText((String) customerTableModel.getValueAt(selectedRow, 2));
            customerPhoneField.setText((String) customerTableModel.getValueAt(selectedRow, 3));
        }
    }

    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String customerId = customerIdField.getText().trim();
        Customer customer = staffController.getCustomerInfo(customerId);
        
        if (customer != null) {
            customer.setAddress(customerAddressField.getText().trim());
            customer.setPhone(customerPhoneField.getText().trim());
            
            if (staffController.updateCustomerInfo(customer)) {
                loadCustomerData();
            }
        }
    }

    // Report Methods
    private void generateInventoryReport() {
        String report = staffController.generateInventoryReport();
        reportArea.setText(report);
    }

    private void generateOrderReport() {
        String report = staffController.generateOrderReport();
        reportArea.setText(report);
    }
}

