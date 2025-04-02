package fr.stockmate.java.gui;

import fr.stockmate.java.models.CustomerOrder;
import fr.stockmate.java.models.Product;
import fr.stockmate.java.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderPanel extends JPanel {
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JComboBox<Product> productComboBox;
    private JSpinner quantitySpinner;
    private JLabel totalAmountLabel;

    // List to store current cart items
    private List<OrderItem> cartItems = new ArrayList<>();

    public OrderPanel() {
        setLayout(new BorderLayout());
        initializeOrderPanel();
    }

    private void initializeOrderPanel() {
        // Product selection panel (top)
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Products"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);



        // Product selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        selectionPanel.add(new JLabel("Product:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        productComboBox = new JComboBox<>(Product.getAllProducts().toArray(new Product[0]));
        selectionPanel.add(productComboBox, gbc);

        // Quantity selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        selectionPanel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        selectionPanel.add(quantitySpinner, gbc);

        // Add to cart button
        gbc.gridx = 2;
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> addToCart());
        selectionPanel.add(addToCartButton, gbc);

        add(selectionPanel, BorderLayout.NORTH);

        // Cart panel (center)
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));

        // Cart table
        String[] cartColumnNames = {"Product", "Price", "Quantity", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumnNames, 0);
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        // Cart controls
        JPanel cartControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton removeFromCartButton = new JButton("Remove Selected");
        removeFromCartButton.addActionListener(e -> removeFromCart());
        cartControlPanel.add(removeFromCartButton);

        totalAmountLabel = new JLabel("Total: €0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cartControlPanel.add(totalAmountLabel);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> placeOrder());
        cartControlPanel.add(placeOrderButton);

        cartPanel.add(cartControlPanel, BorderLayout.SOUTH);

        add(cartPanel, BorderLayout.CENTER);
    }

    private void addToCart() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity = (int) quantitySpinner.getValue();

        // Check if the product is already in the cart
        boolean found = false;
        for (OrderItem item : cartItems) {
            if (item.productId == selectedProduct.getId()) {
                item.quantity += quantity;
                found = true;
                break;
            }
        }

        // If not found, add as new item
        if (!found) {
            cartItems.add(new OrderItem(
                    selectedProduct.getId(),
                    selectedProduct.getName(),
                    selectedProduct.getUnitPrice(),
                    quantity
            ));
        }

        // Update the cart table
        updateCartTable();
    }

    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < cartItems.size()) {
            cartItems.remove(selectedRow);
            updateCartTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCartTable() {
        // Clear the table
        cartTableModel.setRowCount(0);

        // Calculate total
        BigDecimal total = BigDecimal.ZERO;

        // Add items to table
        for (OrderItem item : cartItems) {
            BigDecimal subtotal = BigDecimal.valueOf(item.price * item.quantity);
            total = total.add(subtotal);

            cartTableModel.addRow(new Object[]{
                    item.productName,
                    "€" + item.price,
                    item.quantity,
                    "€" + subtotal
            });
        }

        // Update total label
        totalAmountLabel.setText("Total: €" + total);
    }

    private void placeOrder() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }



        try {
            // Calculate total amount
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderItem item : cartItems) {
                totalAmount = totalAmount.add(BigDecimal.valueOf(item.price * item.quantity));
            }

            // Create new order
            CustomerOrder order = new CustomerOrder(
                    generateUniqueId(),
                    LocalDate.now(),
                    LocalDate.now().plusDays(7),  // Estimated delivery date
                    "Pending",
                    totalAmount
            );


            // Save the order
            order.save();

            JOptionPane.showMessageDialog(this,
                    "Order placed successfully!\nOrder ID: " + order.getId(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the cart
            cartItems.clear();
            updateCartTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to place order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int generateUniqueId() {
        return User.getAllUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }

    // Helper class to represent items in the cart
    private static class OrderItem {
        int productId;
        String productName;
        double price;
        int quantity;

        OrderItem(int productId, String productName, double price, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }
    }
}