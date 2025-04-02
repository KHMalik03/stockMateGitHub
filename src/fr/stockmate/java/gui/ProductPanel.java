package fr.stockmate.java.gui;

import fr.stockmate.java.models.Category;
import fr.stockmate.java.models.Product;
import fr.stockmate.java.models.Supplier;
import fr.stockmate.java.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField productNameField, productReferenceField, productDescriptionField, productPriceField, productStockField, productAlertThresholdField;
    private JComboBox<Category> productCategoryComboBox;
    private JComboBox<Supplier> productSupplierComboBox;

    public ProductPanel() {
        setLayout(new BorderLayout());
        initializeProductManagementPanel();
    }

    private void initializeProductManagementPanel() {
        // Table Model
        String[] productColumnNames = {"ID", "Name", "Reference", "Stock", "Price"};
        productTableModel = new DefaultTableModel(productColumnNames, 0);
        productTable = new JTable(productTableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Load initial product data
        loadProductData();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Product"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        // Adding components with proper alignment
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        productNameField = new JTextField(15);
        inputPanel.add(productNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Reference:"), gbc);
        gbc.gridx = 1;
        productReferenceField = new JTextField(15);
        inputPanel.add(productReferenceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        productDescriptionField = new JTextField(15);
        inputPanel.add(productDescriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        productPriceField = new JTextField(15);
        inputPanel.add(productPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        productStockField = new JTextField(15);
        inputPanel.add(productStockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Alert Threshold:"), gbc);
        gbc.gridx = 1;
        productAlertThresholdField = new JTextField(15);
        inputPanel.add(productAlertThresholdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        productCategoryComboBox = new JComboBox<>(Category.getAllCategories().toArray(new Category[0]));
        inputPanel.add(productCategoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 1;
        productSupplierComboBox = new JComboBox<>(Supplier.getAllSuppliers().toArray(new Supplier[0]));
        inputPanel.add(productSupplierComboBox, gbc);

        // Save Button
        gbc.gridx = 1;
        gbc.gridy = 8;
        JButton saveButton = new JButton("Save Product");
        saveButton.addActionListener(e -> saveProduct());
        inputPanel.add(saveButton, gbc);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadProductData() {
        // Clear existing data
        productTableModel.setRowCount(0);

        // Load products
        List<Product> products = Product.getAllProducts();
        for (Product product : products) {
            productTableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getReference(),
                    product.getStockQuantity(),
                    product.getUnitPrice()
            });
        }
    }

    private void saveProduct() {
        try {
            // Get input values as strings
            String priceString = productPriceField.getText().trim();
            String stockQuantityString = productStockField.getText().trim();
            String alertThresholdString = productAlertThresholdField.getText().trim();

            // Debug: Print the input values
            System.out.println("Price: '" + priceString + "'");
            System.out.println("Stock Quantity: '" + stockQuantityString + "'");
            System.out.println("Alert Threshold: '" + alertThresholdString + "'");

            // Validate and convert numeric fields
            int price = parseInteger(priceString, "price");
            int stockQuantity = parseInteger(stockQuantityString, "stock quantity");
            int alertThreshold = parseInteger(alertThresholdString, "alert threshold");

            // Ensure a valid selection for category and supplier
            if (productCategoryComboBox.getSelectedItem() == null || productSupplierComboBox.getSelectedItem() == null) {
                throw new IllegalArgumentException("Please select a valid Category and Supplier.");
            }

            // Create a new Product object
            Product newProduct = new Product(
                    generateUniqueId(),
                    productReferenceField.getText(),
                    productNameField.getText(),
                    productDescriptionField.getText(),
                    price,
                    stockQuantity,
                    ((Supplier) productSupplierComboBox.getSelectedItem()).getId(),
                    ((Category) productCategoryComboBox.getSelectedItem()).getId(),
                    alertThreshold
            );

            // Save the product
            newProduct.save();
            loadProductData(); // Refresh the product table
            clearProductFields(); // Clear input fields
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log the exception for debugging
        }
    }

    private int parseInteger(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " format: '" + value + "'");
        }
    }

    private void clearProductFields() {
        productNameField.setText("");
        productReferenceField.setText("");
        productDescriptionField.setText("");
        productPriceField.setText("");
        productStockField.setText("");
        productAlertThresholdField.setText("");
        productCategoryComboBox.setSelectedIndex(-1);
        productSupplierComboBox.setSelectedIndex(-1);
    }

    private int generateUniqueId() {
        return User.getAllUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }
}
