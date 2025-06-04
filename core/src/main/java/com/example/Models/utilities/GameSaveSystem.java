package com.example.Models.utilities;

import com.example.Controllers.EnemyController;
import com.example.Controllers.GameController;
import com.example.Models.Enemy;
import com.example.Models.Player;
import com.example.Models.Seed;
import com.example.Models.Weapon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GameSaveSystem {
    private static final String SAVE_DIRECTORY = "/Users/saminsadeghipour/Desktop/AP/core/src/main/DataBase/saves/";
    private static final String SAVE_FILE_EXTENSION = ".gamesave";
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    static {
        ensureSaveDirectoryExists();
    }

    public static boolean saveGame(String saveSlotName, GameController gameController) {
        try {
            GameSaveData saveData = new GameSaveData();

            // Basic game info
            saveData.saveId = saveSlotName;
            saveData.playerUsername = com.example.Models.App.getCurrentUser() != null ?
                com.example.Models.App.getCurrentUser().getUsername() : "Guest";
            saveData.gameStartTime = gameController.getGame().getStartTime();
            saveData.chosenGameDuration = gameController.getChosenTime();
            saveData.timeSurvived = gameController.getTimeSurvived();

            // Save complete state
            saveData.playerData = savePlayerData(gameController.getPlayerController().getPlayer());
            saveData.weaponData = saveWeaponData(gameController.getWeaponController());
            saveData.enemies = saveEnemiesData(gameController.getEnemyController());
            saveData.worldData = saveWorldData(gameController.getEnemyController());
            saveData.gameProgress = saveGameProgress(gameController.getEnemyController());

            // Write to file
            String fileName = saveSlotName + SAVE_FILE_EXTENSION;
            File saveFile = new File(SAVE_DIRECTORY + fileName);

            try (FileWriter writer = new FileWriter(saveFile)) {
                gson.toJson(saveData, writer);
                System.out.println("Game saved successfully to: " + saveFile.getAbsolutePath());
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static GameSaveData loadGame(String saveSlotName) {
        try {
            String fileName = saveSlotName + SAVE_FILE_EXTENSION;
            File saveFile = new File(SAVE_DIRECTORY + fileName);

            if (!saveFile.exists()) {
                System.err.println("Save file not found: " + saveFile.getAbsolutePath());
                return null;
            }

            try (FileReader reader = new FileReader(saveFile)) {
                GameSaveData saveData = gson.fromJson(reader, GameSaveData.class);
                System.out.println("Game loaded successfully from: " + saveFile.getAbsolutePath());
                return saveData;
            }

        } catch (Exception e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getAvailableSaves() {
        List<String> saves = new ArrayList<>();

        try {
            File saveDir = new File(SAVE_DIRECTORY);
            if (saveDir.exists() && saveDir.isDirectory()) {
                File[] files = saveDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().endsWith(SAVE_FILE_EXTENSION)) {
                            String saveName = file.getName().replace(SAVE_FILE_EXTENSION, "");
                            saves.add(saveName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting save list: " + e.getMessage());
        }

        return saves;
    }

    public static boolean deleteSave(String saveSlotName) {
        try {
            String fileName = saveSlotName + SAVE_FILE_EXTENSION;
            File saveFile = new File(SAVE_DIRECTORY + fileName);

            if (saveFile.exists()) {
                boolean deleted = saveFile.delete();
                if (deleted) {
                    System.out.println("Save deleted: " + saveSlotName);
                }
                return deleted;
            }

            return false;
        } catch (Exception e) {
            System.err.println("Error deleting save: " + e.getMessage());
            return false;
        }
    }

    public static boolean saveExists(String saveSlotName) {
        String fileName = saveSlotName + SAVE_FILE_EXTENSION;
        File saveFile = new File(SAVE_DIRECTORY + fileName);
        return saveFile.exists();
    }

    public static SaveFileInfo getSaveInfo(String saveSlotName) {
        try {
            GameSaveData saveData = loadGame(saveSlotName);
            if (saveData != null) {
                return new SaveFileInfo(
                    saveSlotName,
                    saveData.saveTimestamp,
                    saveData.playerUsername,
                    saveData.timeSurvived,
                    saveData.playerData.level,
                    saveData.playerData.killCount
                );
            }
        } catch (Exception e) {
            System.err.println("Error getting save info: " + e.getMessage());
        }
        return null;
    }

    private static PlayerSaveData savePlayerData(Player player) {
        PlayerSaveData data = new PlayerSaveData();
        data.heroName = player.getHero().getName();
        data.posX = player.getPosX();
        data.posY = player.getPosY();
        data.health = player.getPlayerHealth();
        data.level = player.getLevel();
        data.xp = player.getXp();
        data.killCount = player.getKillCount();
        data.speed = player.getSpeed();
        data.isAlive = player.isAlive();
        data.lightEnabled = player.isLightEnabled();
        data.hasDamageBoost = player.hasDamageBoost();
        data.damageMultiplier = player.getDamageMultiplier();
        data.maxHp = player.getMaxHp();
        return data;
    }

    private static WeaponSaveData saveWeaponData(com.example.Controllers.WeaponController weaponController) {
        WeaponSaveData data = new WeaponSaveData();
        Weapon weapon = weaponController.getWeapon();
        data.weaponTypeName = weapon.getWeaponType().getName();
        data.currentAmmo = weapon.getAmmo();
        data.maxAmmo = weapon.getWeaponType().getAmmoMax();
        data.damage = weapon.getDamage();
        data.projectiles = weapon.getProjectile();
        data.isReloading = weaponController.isReloading();
        data.reloadProgress = weaponController.getReloadProgress();
        data.reloadDuration = weaponController.getReloadDuration();
        return data;
    }

    private static List<EnemySaveData> saveEnemiesData(EnemyController enemyController) {
        List<EnemySaveData> enemiesData = new ArrayList<>();

        for (Enemy enemy : enemyController.getEnemies()) {
            if (enemy.isAlive()) {
                EnemySaveData data = new EnemySaveData();
                data.enemyTypeName = enemy.getEnemyType().getName();
                data.posX = enemy.getPosX();
                data.posY = enemy.getPosY();
                data.hp = enemy.getHP();
                data.isAlive = enemy.isAlive();
                data.lastShotTime = 0; // Default value, will be set for eyebats
                enemiesData.add(data);
            }
        }

        return enemiesData;
    }

    private static WorldSaveData saveWorldData(EnemyController enemyController) {
        WorldSaveData data = new WorldSaveData();
        data.areTreesPlaced = true; // Trees are always placed after initialization
        data.numberOfTrees = 50; // Default number

        // Save seeds
        data.seeds = new ArrayList<>();
        for (Seed seed : enemyController.getSeeds()) {
            SeedSaveData seedData = new SeedSaveData();
            seedData.posX = seed.getX();
            seedData.posY = seed.getY();
            // Note: You'll need to add a getEnemyType() method to Seed class
            // or store the enemy type information in the seed
            data.seeds.add(seedData);
        }

        return data;
    }

    private static GameProgressData saveGameProgress(EnemyController enemyController) {
        GameProgressData data = new GameProgressData();
        data.elderSpawned = enemyController.isElderSpawned();
        data.elderBarrierActive = enemyController.isElderBarrierActive();
        data.elderBarrierRadius = enemyController.getElderBarrierRadius();
        data.elderLastDashTime = enemyController.getElderLastDashTime();

        // These should be tracked based on whether the spawn timers are active
        data.tentacleSpawnActive = true; // Assume active if game is running
        data.eyebatSpawnActive = true; // Assume active if game is running

        data.stateTime = enemyController.getStateTime();
        return data;
    }

    private static void ensureSaveDirectoryExists() {
        try {
            File saveDir = new File(SAVE_DIRECTORY);
            if (!saveDir.exists()) {
                boolean created = saveDir.mkdirs();
                if (created) {
                    System.out.println("Created save directory: " + SAVE_DIRECTORY);
                } else {
                    System.err.println("Failed to create save directory: " + SAVE_DIRECTORY);
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating save directory: " + e.getMessage());
        }
    }

    // Data classes
    public static class GameSaveData {
        public String saveId;
        public long saveTimestamp;
        public String playerUsername;
        public long gameStartTime;
        public long chosenGameDuration;
        public float timeSurvived;
        public PlayerSaveData playerData;
        public WeaponSaveData weaponData;
        public List<EnemySaveData> enemies;
        public WorldSaveData worldData;
        public GameProgressData gameProgress;

        public GameSaveData() {
            this.saveTimestamp = System.currentTimeMillis();
            this.enemies = new ArrayList<>();
        }
    }

    public static class PlayerSaveData {
        public String heroName;
        public float posX, posY;
        public float health;
        public int level;
        public int xp;
        public int killCount;
        public float speed;
        public boolean isAlive;
        public boolean lightEnabled;
        public boolean hasDamageBoost;
        public float damageMultiplier;
        public int maxHp;
    }

    public static class WeaponSaveData {
        public String weaponTypeName;
        public int currentAmmo;
        public int maxAmmo;
        public int damage;
        public int projectiles;
        public boolean isReloading;
        public float reloadProgress;
        public float reloadDuration;
    }

    public static class EnemySaveData {
        public String enemyTypeName;
        public float posX, posY;
        public int hp;
        public boolean isAlive;
        public long lastShotTime;
    }

    public static class WorldSaveData {
        public boolean areTreesPlaced;
        public int numberOfTrees;
        public List<SeedSaveData> seeds;

        public WorldSaveData() {
            this.seeds = new ArrayList<>();
        }
    }

    public static class SeedSaveData {
        public String enemyTypeName;
        public float posX, posY;
    }

    public static class GameProgressData {
        public boolean elderSpawned;
        public boolean elderBarrierActive;
        public float elderBarrierRadius;
        public long elderLastDashTime;
        public boolean tentacleSpawnActive;
        public boolean eyebatSpawnActive;
        public float stateTime;
    }

    public static class SaveFileInfo {
        public final String saveName;
        public final long timestamp;
        public final String playerName;
        public final float timeSurvived;
        public final int level;
        public final int kills;

        public SaveFileInfo(String saveName, long timestamp, String playerName,
                            float timeSurvived, int level, int kills) {
            this.saveName = saveName;
            this.timestamp = timestamp;
            this.playerName = playerName;
            this.timeSurvived = timeSurvived;
            this.level = level;
            this.kills = kills;
        }

        public String getFormattedTimestamp() {
            java.util.Date date = new java.util.Date(timestamp);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }

        public String getFormattedSurvivalTime() {
            int minutes = (int) (timeSurvived / 60);
            int seconds = (int) (timeSurvived % 60);
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
