package com.greenthumb.util;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized error handling utility for the application.
 * Provides consistent error handling and user feedback mechanisms.
 */
public class ErrorHandler {
    
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    
    // Error types
    public enum ErrorType {
        DATABASE_ERROR,
        VALIDATION_ERROR,
        AUTHENTICATION_ERROR,
        BUSINESS_LOGIC_ERROR,
        SYSTEM_ERROR,
        USER_INPUT_ERROR
    }

    /**
     * Handle general exceptions with user-friendly messages
     * @param parent Parent component for dialog
     * @param exception Exception that occurred
     * @param userMessage User-friendly message
     * @param errorType Type of error
     */
    public static void handleError(Component parent, Exception exception, String userMessage, ErrorType errorType) {
        // Log the error
        logError(exception, userMessage, errorType);
        
        // Show user-friendly message
        showErrorDialog(parent, userMessage, getErrorTitle(errorType));
    }

    /**
     * Handle database-related errors
     * @param parent Parent component for dialog
     * @param exception Database exception
     * @param operation Operation that failed
     */
    public static void handleDatabaseError(Component parent, SQLException exception, String operation) {
        String userMessage = "Database operation failed: " + operation + "\n" +
                           "Please check your database connection and try again.";
        
        // Log detailed error
        logger.log(Level.SEVERE, "Database error during: " + operation, exception);
        
        // Show user message
        showErrorDialog(parent, userMessage, "Database Error");
        
        // If connection is lost, suggest restart
        if (isDatabaseConnectionError(exception)) {
            int option = JOptionPane.showConfirmDialog(
                parent,
                "Database connection appears to be lost.\n" +
                "Would you like to restart the application?",
                "Connection Lost",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                restartApplication();
            }
        }
    }

    /**
     * Handle validation errors
     * @param parent Parent component for dialog
     * @param validationMessage Validation error message
     */
    public static void handleValidationError(Component parent, String validationMessage) {
        showErrorDialog(parent, validationMessage, "Input Validation Error");
        logger.log(Level.WARNING, "Validation error: " + validationMessage);
    }

    /**
     * Handle authentication errors
     * @param parent Parent component for dialog
     * @param message Authentication error message
     */
    public static void handleAuthenticationError(Component parent, String message) {
        String userMessage = "Authentication failed: " + message;
        showErrorDialog(parent, userMessage, "Authentication Error");
        logger.log(Level.WARNING, "Authentication error: " + message);
    }

    /**
     * Handle business logic errors
     * @param parent Parent component for dialog
     * @param message Business logic error message
     */
    public static void handleBusinessLogicError(Component parent, String message) {
        showErrorDialog(parent, message, "Operation Error");
        logger.log(Level.INFO, "Business logic error: " + message);
    }

    /**
     * Handle unexpected system errors
     * @param parent Parent component for dialog
     * @param exception System exception
     * @param context Context where error occurred
     */
    public static void handleSystemError(Component parent, Exception exception, String context) {
        String userMessage = "An unexpected error occurred.\n" +
                           "Please restart the application and try again.\n" +
                           "If the problem persists, contact support.";
        
        // Log detailed error
        logger.log(Level.SEVERE, "System error in: " + context, exception);
        
        // Show error dialog with option to view details
        showDetailedErrorDialog(parent, userMessage, exception, "System Error");
    }

    /**
     * Show simple error dialog
     * @param parent Parent component
     * @param message Error message
     * @param title Dialog title
     */
    public static void showErrorDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Show warning dialog
     * @param parent Parent component
     * @param message Warning message
     * @param title Dialog title
     */
    public static void showWarningDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Show information dialog
     * @param parent Parent component
     * @param message Information message
     * @param title Dialog title
     */
    public static void showInfoDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Show confirmation dialog
     * @param parent Parent component
     * @param message Confirmation message
     * @param title Dialog title
     * @return true if user confirmed, false otherwise
     */
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        int option = JOptionPane.showConfirmDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return option == JOptionPane.YES_OPTION;
    }

    /**
     * Show detailed error dialog with stack trace
     * @param parent Parent component
     * @param message User message
     * @param exception Exception with details
     * @param title Dialog title
     */
    public static void showDetailedErrorDialog(Component parent, String message, Exception exception, String title) {
        // Create main panel
        JPanel panel = new JPanel(new BorderLayout());
        
        // Add user message
        JLabel messageLabel = new JLabel("<html><body style='width: 300px'>" + message + "</body></html>");
        panel.add(messageLabel, BorderLayout.NORTH);
        
        // Add details button
        JButton detailsButton = new JButton("Show Details");
        JTextArea detailsArea = new JTextArea(10, 50);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setVisible(false);
        
        detailsButton.addActionListener(e -> {
            if (scrollPane.isVisible()) {
                scrollPane.setVisible(false);
                detailsButton.setText("Show Details");
            } else {
                detailsArea.setText(getStackTrace(exception));
                scrollPane.setVisible(true);
                detailsButton.setText("Hide Details");
            }
            SwingUtilities.getWindowAncestor(panel).pack();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(detailsButton);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Log error with appropriate level
     * @param exception Exception to log
     * @param message User message
     * @param errorType Type of error
     */
    private static void logError(Exception exception, String message, ErrorType errorType) {
        Level logLevel;
        switch (errorType) {
            case DATABASE_ERROR:
            case SYSTEM_ERROR:
                logLevel = Level.SEVERE;
                break;
            case AUTHENTICATION_ERROR:
            case VALIDATION_ERROR:
                logLevel = Level.WARNING;
                break;
            case BUSINESS_LOGIC_ERROR:
            case USER_INPUT_ERROR:
                logLevel = Level.INFO;
                break;
            default:
                logLevel = Level.WARNING;
        }
        
        logger.log(logLevel, message, exception);
    }

    /**
     * Get appropriate error title for error type
     * @param errorType Type of error
     * @return Error dialog title
     */
    private static String getErrorTitle(ErrorType errorType) {
        switch (errorType) {
            case DATABASE_ERROR:
                return "Database Error";
            case VALIDATION_ERROR:
                return "Input Validation Error";
            case AUTHENTICATION_ERROR:
                return "Authentication Error";
            case BUSINESS_LOGIC_ERROR:
                return "Operation Error";
            case SYSTEM_ERROR:
                return "System Error";
            case USER_INPUT_ERROR:
                return "Input Error";
            default:
                return "Error";
        }
    }

    /**
     * Check if exception is a database connection error
     * @param exception SQL exception
     * @return true if connection error, false otherwise
     */
    private static boolean isDatabaseConnectionError(SQLException exception) {
        String message = exception.getMessage().toLowerCase();
        return message.contains("connection") || 
               message.contains("communications link failure") ||
               message.contains("no suitable driver") ||
               exception.getErrorCode() == 0;
    }

    /**
     * Get stack trace as string
     * @param exception Exception
     * @return Stack trace string
     */
    private static String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Restart application
     */
    private static void restartApplication() {
        try {
            // Close current application
            System.exit(0);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during application restart", e);
        }
    }

    /**
     * Handle critical errors that require application shutdown
     * @param parent Parent component
     * @param exception Critical exception
     * @param message Error message
     */
    public static void handleCriticalError(Component parent, Exception exception, String message) {
        logger.log(Level.SEVERE, "Critical error: " + message, exception);
        
        String fullMessage = message + "\n\nThe application will now close.";
        showErrorDialog(parent, fullMessage, "Critical Error");
        
        System.exit(1);
    }

    /**
     * Validate and handle user input with error feedback
     * @param parent Parent component
     * @param value Input value
     * @param fieldName Field name for error message
     * @param validator Validation function
     * @return true if valid, false otherwise
     */
    public static boolean validateInput(Component parent, String value, String fieldName, 
                                      java.util.function.Function<String, String> validator) {
        String errorMessage = validator.apply(value);
        if (errorMessage != null) {
            handleValidationError(parent, fieldName + ": " + errorMessage);
            return false;
        }
        return true;
    }

    /**
     * Show success message
     * @param parent Parent component
     * @param message Success message
     */
    public static void showSuccessMessage(Component parent, String message) {
        showInfoDialog(parent, message, "Success");
    }

    /**
     * Show operation in progress dialog
     * @param parent Parent component
     * @param message Progress message
     * @return Progress dialog
     */
    public static JDialog showProgressDialog(Component parent, String message) {
        JDialog progressDialog = new JDialog();
        progressDialog.setTitle("Please Wait");
        progressDialog.setModal(true);
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel(message);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        panel.add(messageLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        
        progressDialog.add(panel);
        progressDialog.pack();
        progressDialog.setLocationRelativeTo(parent);
        
        return progressDialog;
    }
}

