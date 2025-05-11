package com.example.Models;

import com.example.Models.utilities.FileManager;

import java.util.HashMap;
import java.util.Map;

public class App {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public static void save() {
        FileManager.saveUsers(users);
    }

    public static void load() {
        FileManager.loadUsers();
    }
}
