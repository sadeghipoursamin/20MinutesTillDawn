package com.example.Models.utilities;

import com.example.Controllers.EnemyController;
import com.example.Controllers.GameController;
import com.example.Controllers.PlayerController;
import com.example.Controllers.WeaponController;
import com.example.Models.Player;
import com.example.Models.Weapon;
import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;
import com.example.Models.utilities.GameSaveSystem.GameSaveData;

public class GameLoader {

    public static GameController loadGameFromSave(String saveSlotName) {
        try {
            GameSaveData saveData = GameSaveSystem.loadGame(saveSlotName);
            if (saveData == null) {
                System.err.println("Failed to load save data for: " + saveSlotName);
                return null;
            }

            // Get hero and weapon type from save data
            Hero hero = Hero.getHeroByName(saveData.playerData.heroName);
            WeaponType weaponType = WeaponType.getWeaponTypeByName(saveData.weaponData.weaponTypeName);

            if (hero == null || weaponType == null) {
                System.err.println("Invalid hero or weapon type in save data");
                return null;
            }

            // Create new game controller with saved parameters
            GameController gameController = new GameController(hero, weaponType, saveData.chosenGameDuration);

            // Mark as loaded game
            gameController.setLoadedGame(true);

            System.out.println("GameController created from save data: " + saveSlotName);
            return gameController;

        } catch (Exception e) {
            System.err.println("Error loading game from save: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean restoreGameState(GameController gameController, String saveSlotName) {
        try {
            GameSaveData saveData = GameSaveSystem.loadGame(saveSlotName);
            if (saveData == null) {
                System.err.println("Failed to load save data for restoration");
                return false;
            }

            // Restore game timing first
            restoreGameTiming(gameController, saveData);

            // Restore player state
            restorePlayerState(gameController.getPlayerController(), saveData.playerData);

            // Restore weapon state
            restoreWeaponState(gameController.getWeaponController(), saveData.weaponData);

            // Restore enemies
            restoreEnemiesState(gameController.getEnemyController(), saveData.enemies);

            // Restore seeds
            restoreSeedsState(gameController.getEnemyController(), saveData.worldData);

            // Restore game progress (elder state, spawn timers, etc.)
            restoreGameProgress(gameController.getEnemyController(), saveData.gameProgress);

            System.out.println("Game state restored successfully from: " + saveSlotName);
            return true;

        } catch (Exception e) {
            System.err.println("Error restoring game state: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void restoreGameTiming(GameController gameController, GameSaveData saveData) {
        // Set the survived time
        gameController.setTimeSurvived(saveData.timeSurvived);

        // Initialize as loaded game
        gameController.initializeLoadedGame(saveData.timeSurvived);

        System.out.println("Restored game timing - Time survived: " + saveData.timeSurvived + " seconds");
    }

    private static void restorePlayerState(PlayerController playerController, GameSaveSystem.PlayerSaveData playerData) {
        Player player = playerController.getPlayer();

        // Restore position
        player.setPosX(playerData.posX);
        player.setPosY(playerData.posY);

        // Restore health and stats
        player.setPlayerHealth(playerData.health);
        player.setSpeed(playerData.speed);

        // Restore level (use existing method to properly level up)
        for (int i = 0; i < playerData.level; i++) {
            player.cheatIncreaseLevel();
        }

        // Restore XP - need to add setXp method to Player class
        // For now, we'll add XP incrementally
        int xpToAdd = playerData.xp;
        while (xpToAdd > 0) {
            int chunk = Math.min(xpToAdd, 10);
            player.increaseXp(chunk);
            xpToAdd -= chunk;
        }

        // Restore kill count
        for (int i = 0; i < playerData.killCount; i++) {
            player.increaseKillCount();
        }

        // Restore states
        player.setLightEnabled(playerData.lightEnabled);

        // Restore damage boost if it was active
        if (playerData.hasDamageBoost) {
            player.activateDamageBoost();
        }

        System.out.println("Player state restored - Level: " + playerData.level +
            ", Health: " + playerData.health +
            ", Position: (" + playerData.posX + ", " + playerData.posY + ")");
    }

    private static void restoreWeaponState(WeaponController weaponController, GameSaveSystem.WeaponSaveData weaponData) {
        Weapon weapon = weaponController.getWeapon();

        // Restore ammo
        weapon.setAmmo(weaponData.currentAmmo);

        // Restore weapon modifications
        weapon.setDamage(weaponData.damage);
        weapon.setProjectile(weaponData.projectiles);

        // Restore reload duration
        weaponController.setReloadDuration(weaponData.reloadDuration);

        // Note: We don't restore the reloading state as it's a transient action

        System.out.println("Weapon state restored - Ammo: " + weaponData.currentAmmo +
            "/" + weaponData.maxAmmo +
            ", Damage: " + weaponData.damage);
    }

    private static void restoreEnemiesState(EnemyController enemyController,
                                            java.util.List<GameSaveSystem.EnemySaveData> enemiesData) {
        // Restore enemies using the existing method
        enemyController.restoreEnemyState(enemiesData);

        System.out.println("Restored " + enemiesData.size() + " enemies from save");
    }

    private static void restoreSeedsState(EnemyController enemyController,
                                          GameSaveSystem.WorldSaveData worldData) {
        if (worldData.seeds != null) {
            enemyController.restoreSeedsState(worldData.seeds);
            System.out.println("Restored " + worldData.seeds.size() + " seeds from save");
        }
    }

    private static void restoreGameProgress(EnemyController enemyController,
                                            GameSaveSystem.GameProgressData gameProgress) {
        // Restore elder state
        if (gameProgress.elderSpawned) {
            enemyController.setElderSpawned(true);
            enemyController.setElderLastDashTime(gameProgress.elderLastDashTime);

            // Restore elder barrier state
            enemyController.restoreElderState(
                gameProgress.elderSpawned,
                gameProgress.elderLastDashTime,
                gameProgress.elderBarrierActive,
                gameProgress.elderBarrierRadius
            );
        }

        // Restore animation state time
        enemyController.setStateTime(gameProgress.stateTime);

        // Restart spawn timers based on saved state
        enemyController.restartSpawnTimers(gameProgress);

        System.out.println("Game progress restored - Elder spawned: " + gameProgress.elderSpawned);
    }

    public static boolean isValidSaveFile(String saveSlotName) {
        try {
            GameSaveData saveData = GameSaveSystem.loadGame(saveSlotName);
            if (saveData == null) {
                return false;
            }

            // Basic validation
            if (saveData.playerData == null || saveData.weaponData == null) {
                return false;
            }

            // Check if hero and weapon types are valid
            Hero hero = Hero.getHeroByName(saveData.playerData.heroName);
            WeaponType weaponType = WeaponType.getWeaponTypeByName(saveData.weaponData.weaponTypeName);

            return hero != null && weaponType != null;

        } catch (Exception e) {
            System.err.println("Error validating save file: " + e.getMessage());
            return false;
        }
    }

    public static String getSaveDisplayInfo(String saveSlotName) {
        try {
            GameSaveSystem.SaveFileInfo info = GameSaveSystem.getSaveInfo(saveSlotName);
            if (info != null) {
                return String.format(
                    "Player: %s\nLevel: %d\nTime: %s\nKills: %d\nSaved: %s",
                    info.playerName,
                    info.level,
                    info.getFormattedSurvivalTime(),
                    info.kills,
                    info.getFormattedTimestamp()
                );
            }
        } catch (Exception e) {
            System.err.println("Error getting save display info: " + e.getMessage());
        }

        return "Invalid save file";
    }
}
