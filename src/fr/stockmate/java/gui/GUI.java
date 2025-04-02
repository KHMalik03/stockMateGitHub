package fr.stockmate.java.gui;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private static JFrame mainFrame;
    private static CardLayout cardLayout;
    private static JPanel cardPanel;

    public static void createAndShowGUI() {
        // Initialize the main JFrame
        mainFrame = new JFrame("StockMate");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 600);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Login Panel
        LoginPanel loginPanel = new LoginPanel(new GUI());
        cardPanel.add(loginPanel, "LoginPanel");

        // Main Application Panel
        JPanel mainPanel = createMainApplicationPanel();
        cardPanel.add(mainPanel, "MainPanel");

        // Add User Order Panel (only shows product ordering functionality)
        JPanel userOrderPanel = createUserOrderPanel();
        cardPanel.add(userOrderPanel, "UserOrderPanel");

        mainFrame.getContentPane().add(cardPanel);

        // Make the frame visible
        mainFrame.setVisible(true);

        // Start with login screen
        cardLayout.show(cardPanel, "LoginPanel");
    }

    private static JPanel createMainApplicationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create tabbed pane for admin interface
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Products", new ProductPanel());
        tabbedPane.addTab("Suppliers", new SupplierPanel());
        tabbedPane.addTab("Order History", new OrderHistoryPanel());
        tabbedPane.addTab("Customers", new CustomerPanel());
        tabbedPane.addTab("User Management", new UserPanel());

        panel.add(tabbedPane, BorderLayout.CENTER);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(e -> switchToLogin());
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);


        panel.add(menuBar, BorderLayout.NORTH);

        return panel;
    }

    private static JPanel createUserOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create order panel
        OrderPanel orderPanel = new OrderPanel();
        panel.add(orderPanel, BorderLayout.CENTER);

        // Create simple menu bar for user interface
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(e -> switchToLogin());
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        panel.add(menuBar, BorderLayout.NORTH);

        return panel;
    }


    public static void switchToMainApplication() {
        cardLayout.show(cardPanel, "MainPanel");
    }

    public static void switchToUserOrderPanel() {
        cardLayout.show(cardPanel, "UserOrderPanel");
    }

    public static void switchToLogin() {
        cardLayout.show(cardPanel, "LoginPanel");
    }
}