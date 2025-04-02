package fr.stockmate.java.gui;

import fr.stockmate.java.models.Supplier;
import fr.stockmate.java.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierPanel extends JPanel {
    private JTable supplierTable;
    private DefaultTableModel supplierTableModel;
    private JTextField supplierNameField, supplierAddressField, supplierPhoneField, supplierEmailField, supplierContactPersonField;

    public SupplierPanel() {
        setLayout(new BorderLayout());
        initializeSupplierManagementPanel();
    }

    private void initializeSupplierManagementPanel() {
        // Table Model
        String[] supplierColumnNames = {"ID", "Name", "Contact Person", "Email"};
        supplierTableModel = new DefaultTableModel(supplierColumnNames, 0);
        supplierTable = new JTable(supplierTableModel);
        JScrollPane tableScrollPane = new JScrollPane(supplierTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Load initial supplier data
        loadSupplierData();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Supplier"));

        supplierNameField = new JTextField();
        supplierAddressField = new JTextField();
        supplierPhoneField = new JTextField();
        supplierEmailField = new JTextField();
        supplierContactPersonField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(supplierNameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(supplierAddressField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(supplierPhoneField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(supplierEmailField);
        inputPanel.add(new JLabel("Contact Person:"));
        inputPanel.add(supplierContactPersonField);

        // Save Button
        JButton saveButton = new JButton("Save Supplier");
        saveButton.addActionListener(e -> saveSupplier());
        inputPanel.add(saveButton);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadSupplierData() {
        // Clear existing data
        supplierTableModel.setRowCount(0);

        // Load suppliers
        List<Supplier> suppliers = Supplier.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            supplierTableModel.addRow(new Object[]{
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getContactPerson(),
                    supplier.getEmail()
            });
        }
    }

    private void saveSupplier() {
        try {
            Supplier newSupplier = new Supplier(
                    generateUniqueId(),
                    supplierNameField.getText(),
                    supplierAddressField.getText(),
                    supplierPhoneField.getText(),
                    supplierEmailField.getText(),
                    supplierContactPersonField.getText()
            );
            newSupplier.save();
            loadSupplierData();
            clearSupplierFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Failed to save supplier: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearSupplierFields() {
        supplierNameField.setText("");
        supplierAddressField.setText("");
        supplierPhoneField.setText("");
        supplierEmailField.setText("");
        supplierContactPersonField.setText("");
    }

    private int generateUniqueId() {
        return User.getAllUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
     }
}
