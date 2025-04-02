package fr.stockmate.java.gui;

import fr.stockmate.java.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JTextField lastNameField, firstNameField, loginField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton saveButton, deleteButton, clearButton, refreshButton;

    public UserPanel() {
        setLayout(new BorderLayout());
        initializeUserManagementPanel();
    }

    private void initializeUserManagementPanel() {
        // Table Model
        String[] userColumnNames = {"ID", "Last Name", "First Name", "Login", "Role"};
        userTableModel = new DefaultTableModel(userColumnNames, 0);
        userTable = new JTable(userTableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);

        // Add selection listener to populate form when a row is selected
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        add(tableScrollPane, BorderLayout.CENTER);

        // Load initial user data
        loadUserData();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("User Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Form fields
        lastNameField = new JTextField(20);
        firstNameField = new JTextField(20);
        loginField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Role dropdown
        String[] roles = {"ADMIN", "USER", "MANAGER"};
        roleComboBox = new JComboBox<>(roles);

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(roleComboBox, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveUser());

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteUser());

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadUserData());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());

        // Add all buttons to the panel
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton); // Add the refresh button
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadUserData() {
        // Clear existing data
        userTableModel.setRowCount(0);

        // Load users
        List<User> users = User.getAllUsers();
        for (User user : users) {
            userTableModel.addRow(new Object[]{
                    user.getId(),
                    user.getLastName(),
                    user.getFirstName(),
                    user.getLogin(),
                    user.getRole()
            });
        }
    }

    private void saveUser() {
        try {
            // Check if form is valid
            if (!validateForm()) {
                return;
            }

            // Get selected row to determine if we're updating or creating
            int selectedRow = userTable.getSelectedRow();
            int userId = selectedRow != -1 ?
                    (int) userTableModel.getValueAt(selectedRow, 0) :
                    generateUniqueId();

            // Create a new User object
            User user = new User(
                    userId,
                    lastNameField.getText(),
                    firstNameField.getText(),
                    loginField.getText(),
                    new String(passwordField.getPassword()),
                    (String) roleComboBox.getSelectedItem()
            );

            // Save the user
            user.save();

            // Refresh the data and clear the form
            loadUserData();
            clearForm();

            JOptionPane.showMessageDialog(this,
                    "User saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) userTableModel.getValueAt(selectedRow, 0);

            // Confirm deletion
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this user?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                User.deleteUser(userId);
                loadUserData();
                clearForm();
                JOptionPane.showMessageDialog(this,
                        "User deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a user to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateFormFromSelection() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) userTableModel.getValueAt(selectedRow, 0);

            // Find the user in the list
            User.getAllUsers().stream()
                    .filter(u -> u.getId() == userId)
                    .findFirst()
                    .ifPresent(user -> {
                        lastNameField.setText(user.getLastName());
                        firstNameField.setText(user.getFirstName());
                        loginField.setText(user.getLogin());
                        passwordField.setText(user.getPassword());
                        roleComboBox.setSelectedItem(user.getRole());
                    });
        }
    }

    private void clearForm() {
        lastNameField.setText("");
        firstNameField.setText("");
        loginField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        userTable.clearSelection();
    }

    private boolean validateForm() {
        // Check required fields
        if (lastNameField.getText().trim().isEmpty() ||
                firstNameField.getText().trim().isEmpty() ||
                loginField.getText().trim().isEmpty() ||
                new String(passwordField.getPassword()).trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "All fields are required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if login is unique (for new users)
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) { // New user
            boolean loginExists = User.getAllUsers().stream()
                    .anyMatch(u -> u.getLogin().equals(loginField.getText().trim()));

            if (loginExists) {
                JOptionPane.showMessageDialog(this,
                        "Login already exists. Please choose a different login.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private int generateUniqueId() {
        return User.getAllUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }
}