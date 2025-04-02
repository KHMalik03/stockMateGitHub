package fr.stockmate.java.gui;

import fr.stockmate.java.models.User;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private GUI gui;

    public LoginPanel(GUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout());
        initializeLoginScreen();
    }

    private void initializeLoginScreen() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("StockMate Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> performLogin());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx++;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx++;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        add(loginPanel, BorderLayout.CENTER);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            // Authenticate user using the static method
            User authenticatedUser = User.authenticate(username, password);

            if (authenticatedUser != null) {
                // Check user role and redirect accordingly
                if ("ADMIN".equals(authenticatedUser.getRole())) {
                    gui.switchToMainApplication();
                } else {
                    gui.switchToUserOrderPanel();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error during login: " + e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}