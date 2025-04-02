package fr.stockmate.java.models;

import com.google.gson.reflect.TypeToken;
import fr.stockmate.java.utils.JsonFileHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Product {
    private int id;
    private String reference;
    private String name;
    private String description;
    private double unitPrice;
    private int stockQuantity;
    private int supplierId;
    private int categoryId;
    private int alertThreshold;

    private static final String PRODUCTS_FILE = "products.json";

    // Constructor
    public Product(int id, String reference, String name, String description,
                   double unitPrice, int stockQuantity, int supplierId,
                   int categoryId, int alertThreshold) {
        this.id = id;
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
        this.alertThreshold = alertThreshold;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getReference() { return reference; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getUnitPrice() { return unitPrice; }

    public int getStockQuantity() { return stockQuantity; }

    // CRUD Operations
    public void save() {
        List<Product> products = getAllProducts();
        Optional<Product> existingProduct = products.stream()
                .filter(p -> p.getId() == this.id)
                .findFirst();

        if (existingProduct.isPresent()) {
            products = products.stream()
                    .map(p -> p.getId() == this.id ? this : p)
                    .collect(Collectors.toList());
        } else {
            products.add(this);
        }

        JsonFileHandler.saveToFile(products, PRODUCTS_FILE);
    }

    public static List<Product> getAllProducts() {
        Type productListType = new TypeToken<List<Product>>(){}.getType();
        return JsonFileHandler.loadFromFile(PRODUCTS_FILE, productListType);
    }

    // Override toString method
    @Override
    public String toString() {
        return this.name; // Replace with actual product name field
    }
}