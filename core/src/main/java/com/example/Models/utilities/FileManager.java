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
        File file = new File(USER_FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, User> loadUsers() {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type userMapType = new TypeToken<Map<String, User>>() {
            }.getType();
            return gson.fromJson(reader, userMapType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static void saveSettings(Settings settings) {
        File file = new File(SETTINGS_FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(settings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings loadSettings() {
        File file = new File(SETTINGS_FILE_PATH);
        if (!file.exists()) {
            return new Settings();
        }

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Settings();
    }
}
