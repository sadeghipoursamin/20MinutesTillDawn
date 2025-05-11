package com.example.Models;

import com.example.Models.utilities.FileManager;

import java.util.HashMap;
import java.util.Map;

public class App {
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser;
    public static void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public static void save() {
        FileManager.saveUsers(users);
    }

    public static void load() {
        users = FileManager.loadUsers();
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static User findUserByUsername(String username) {
        return users.get(username);
    }
}
