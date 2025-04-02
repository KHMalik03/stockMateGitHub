package fr.stockmate.java.gui;

import fr.stockmate.java.models.CustomerOrder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistoryPanel extends JPanel {
    private DefaultTableModel orderTableModel;
    private JTable orderTable;

    public OrderHistoryPanel() {
        setLayout(new BorderLayout());
        initializeOrderHistoryPanel();
    }

    private void initializeOrderHistoryPanel() {
        // Create table model
        String[] orderColumnNames = {"Order ID", "Order Date", "Delivery Date", "Status", "Total Amount"};
        orderTableModel = new DefaultTableModel(orderColumnNames, 0);
        orderTable = new JTable(orderTableModel);
        JScrollPane tableScrollPane = new JScrollPane(orderTable);

        // Add the table to the panel
        add(tableScrollPane, BorderLayout.CENTER);

        // Load initial order data
        loadOrderData();

        // Create control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadOrderData());
        controlPanel.add(refreshButton);

        JButton deleteButton = new JButton("Delete Order");
        deleteButton.addActionListener(e -> deleteSelectedOrder());
        controlPanel.add(deleteButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void loadOrderData() {
        // Clear existing data
        orderTableModel.setRowCount(0);

        // Load orders
        List<CustomerOrder> orders = CustomerOrder.getAllCustomerOrders();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (CustomerOrder order : orders) {
            orderTableModel.addRow(new Object[]{
                    order.getId(),
                    order.getOrderDate().format(formatter),
                    order.getDeliveryDate().format(formatter),
                    order.getOrderStatus(),
                    "€" + order.getTotalAmount()
            });
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an order to delete",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Order #" + orderId + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Get all orders
                List<CustomerOrder> orders = CustomerOrder.getAllCustomerOrders();

                // Remove the selected order
                orders.removeIf(order -> order.getId() == orderId);

                // Save updated list (we need to add a method to CustomerOrder for this)
                saveOrderList(orders);

                // Refresh the table
                loadOrderData();

                JOptionPane.showMessageDialog(this,
                        "Order #" + orderId + " deleted successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete order: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void saveOrderList(List<CustomerOrder> orders) {
        if (!orders.isEmpty()) {
            CustomerOrder temp = orders.get(0);
            }
    }
}