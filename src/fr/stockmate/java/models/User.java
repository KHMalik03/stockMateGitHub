package fr.stockmate.java.models;

import com.google.gson.reflect.TypeToken;
import fr.stockmate.java.utils.JsonFileHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class User {
    private int id;
    private String lastName;
    private String firstName;
    private String login;
    private String password;
    private String role;

    private static final String USERS_FILE = "users.json";

    // Constructor
    public User(int id, String lastName, String firstName, String login, String password, String role) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLastName() { return lastName; }

    public String getFirstName() { return firstName; }

    public String getLogin() { return login; }

    public String getPassword() { return password; }

    public String getRole() { return role; }


    // CRUD Operations
    public void save() {
        List<User> users = getAllUsers();
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId() == this.id)
                .findFirst();

        if (existingUser.isPresent()) {
            users = users.stream()
                    .map(u -> u.getId() == this.id ? this : u)
                    .collect(Collectors.toList());
        } else {
            users.add(this);
        }

        JsonFileHandler.saveToFile(users, USERS_FILE);
    }

    public static List<User> getAllUsers() {
        Type userListType = new TypeToken<List<User>>(){}.getType();
        return JsonFileHandler.loadFromFile(USERS_FILE, userListType);
    }

    public static void deleteUser(int userId) {
        List<User> users = getAllUsers();
        users.removeIf(user -> user.getId() == userId);
        JsonFileHandler.saveToFile(users, USERS_FILE);
    }

    public static User authenticate(String username, String password) {
        List<User> users = getAllUsers();
        return users.stream()
                .filter(user -> user.getLogin().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}