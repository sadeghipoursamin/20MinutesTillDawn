package com.example.Models;

import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;
import com.example.Models.utilities.DatabaseManager;
import com.example.Models.utilities.FileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    private static Map<String, User> users = new HashMap<>();
    private static List<Hero> heroes = new ArrayList<>();
    private static List<WeaponType> weaponTypes = new ArrayList<>();
    private static List<Integer> times = new ArrayList<>();
    private static User currentUser;
    private static String language = "en";
    private static Settings settings = new Settings(); // Initialize with default settings

    public static void addUser(User user) {
        if (user != null && user.getUsername() != null) {
            users.put(user.getUsername(), user);
            FileManager.saveUser(user);
        }
    }

    public static void save() {
        FileManager.saveUsers(users);
        System.out.println("All users saved to both JSON and database");
    }

    public static void saveSettings() {
        FileManager.saveSettings(settings);
    }

    public static void load() {
        users = FileManager.loadUsers();
        settings = FileManager.loadSettings();
        if (settings == null) {
            settings = new Settings();
        }

        System.out.println("Loaded " + users.size() + " users from storage");
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    public static void setUsers(Map<String, User> users) {
        App.users = users;
        save();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static User findUserByUsername(String username) {
        if (username == null) {
            return null;
        }

        User user = users.get(username);

        if (user == null) {
            user = DatabaseManager.loadUser(username);
            if (user != null) {
                users.put(username, user);
            }
        }

        return user;
    }

    public static void removeUser(String username) {
        if (username != null) {
            users.remove(username);

            FileManager.deleteUser(username);

            if (currentUser != null && username.equals(currentUser.getUsername())) {
                currentUser = null;
            }

            System.out.println("User removed: " + username);
        }
    }

    public static void updateUser(User user) {
        if (user != null && user.getUsername() != null) {
            users.put(user.getUsername(), user);

            FileManager.saveUser(user);

            System.out.println("User updated: " + user.getUsername());
        }
    }

    public static List<Hero> getHeroes() {
        return heroes;
    }

    public static void setHeroes(List<Hero> heroes) {
        App.heroes = heroes;
    }

    public static List<WeaponType> getWeapons() {
        return weaponTypes;
    }

    public static void setWeapons(List<WeaponType> weaponTypes) {
        App.weaponTypes = weaponTypes;
    }

    public static void initialize() {
        heroes.add(Hero.SHANA);
        heroes.add(Hero.DIAMOND);
        heroes.add(Hero.SCARLET);
        heroes.add(Hero.LILITH);
        heroes.add(Hero.DASHER);

        weaponTypes.add(WeaponType.REVOLVER);
        weaponTypes.add(WeaponType.SHOTGUN);
        weaponTypes.add(WeaponType.SMGS_DUAL);

        times.add(2);
        times.add(5);
        times.add(10);
        times.add(20);

        System.out.println("App initialized with heroes, weapons, and times");
    }

    public static List<Integer> getTimes() {
        return times;
    }

    public static String getLanguage() {
        return language;
    }

    public static void changeLanguage() {
        if (language.equals("en")) {
            language = "fr";
        } else {
            language = "en";
        }
        System.out.println("Language changed to: " + language);
    }

    public static Settings getSettings() {
        return settings;
    }

    public static void setSettings(Settings settings) {
        App.settings = settings;
    }

    public static void performMaintenanceSync() {
        FileManager.performDatabaseMaintenance();

        // Reload users after maintenance
        users = FileManager.loadUsers();

        System.out.println("Maintenance sync completed");
    }

    public static void shutdown() {
        save();
        saveSettings();

        DatabaseManager.closeDatabase();

        System.out.println("App shutdown completed - all data saved");
    }

    public static void logout() {
        currentUser = null;
    }
}
