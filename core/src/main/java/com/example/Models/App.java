package com.example.Models;

import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;
import com.example.Models.utilities.AvatarManager;
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
    private static boolean avatarManagerInitialized = false;

    public static void addUser(User user) {
        if (user != null && user.getUsername() != null) {
            // Ensure user has a valid avatar
            if (user.getAvatarPath() == null || user.getAvatarPath().isEmpty()) {
                initializeAvatarManagerIfNeeded();
                user.setAvatarPath(AvatarManager.getInstance().getDefaultAvatarPath());
                user.setCustomAvatar(false);
            }

            users.put(user.getUsername(), user);
            FileManager.saveUser(user);
            System.out.println("User added: " + user.getUsername() + " with avatar: " + user.getAvatarPath());
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

        // Initialize avatar manager and update users who don't have avatars
        initializeAvatarManagerIfNeeded();
        updateUsersWithDefaultAvatars();

        System.out.println("Loaded " + users.size() + " users from storage");
    }

    private static void initializeAvatarManagerIfNeeded() {
        if (!avatarManagerInitialized) {
            // This will initialize the avatar manager singleton
            AvatarManager.getInstance();
            avatarManagerInitialized = true;
            System.out.println("Avatar Manager initialized");
        }
    }

    private static void updateUsersWithDefaultAvatars() {
        boolean usersUpdated = false;

        for (User user : users.values()) {
            if (user.getAvatarPath() == null || user.getAvatarPath().isEmpty()) {
                user.setAvatarPath(AvatarManager.getInstance().getDefaultAvatarPath());
                user.setCustomAvatar(false);
                usersUpdated = true;
                System.out.println("Updated user " + user.getUsername() + " with default avatar");
            }
        }

        if (usersUpdated) {
            save(); // Save the updated users
        }
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

        // Ensure current user has a valid avatar
        if (currentUser != null && (currentUser.getAvatarPath() == null || currentUser.getAvatarPath().isEmpty())) {
            initializeAvatarManagerIfNeeded();
            currentUser.setAvatarPath(AvatarManager.getInstance().getDefaultAvatarPath());
            currentUser.setCustomAvatar(false);
            updateUser(currentUser);
        }
    }

    public static User findUserByUsername(String username) {
        if (username == null) {
            return null;
        }

        User user = users.get(username);

        if (user == null) {
            user = DatabaseManager.loadUser(username);
            if (user != null) {
                // Ensure loaded user has a valid avatar
                if (user.getAvatarPath() == null || user.getAvatarPath().isEmpty()) {
                    initializeAvatarManagerIfNeeded();
                    user.setAvatarPath(AvatarManager.getInstance().getDefaultAvatarPath());
                    user.setCustomAvatar(false);
                }
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

            System.out.println("User updated: " + user.getUsername() + " with avatar: " + user.getAvatarPath());
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

        // Initialize avatar manager
        initializeAvatarManagerIfNeeded();

        System.out.println("App initialized with heroes, weapons, times, and avatar system");
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

        // Update any users missing avatars
        updateUsersWithDefaultAvatars();

        System.out.println("Maintenance sync completed");
    }

    public static void shutdown() {
        save();
        saveSettings();

        // Dispose avatar manager
        if (avatarManagerInitialized) {
            AvatarManager.getInstance().dispose();
        }

        DatabaseManager.closeDatabase();

        System.out.println("App shutdown completed - all data saved, avatar manager disposed");
    }

    public static void logout() {
        currentUser = null;
    }

    public static AvatarManager getAvatarManager() {
        initializeAvatarManagerIfNeeded();
        return AvatarManager.getInstance();
    }
}
