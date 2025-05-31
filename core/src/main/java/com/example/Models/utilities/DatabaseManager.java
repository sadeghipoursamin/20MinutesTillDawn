package com.example.Models.utilities;

import com.example.Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String DB_PATH = "/Users/saminsadeghipour/Desktop/AP/core/src/main/DataBase/users.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try {
            // Ensure the directory exists
            java.io.File dbFile = new java.io.File(DB_PATH);
            java.io.File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
                System.out.println("Created database directory: " + parentDir.getAbsolutePath());
            }

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String createTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            username TEXT PRIMARY KEY,
                            user_json TEXT NOT NULL
                        )
                    """;

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                    System.out.println("Database initialized at: " + DB_PATH);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveUser(User user) {
        if (user == null || user.getUsername() == null) {
            System.err.println("Cannot save null user or user with null username");
            return;
        }

        String userJson = gson.toJson(user);
        String sql = "INSERT OR REPLACE INTO users (username, user_json) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, userJson);
            pstmt.executeUpdate();

            System.out.println("User saved to database: " + user.getUsername());
        } catch (SQLException e) {
            System.err.println("Error saving user to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveUsers(Map<String, User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No users to save to database");
            return;
        }

        String sql = "INSERT OR REPLACE INTO users (username, user_json) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Start transaction

            for (User user : users.values()) {
                if (user != null && user.getUsername() != null) {
                    String userJson = gson.toJson(user);
                    pstmt.setString(1, user.getUsername());
                    pstmt.setString(2, userJson);
                    pstmt.addBatch();
                }
            }

            pstmt.executeBatch();
            conn.commit(); // Commit transaction

            System.out.println("All users saved to database successfully");
        } catch (SQLException e) {
            System.err.println("Error saving users to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static User loadUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT user_json FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userJson = rs.getString("user_json");
                return gson.fromJson(userJson, User.class);
            }
        } catch (SQLException e) {
            System.err.println("Error loading user from database: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, User> loadAllUsers() {
        Map<String, User> users = new HashMap<>();
        String sql = "SELECT username, user_json FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String userJson = rs.getString("user_json");

                try {
                    User user = gson.fromJson(userJson, User.class);
                    if (user != null) {
                        users.put(username, user);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing user JSON for " + username + ": " + e.getMessage());
                }
            }

            System.out.println("Loaded " + users.size() + " users from database");
        } catch (SQLException e) {
            System.err.println("Error loading users from database: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    public static boolean deleteUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted from database: " + username);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user from database: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean userExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static void closeDatabase() {
        // SQLite doesn't require explicit closing, but this method is here for consistency
        System.out.println("Database operations completed");
    }
}
