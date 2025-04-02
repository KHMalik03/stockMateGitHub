package fr.stockmate.java.models;

import com.google.gson.reflect.TypeToken;
import fr.stockmate.java.utils.JsonFileHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Category {
    private int id;
    private String name;
    private String description;

    private static final String CATEGORIES_FILE = "categories.json";

    // Constructor
    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    // CRUD Operations
    public void save() {
        List<Category> categories = getAllCategories();
        Optional<Category> existingCategory = categories.stream()
                .filter(c -> c.getId() == this.id)
                .findFirst();

        if (existingCategory.isPresent()) {
            categories = categories.stream()
                    .map(c -> c.getId() == this.id ? this : c)
                    .collect(Collectors.toList());
        } else {
            categories.add(this);
        }

        JsonFileHandler.saveToFile(categories, CATEGORIES_FILE);
    }

    public static List<Category> getAllCategories() {
        Type categoryListType = new TypeToken<List<Category>>(){}.getType();
        return JsonFileHandler.loadFromFile(CATEGORIES_FILE, categoryListType);
    }

    // Override toString method
    @Override
    public String toString() {
        return name;
    }
}