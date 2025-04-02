package fr.stockmate.java.models;

import com.google.gson.reflect.TypeToken;
import fr.stockmate.java.utils.JsonFileHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;

    private static final String CUSTOMERS_FILE = "customers.json";

    // Constructor
    public Customer(int id, String name, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Empty Constructor
    public Customer() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }

    public String getEmail() { return email; }

    // CRUD Operations
    public void save() {
        List<Customer> customers = getAllCustomers();
        Optional<Customer> existingCustomer = customers.stream()
                .filter(c -> c.getId() == this.id)
                .findFirst();

        if (existingCustomer.isPresent()) {
            customers = customers.stream()
                    .map(c -> c.getId() == this.id ? this : c)
                    .collect(Collectors.toList());
        } else {
            customers.add(this);
        }

        JsonFileHandler.saveToFile(customers, CUSTOMERS_FILE);
    }

    public static List<Customer> getAllCustomers() {
        Type customerListType = new TypeToken<List<Customer>>(){}.getType();
        return JsonFileHandler.loadFromFile(CUSTOMERS_FILE, customerListType);
    }
}