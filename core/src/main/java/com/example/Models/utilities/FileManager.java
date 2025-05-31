package com.example.Models.utilities;

import com.example.Models.Settings;
import com.example.Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private static final String USER_FILE_PATH = "/Users/saminsadeghipour/Desktop/AP/core/src/main/DataBase/users.json";
    private static final String SETTINGS_FILE_PATH = "/Users/saminsadeghipour/Desktop/AP/core/src/main/DataBase/settings.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveUsers(Map<String, User> users) {
        // Save to JSON file
        saveUsersToJson(users);

        // Save to database
        DatabaseManager.saveUsers(users);
    }

    private static void saveUsersToJson(Map<String, User> users) {
        File file = new File(USER_FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(users, writer);
            System.out.println("Users saved to JSON file successfully");
        } catch (IOException e) {
            System.err.println("Error saving users to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Map<String, User> loadUsers() {
        Map<String, User> jsonUsers = loadUsersFromJson();
        Map<String, User> dbUsers = DatabaseManager.loadAllUsers();

        // Merge users from both sources, preferring database over JSON
        Map<String, User> mergedUsers = new HashMap<>(jsonUsers);
        mergedUsers.putAll(dbUsers); // Database takes precedence

        // If database is empty but JSON has data, save JSON data to database
        if (dbUsers.isEmpty() && !jsonUsers.isEmpty()) {
            System.out.println("Database is empty, migrating JSON data to database...");
            DatabaseManager.saveUsers(jsonUsers);
            mergedUsers = jsonUsers;
        }
        // If JSON is empty but database has data, save database data to JSON
        else if (jsonUsers.isEmpty() && !dbUsers.isEmpty()) {
            System.out.println("JSON is empty, backing up database data to JSON...");
            saveUsersToJson(dbUsers);
            mergedUsers = dbUsers;
        }

        System.out.println("Loaded " + mergedUsers.size() + " users total");
        return mergedUsers;
    }

    private static Map<String, User> loadUsersFromJson() {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            System.out.println("JSON users file does not exist, returning empty map");
            return new HashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type userMapType = new TypeToken<Map<String, User>>() {
            }.getType();
            Map<String, User> users = gson.fromJson(reader, userMapType);

            if (users == null) {
                users = new HashMap<>();
            }

            System.out.println("Loaded " + users.size() + " users from JSON");
            return users;
        } catch (IOException e) {
            System.err.println("Error loading users from JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static void saveUser(User user) {
        if (user == null) {
            System.err.println("Cannot save null user");
            return;
        }

        // Save to database
        DatabaseManager.saveUser(user);

        // Also update the JSON file with all current users
        Map<String, User> allUsers = loadUsers();
        allUsers.put(user.getUsername(), user);
        saveUsersToJson(allUsers);

        System.out.println("User saved to both JSON and database: " + user.getUsername());
    }

    public static void deleteUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Cannot delete user with null or empty username");
            return;
        }

        // Delete from database
        boolean dbDeleted = DatabaseManager.deleteUser(username);

        // Delete from JSON
        Map<String, User> users = loadUsersFromJson();
        boolean jsonDeleted = users.remove(username) != null;

        if (jsonDeleted) {
            saveUsersToJson(users);
        }

        if (dbDeleted || jsonDeleted) {
            System.out.println("User deleted from storage: " + username);
        } else {
            System.out.println("User not found in storage: " + username);
        }
    }

    public static void saveSettings(Settings settings) {
        File file = new File(SETTINGS_FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(settings, writer);
            System.out.println("Settings saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Settings loadSettings() {
        File file = new File(SETTINGS_FILE_PATH);
        if (!file.exists()) {
            System.out.println("Settings file does not exist, returning default settings");
            return new Settings();
        }

        try (FileReader reader = new FileReader(file)) {
            Settings settings = gson.fromJson(reader, Settings.class);
            return settings != null ? settings : new Settings();
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
        return new Settings();
    }

    public static void performDatabaseMaintenance() {
        try {
            System.out.println("Performing database maintenance...");

            // Load from both sources
            Map<String, User> jsonUsers = loadUsersFromJson();
            Map<String, User> dbUsers = DatabaseManager.loadAllUsers();

            // Ensure both sources are synchronized
            Map<String, User> allUsers = new HashMap<>(jsonUsers);
            allUsers.putAll(dbUsers);

            // Save to both sources
            saveUsersToJson(allUsers);
            DatabaseManager.saveUsers(allUsers);

            System.out.println("Database maintenance completed. Synchronized " + allUsers.size() + " users.");
        } catch (Exception e) {
            System.err.println("Error during database maintenance: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
