package com.example.Models.utilities;

import com.example.Controllers.EnemyController;
import com.example.Controllers.GameController;
import com.example.Controllers.PlayerController;
import com.example.Controllers.WeaponController;
import com.example.Models.Enemy;
import com.example.Models.Player;
import com.example.Models.Weapon;
import com.example.Models.enums.EnemyType;
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

            // This will be set up when the view is created
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

            // Restore game timing
            restoreGameTiming(gameController, saveData);

            // Restore player state
            restorePlayerState(gameController.getPlayerController(), saveData.playerData);

            // Restore weapon state
            restoreWeaponState(gameController.getWeaponController(), saveData.weaponData);

            // Restore enemies
            restoreEnemiesState(gameController.getEnemyController(), saveData.enemies);

            // Restore world state
            restoreWorldState(gameController.getEnemyController(), saveData.worldData);

            // Restore game progress
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
        // Update the game start time to account for already survived time
        long currentTime = com.badlogic.gdx.utils.TimeUtils.millis();
        long adjustedStartTime = currentTime - (long) (saveData.timeSurvived * 1000);

        gameController.getGame().setStartTime(adjustedStartTime);

        // Set the survived time directly
        // This would require adding a setter method to GameController
        // gameController.setTimeSurvived(saveData.timeSurvived);
    }

    private static void restorePlayerState(PlayerController playerController, GameSaveSystem.PlayerSaveData playerData) {
        Player player = playerController.getPlayer();

        // Restore position
        player.setPosX(playerData.posX);
        player.setPosY(playerData.posY);

        // Restore health and stats
        player.setPlayerHealth(playerData.health);
        player.setSpeed(playerData.speed);

        for (int i = 0; i < playerData.level; i++) {
            player.cheatIncreaseLevel(); // Use existing method to properly level up
        }

        // Restore XP (after leveling)
        // This would require adding a setter method to Player
        // player.setXp(playerData.xp);

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

        // Restore reload state
        if (weaponData.isReloading) {
            // Start reloading process
            // This would require exposing reload methods or state in WeaponController
            weaponController.setReloadDuration(2f); // Default duration
        }

        System.out.println("Weapon state restored - Ammo: " + weaponData.currentAmmo +
            "/" + weaponData.maxAmmo +
            ", Damage: " + weaponData.damage);
    }

    private static void restoreEnemiesState(EnemyController enemyController,
                                            java.util.List<GameSaveSystem.EnemySaveData> enemiesData) {
        // Clear existing enemies first
        enemyController.getEnemies().clear();

        // Recreate enemies from save data
        for (GameSaveSystem.EnemySaveData enemyData : enemiesData) {
            try {
                EnemyType enemyType = getEnemyTypeByName(enemyData.enemyTypeName);
                if (enemyType != null) {
                    Enemy enemy = new Enemy(enemyData.posX, enemyData.posY, enemyType);

                    // Restore HP (damage the enemy if needed)
                    int damageToApply = enemyType.getHP() - enemyData.hp;
                    if (damageToApply > 0) {
                        enemy.reduceHP(damageToApply);
                    }

                    if (enemy.isAlive()) {
                        enemyController.getEnemies().add(enemy);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error restoring enemy: " + e.getMessage());
            }
        }

        System.out.println("Restored " + enemiesData.size() + " enemies");
    }

    private static void restoreWorldState(EnemyController enemyController,
                                          GameSaveSystem.WorldSaveData worldData) {
        // Restore seeds if any
        if (worldData.seeds != null) {
            for (GameSaveSystem.SeedSaveData seedData : worldData.seeds) {
                try {
                    EnemyType enemyType = getEnemyTypeByName(seedData.enemyTypeName);
                    if (enemyType != null) {
                        // This would require exposing the seed creation method
                        enemyController.initializeSeeds(enemyType, seedData.posX, seedData.posY);
                    }
                } catch (Exception e) {
                    System.err.println("Error restoring seed: " + e.getMessage());
                }
            }
        }

        System.out.println("World state restored");
    }

    private static void restoreGameProgress(EnemyController enemyController,
                                            GameSaveSystem.GameProgressData gameProgress) {
        // Restore spawn states
        if (gameProgress.tentacleSpawnActive) {
            enemyController.tentacleSpawn();
        }

        if (gameProgress.eyebatSpawnActive) {
            enemyController.eyeBatSpawn();
        }

        if (gameProgress.elderSpawned) {
            enemyController.elderSpawn();
        }

        System.out.println("Game progress restored");
    }

    // Helper method to convert string to EnemyType
    private static EnemyType getEnemyTypeByName(String name) {
        for (EnemyType type : EnemyType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
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
