package com.greenthumb.view;

import com.greenthumb.controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login view for the Greenthumb Nursery application.
 * Provides user interface for user authentication.
 */
public class LoginView extends JFrame {
    private LoginController loginController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton clearButton;
    private JLabel statusLabel;

    public LoginView() {
        this.loginController = new LoginController(this);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
    }

    /**
     * Initialize Swing components
     */
    private void initializeComponents() {
        // Create components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        statusLabel = new JLabel(" ");

        // Set component properties
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.RED);

        // Set button colors
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(220, 220, 220));

        // Set tooltips
        usernameField.setToolTipText("Enter your username");
        passwordField.setToolTipText("Enter your password");
        loginButton.setToolTipText("Click to login");
        clearButton.setToolTipText("Clear all fields");
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Greenthumb Nursery");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 139, 34));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        // Username label and field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // Status label
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(statusLabel, gbc);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Create footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(34, 139, 34));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel footerLabel = new JLabel("© 2025 Greenthumb Nursery - Desktop Application");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers for components
     */
    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Clear button action
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Enter key on password field
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Enter key on username field
        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.requestFocus();
            }
        });
    }

    /**
     * Set frame properties
     */
    private void setFrameProperties() {
        setTitle("Greenthumb Nursery - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // Center on screen

        // Set minimum size
        setMinimumSize(new Dimension(400, 350));

        // Set icon (if available)
        try {
            // You can add an icon here if you have one
            // setIconImage(Toolkit.getDefaultToolkit().getImage("path/to/icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }

    /**
     * Perform login operation
     */
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty()) {
            setStatusMessage("Please enter username", Color.RED);
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            setStatusMessage("Please enter password", Color.RED);
            passwordField.requestFocus();
            return;
        }

        // Disable login button during authentication
        loginButton.setEnabled(false);
        setStatusMessage("Authenticating...", Color.BLUE);

        // Perform login in a separate thread to avoid blocking UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boolean success = loginController.login(username, password);
                
                // Re-enable login button
                loginButton.setEnabled(true);
                
                if (success) {
                    setStatusMessage("Login successful!", Color.GREEN);
                } else {
                    setStatusMessage("Login failed", Color.RED);
                    passwordField.selectAll();
                    passwordField.requestFocus();
                }
            }
        });
    }

    /**
     * Clear all input fields
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText(" ");
        usernameField.requestFocus();
    }

    /**
     * Set status message
     * @param message Message to display
     * @param color Color of the message
     */
    private void setStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    /**
     * Show the login view
     */
    public void showView() {
        setVisible(true);
        usernameField.requestFocus();
    }

    /**
     * Hide the login view
     */
    public void hideView() {
        setVisible(false);
    }

    /**
     * Get the login controller
     * @return LoginController instance
     */
    public LoginController getLoginController() {
        return loginController;
    }

    /**
     * Main method for testing the login view
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set look and feel
        try {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            // Use default look and feel
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView().showView();
            }
        });
    }
}

