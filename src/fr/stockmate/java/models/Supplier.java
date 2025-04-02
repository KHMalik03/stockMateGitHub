package fr.stockmate.java.models;

import com.google.gson.reflect.TypeToken;
import fr.stockmate.java.utils.JsonFileHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Supplier {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String contactPerson;

    private static final String SUPPLIERS_FILE = "suppliers.json";

    // Constructor
    public Supplier(int id, String name, String address, String phone, String email, String contactPerson) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.contactPerson = contactPerson;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public String getContactPerson() { return contactPerson; }


    // CRUD Operations
    public void save() {
        List<Supplier> suppliers = getAllSuppliers();
        Optional<Supplier> existingSupplier = suppliers.stream()
                .filter(s -> s.getId() == this.id)
                .findFirst();

        if (existingSupplier.isPresent()) {
            suppliers = suppliers.stream()
                    .map(s -> s.getId() == this.id ? this : s)
                    .collect(Collectors.toList());
        } else {
            suppliers.add(this);
        }

        JsonFileHandler.saveToFile(suppliers, SUPPLIERS_FILE);
    }

    public static List<Supplier> getAllSuppliers() {
        Type supplierListType = new TypeToken<List<Supplier>>(){}.getType();
        return JsonFileHandler.loadFromFile(SUPPLIERS_FILE, supplierListType);
    }

    @Override
    public String toString() {
        return name; // This will be displayed in the JComboBox
    }
}