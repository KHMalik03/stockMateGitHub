package fr.stockmate.java.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonFileHandler {
    private static Gson gson;
    private static final String DATA_DIRECTORY = "src/data/";

    static {
        // Initialize Gson with LocalDate adapter
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        // create data directory if it doesn't exist
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static <T> void saveToFile(List<T> items, String filePath) {
        try (FileWriter writer = new FileWriter(DATA_DIRECTORY + filePath)) {
            gson.toJson(items, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> loadFromFile(String filePath, Type typeToken) {
        File file = new File(DATA_DIRECTORY + filePath);

        // handling non-existent file
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveToFile(new ArrayList<>(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // reading
        try (FileReader reader = new FileReader(file)) {
            List<T> result = gson.fromJson(reader, typeToken);
            return result != null ? result : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}