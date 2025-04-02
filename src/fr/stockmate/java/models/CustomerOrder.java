package fr.stockmate.java.models;

import com.google.gson.reflect.TypeToken;
import fr.stockmate.java.utils.JsonFileHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerOrder {
    private int id;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String orderStatus;
    private BigDecimal totalAmount;

    private static final String ORDERS_FILE = "customer_orders.json";

    // Constructor
    public CustomerOrder(int id, LocalDate orderDate, LocalDate deliveryDate,
                         String orderStatus, BigDecimal totalAmount) {
        this.id = id;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
    }

    // Empty Constructor
    public CustomerOrder() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getOrderDate() { return orderDate; }

    public LocalDate getDeliveryDate() { return deliveryDate; }

    public String getOrderStatus() { return orderStatus; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    // CRUD Operations
    public void save() {
        List<CustomerOrder> orders = getAllCustomerOrders();
        Optional<CustomerOrder> existingOrder = orders.stream()
                .filter(o -> o.getId() == this.id)
                .findFirst();

        if (existingOrder.isPresent()) {
            orders = orders.stream()
                    .map(o -> o.getId() == this.id ? this : o)
                    .collect(Collectors.toList());
        } else {
            orders.add(this);
        }

        JsonFileHandler.saveToFile(orders, ORDERS_FILE);
    }

    public static List<CustomerOrder> getAllCustomerOrders() {
        Type orderListType = new TypeToken<List<CustomerOrder>>(){}.getType();
        return JsonFileHandler.loadFromFile(ORDERS_FILE, orderListType);
    }

}

