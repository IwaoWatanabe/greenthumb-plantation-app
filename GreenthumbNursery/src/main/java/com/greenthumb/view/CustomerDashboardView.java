package com.greenthumb.view;

import com.greenthumb.controller.CustomerController;
import com.greenthumb.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Customer dashboard view for the Greenthumb Nursery application.
 * Provides interface for customers to browse plants, manage cart, and place orders.
 */
public class CustomerDashboardView extends JFrame {
    private CustomerController customerController;
    private JTabbedPane tabbedPane;
    
    // Plant Browsing Components
    private JTable plantTable;
    private DefaultTableModel plantTableModel;
    private JTextField searchNameField, searchTypeField, searchMinPriceField, searchMaxPriceField;
    private JSpinner quantitySpinner;
    
    // Shopping Cart Components
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel cartTotalLabel;
    
    // Order History Components
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    
    // Profile Components
    private JTextField profileUsernameField, profileAddressField, profilePhoneField;
    private JPasswordField currentPasswordField, newPasswordField, confirmPasswordField;

    public CustomerDashboardView(CustomerController customerController) {
        this.customerController = customerController;
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
        plantTableModel = new DefaultTableModel(new String[]{"Plant ID", "Name", "Type", "Price", "Available"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        plantTable = new JTable(plantTableModel);
        
        cartTableModel = new DefaultTableModel(new String[]{"Plant Name", "Price", "Quantity", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        
        orderTableModel = new DefaultTableModel(new String[]{"Order ID", "Date", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        
        // Initialize form components
        searchNameField = new JTextField(10);
        searchTypeField = new JTextField(10);
        searchMinPriceField = new JTextField(8);
        searchMaxPriceField = new JTextField(8);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        cartTotalLabel = new JLabel("Total: $0.00");
        cartTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Profile components
        profileUsernameField = new JTextField(15);
        profileUsernameField.setEditable(false);
        profileAddressField = new JTextField(15);
        profilePhoneField = new JTextField(15);
        
        currentPasswordField = new JPasswordField(15);
        newPasswordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);
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
        logoutItem.addActionListener(e -> customerController.logout());
        exitItem.addActionListener(e -> System.exit(0));
        
        // Create tabs
        tabbedPane.addTab("Browse Plants", createPlantBrowsingPanel());
        tabbedPane.addTab("Shopping Cart", createShoppingCartPanel());
        tabbedPane.addTab("Order History", createOrderHistoryPanel());
        tabbedPane.addTab("My Profile", createProfilePanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("Welcome, " + customerController.getCurrentCustomer().getUsername() + " (Customer)");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Create plant browsing panel
     */
    private JPanel createPlantBrowsingPanel() {
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
        JButton showAllBtn = new JButton("Show All");
        
        searchPanel.add(searchBtn);
        searchPanel.add(clearSearchBtn);
        searchPanel.add(showAllBtn);
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(plantTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 350));
        
        // Add to cart panel
        JPanel addToCartPanel = new JPanel(new FlowLayout());
        addToCartPanel.setBorder(BorderFactory.createTitledBorder("Add to Cart"));
        
        addToCartPanel.add(new JLabel("Quantity:"));
        addToCartPanel.add(quantitySpinner);
        
        JButton addToCartBtn = new JButton("Add to Cart");
        JButton viewDetailsBtn = new JButton("View Details");
        
        addToCartPanel.add(addToCartBtn);
        addToCartPanel.add(viewDetailsBtn);
        
        // Setup button actions
        searchBtn.addActionListener(e -> searchPlants());
        clearSearchBtn.addActionListener(e -> clearSearch());
        showAllBtn.addActionListener(e -> loadPlantData());
        addToCartBtn.addActionListener(e -> addToCart());
        viewDetailsBtn.addActionListener(e -> viewPlantDetails());
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(addToCartPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create shopping cart panel
     */
    private JPanel createShoppingCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Cart table
        JScrollPane tableScrollPane = new JScrollPane(cartTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Shopping Cart Items"));
        
        // Cart actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout());
        
        JButton updateQuantityBtn = new JButton("Update Quantity");
        JButton removeItemBtn = new JButton("Remove Item");
        JButton clearCartBtn = new JButton("Clear Cart");
        JButton refreshCartBtn = new JButton("Refresh");
        
        actionsPanel.add(updateQuantityBtn);
        actionsPanel.add(removeItemBtn);
        actionsPanel.add(clearCartBtn);
        actionsPanel.add(refreshCartBtn);
        
        // Checkout panel
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBorder(BorderFactory.createTitledBorder("Checkout"));
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(cartTotalLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton placeOrderBtn = new JButton("Place Order");
        placeOrderBtn.setFont(new Font("Arial", Font.BOLD, 14));
        placeOrderBtn.setBackground(new Color(34, 139, 34));
        placeOrderBtn.setForeground(Color.WHITE);
        buttonPanel.add(placeOrderBtn);
        
        checkoutPanel.add(totalPanel, BorderLayout.NORTH);
        checkoutPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Setup button actions
        updateQuantityBtn.addActionListener(e -> updateCartItemQuantity());
        removeItemBtn.addActionListener(e -> removeFromCart());
        clearCartBtn.addActionListener(e -> clearCart());
        refreshCartBtn.addActionListener(e -> refreshCart());
        placeOrderBtn.addActionListener(e -> placeOrder());
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.NORTH);
        panel.add(checkoutPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create order history panel
     */
    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(orderTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 400));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Order History"));
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        JButton viewDetailsBtn = new JButton("View Details");
        JButton cancelOrderBtn = new JButton("Cancel Order");
        JButton refreshOrderBtn = new JButton("Refresh");
        
        controlPanel.add(viewDetailsBtn);
        controlPanel.add(cancelOrderBtn);
        controlPanel.add(refreshOrderBtn);
        
        // Setup button actions
        viewDetailsBtn.addActionListener(e -> viewOrderDetails());
        cancelOrderBtn.addActionListener(e -> cancelOrder());
        refreshOrderBtn.addActionListener(e -> loadOrderHistory());
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create profile panel
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Profile info panel
        JPanel profileInfoPanel = new JPanel(new GridBagLayout());
        profileInfoPanel.setBorder(BorderFactory.createTitledBorder("Profile Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        profileInfoPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        profileInfoPanel.add(profileUsernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        profileInfoPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        profileInfoPanel.add(profileAddressField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        profileInfoPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        profileInfoPanel.add(profilePhoneField, gbc);
        
        JButton updateProfileBtn = new JButton("Update Profile");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        profileInfoPanel.add(updateProfileBtn, gbc);
        
        // Password change panel
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        passwordPanel.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        passwordPanel.add(currentPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        passwordPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        passwordPanel.add(newPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        passwordPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        passwordPanel.add(confirmPasswordField, gbc);
        
        JButton changePasswordBtn = new JButton("Change Password");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        passwordPanel.add(changePasswordBtn, gbc);
        
        // Setup button actions
        updateProfileBtn.addActionListener(e -> updateProfile());
        changePasswordBtn.addActionListener(e -> changePassword());
        
        panel.add(profileInfoPanel, BorderLayout.NORTH);
        panel.add(passwordPanel, BorderLayout.CENTER);
        
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
                    CustomerDashboardView.this,
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
        setTitle("Greenthumb Nursery - Customer Dashboard");
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
        refreshCart();
        loadOrderHistory();
        loadProfileData();
    }

    // Plant Browsing Methods
    private void loadPlantData() {
        plantTableModel.setRowCount(0);
        List<Plant> plants = customerController.getAvailablePlants();
        if (plants != null) {
            for (Plant plant : plants) {
                plantTableModel.addRow(new Object[]{
                    plant.getPlantId(),
                    plant.getName(),
                    plant.getType(),
                    String.format("$%.2f", plant.getPrice()),
                    plant.getQuantity() > 0 ? "Yes (" + plant.getQuantity() + ")" : "No"
                });
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
        List<Plant> plants = customerController.searchPlants(
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
                    plant.getQuantity() > 0 ? "Yes (" + plant.getQuantity() + ")" : "No"
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

    private void addToCart() {
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a plant to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String plantId = (String) plantTableModel.getValueAt(selectedRow, 0);
        int quantity = (Integer) quantitySpinner.getValue();
        
        if (customerController.addToCart(plantId, quantity)) {
            refreshCart();
            // Switch to cart tab to show the updated cart
            tabbedPane.setSelectedIndex(1);
        }
    }

    private void viewPlantDetails() {
        int selectedRow = plantTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a plant to view details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String plantId = (String) plantTableModel.getValueAt(selectedRow, 0);
        Plant plant = customerController.getPlantById(plantId);
        
        if (plant != null) {
            StringBuilder details = new StringBuilder();
            details.append("Plant Details\n\n");
            details.append("ID: ").append(plant.getPlantId()).append("\n");
            details.append("Name: ").append(plant.getName()).append("\n");
            details.append("Type: ").append(plant.getType()).append("\n");
            details.append("Price: $").append(String.format("%.2f", plant.getPrice())).append("\n");
            details.append("Available: ").append(plant.getQuantity()).append("\n\n");
            details.append("Description:\n").append(plant.getDescription());
            
            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Plant Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Shopping Cart Methods
    private void refreshCart() {
        cartTableModel.setRowCount(0);
        List<OrderItem> cartItems = customerController.getShoppingCart();
        
        if (cartItems != null) {
            for (OrderItem item : cartItems) {
                cartTableModel.addRow(new Object[]{
                    item.getPlant().getName(),
                    String.format("$%.2f", item.getPlant().getPrice()),
                    item.getQuantity(),
                    String.format("$%.2f", item.getSubtotal())
                });
            }
        }
        
        // Update total
        double total = customerController.calculateCartTotal();
        cartTotalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    private void updateCartItemQuantity() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String plantName = (String) cartTableModel.getValueAt(selectedRow, 0);
        String quantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:", "Update Quantity", JOptionPane.QUESTION_MESSAGE);
        
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(quantityStr.trim());
                
                // Find plant ID by name
                List<OrderItem> cartItems = customerController.getShoppingCart();
                for (OrderItem item : cartItems) {
                    if (item.getPlant().getName().equals(plantName)) {
                        if (customerController.updateCartItemQuantity(item.getPlantId(), newQuantity)) {
                            refreshCart();
                        }
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String plantName = (String) cartTableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove " + plantName + " from your cart?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Find plant ID by name
            List<OrderItem> cartItems = customerController.getShoppingCart();
            for (OrderItem item : cartItems) {
                if (item.getPlant().getName().equals(plantName)) {
                    if (customerController.removeFromCart(item.getPlantId())) {
                        refreshCart();
                    }
                    break;
                }
            }
        }
    }

    private void clearCart() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear your entire cart?",
            "Confirm Clear Cart",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            customerController.clearCart();
            refreshCart();
        }
    }

    private void placeOrder() {
        List<OrderItem> cartItems = customerController.getShoppingCart();
        if (cartItems == null || cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double total = customerController.calculateCartTotal();
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Place order for $" + String.format("%.2f", total) + "?",
            "Confirm Order",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (customerController.placeOrder()) {
                refreshCart();
                loadOrderHistory();
                // Switch to order history tab
                tabbedPane.setSelectedIndex(2);
            }
        }
    }

    // Order History Methods
    private void loadOrderHistory() {
        orderTableModel.setRowCount(0);
        List<Order> orders = customerController.getOrderHistory();
        
        if (orders != null) {
            for (Order order : orders) {
                orderTableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getOrderDate(),
                    String.format("$%.2f", order.getTotalAmount()),
                    order.getStatus()
                });
            }
        }
    }

    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to view details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String orderId = (String) orderTableModel.getValueAt(selectedRow, 0);
        List<OrderItem> orderItems = customerController.getOrderDetails(orderId);
        
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

    private void cancelOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String orderId = (String) orderTableModel.getValueAt(selectedRow, 0);
        String status = (String) orderTableModel.getValueAt(selectedRow, 3);
        
        if (!"Pending".equals(status)) {
            JOptionPane.showMessageDialog(this, "Only pending orders can be cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (customerController.cancelOrder(orderId)) {
            loadOrderHistory();
        }
    }

    // Profile Methods
    private void loadProfileData() {
        Customer customer = customerController.getCurrentCustomer();
        profileUsernameField.setText(customer.getUsername());
        profileAddressField.setText(customer.getAddress() != null ? customer.getAddress() : "");
        profilePhoneField.setText(customer.getPhone() != null ? customer.getPhone() : "");
    }

    private void updateProfile() {
        String address = profileAddressField.getText().trim();
        String phone = profilePhoneField.getText().trim();
        
        if (customerController.updateProfile(address, phone)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (customerController.changePassword(currentPassword, newPassword, confirmPassword)) {
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }
    }
}

