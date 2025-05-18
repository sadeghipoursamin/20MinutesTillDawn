package com.example.Models;

import com.example.Models.enums.Hero;
import com.example.Models.enums.Weapon;
import com.example.Models.utilities.FileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    private static Map<String, User> users = new HashMap<>();
    private static List<Hero> heroes = new ArrayList<>();
    private static List<Weapon> weapons = new ArrayList<>();
    private static List<Integer> times = new ArrayList<>();
    private static User currentUser;
    private static String language = "en";
    private static Settings settings = new Settings(); // Initialize with default settings

    public static void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public static void save() {
        FileManager.saveUsers(users);
    }

    public static void saveSettings() {
        FileManager.saveSettings(settings);
    }

    public static void load() {
        users = FileManager.loadUsers();
        settings = FileManager.loadSettings();
        if (settings == null) {
            settings = new Settings(); // If settings couldn't be loaded, use defaults
        }
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    public static void setUsers(Map<String, User> users) {
        App.users = users;
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

    public static List<Hero> getHeroes() {
        return heroes;
    }

    public static void setHeroes(List<Hero> heroes) {
        App.heroes = heroes;
    }

    public static List<Weapon> getWeapons() {
        return weapons;
    }

    public static void setWeapons(List<Weapon> weapons) {
        App.weapons = weapons;
    }

    public static void initialize() {
        heroes.add(Hero.SHANA);
        heroes.add(Hero.DIAMOND);
        heroes.add(Hero.SCARLET);
        heroes.add(Hero.LILITH);
        heroes.add(Hero.DASHER);

        weapons.add(Weapon.REVOLVER);
        weapons.add(Weapon.SHOTGUN);
        weapons.add(Weapon.SMGS_DUAL);

        times.add(2);
        times.add(5);
        times.add(10);
        times.add(20);
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
    }

    public static Settings getSettings() {
        return settings;
    }

    public static void setSettings(Settings settings) {
        App.settings = settings;
    }
}
