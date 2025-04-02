package fr.stockmate.java.gui;

import fr.stockmate.java.models.Customer;
import fr.stockmate.java.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private DefaultTableModel customerTableModel;
    private JTable customerTable;
    private JTextField customerNameField, customerAddressField, customerPhoneField, customerEmailField;

    public CustomerPanel() {
        setLayout(new BorderLayout());
        add(createCustomerManagementPanel(), BorderLayout.CENTER);
    }

    private JPanel createCustomerManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table Model
        String[] customerColumnNames = {"ID", "Name", "Email", "Phone"};
        customerTableModel = new DefaultTableModel(customerColumnNames, 0);
        customerTable = new JTable(customerTableModel);
        JScrollPane tableScrollPane = new JScrollPane(customerTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Load initial customer data
        loadCustomerData();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Customer"));

        customerNameField = new JTextField();
        customerAddressField = new JTextField();
        customerPhoneField = new JTextField();
        customerEmailField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(customerNameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(customerAddressField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(customerPhoneField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(customerEmailField);

        // Save Button
        JButton saveButton = new JButton("Save Customer");
        saveButton.addActionListener(e -> saveCustomer());
        inputPanel.add(saveButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadCustomerData() {
        // Clear existing data
        customerTableModel.setRowCount(0);

        // Load customers
        List<Customer> customers = Customer.getAllCustomers();
        for (Customer customer : customers) {
            customerTableModel.addRow(new Object[]{
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone()
            });
        }
    }

    private void saveCustomer() {
        try {
            Customer newCustomer = new Customer(
                    generateUniqueId(),
                    customerNameField.getText(),
                    customerAddressField.getText(),
                    customerPhoneField.getText(),
                    customerEmailField.getText()
            );
            newCustomer.save();
            loadCustomerData();
            clearCustomerFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save customer: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearCustomerFields() {
        customerNameField.setText("");
        customerAddressField.setText("");
        customerPhoneField.setText("");
        customerEmailField.setText("");
    }

    private int generateUniqueId() {
        return User.getAllUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }
}
