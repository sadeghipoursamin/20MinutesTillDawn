package com.example.Controllers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.example.Main;
import com.example.Models.Bullet;
import com.example.Models.Enemy;
import com.example.Models.Seed;
import com.example.Models.enums.EnemyType;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.MainMenuView;
import com.example.Views.UpdatePlayerWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EnemyController {
    private final long invincibilityDuration = 1_000_000_000L; // 1 second in nanoseconds
    private WeaponController weaponController;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private PlayerController playerController;
    private boolean areTreesPlaced = false;
    private int numberOfTrees = 50;
    private float stateTime = 0f;
    private boolean gamePaused = false;
    private ArrayList<Seed> seeds = new ArrayList<>();
    private Map<EnemyType, Animation<TextureRegion>> cachedAnimations = new HashMap<>();
    private GameController gameController;
    private long lastHitTime = 0;

    private Timer.Task tentacleSpawnTask;
    private Timer.Task eyebatSpawnTask;

    public EnemyController(PlayerController playerController) {
        this.playerController = playerController;

        for (EnemyType type : EnemyType.values()) {
            try {
                Animation<TextureRegion> animation = GameAssetManager.getGameAssetManager().enemyAnimation(type.getName());
                if (animation != null) {
                    cachedAnimations.put(type, animation);
                }
            } catch (Exception e) {
                System.err.println("Error loading animation for " + type.getName() + ": " + e.getMessage());
            }
        }
    }

    public void setWeaponController(WeaponController weaponController) {
        this.weaponController = weaponController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void update(float deltaTime) {
        if (gamePaused) {
            return;
        }
        stateTime += deltaTime;
        if (!areTreesPlaced) {
            initializeTrees();
        }
        updateEyebats(deltaTime);
        updateTentacles(deltaTime);
        handleBulletCollisions();
        handlePlayerCollisions();
        handlePlayerSeedCollisions();
    }

    public void tentacleSpawn() {
        tentacleSpawnTask = new Timer.Task() {
            @Override
            public void run() {
                if (!gamePaused) { // Only spawn if game is not paused
                    initializeTentacles();
                }
            }
        };
        Timer.schedule(tentacleSpawnTask, 3, 3);
    }

    public void eyeBatSpawn() {
        eyebatSpawnTask = new Timer.Task() {
            @Override
            public void run() {
                if (!gamePaused) { // Only spawn if game is not paused
                    initializeEyebats();
                }
            }
        };
        Timer.schedule(eyebatSpawnTask, 10, 10);
    }

    public void render(SpriteBatch batch) {
        if (batch == null) return;

        for (Enemy enemy : enemies) {
            if (enemy == null || !enemy.isAlive()) continue;

            try {
                Animation<TextureRegion> animation = cachedAnimations.get(enemy.getEnemyType());
                if (animation == null) continue;

                TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
                if (currentFrame == null || currentFrame.getTexture() == null) continue;

                // Default scale
                float scale = 1f;
                if (enemy.getEnemyType().equals(EnemyType.TREE)) {
                    scale = 2.5f;
                }
                if (enemy.getEnemyType().equals(EnemyType.TENTACLE_MONSTER)) {
                    scale = 2.f;
                }

                float regionWidth = currentFrame.getRegionWidth();
                float regionHeight = currentFrame.getRegionHeight();

                batch.draw(
                    currentFrame,
                    enemy.getPosX(),
                    enemy.getPosY(),
                    regionWidth / 2f,
                    regionHeight / 2f,
                    regionWidth,
                    regionHeight,
                    scale,
                    scale,
                    0
                );
            } catch (Exception e) {
                System.err.println("Error rendering enemy: " + e.getMessage());
            }

            for (Seed seed : seeds) {
                if (seed != null && seed.getSprite() != null) {
                    seed.getSprite().draw(batch);
                }
            }
        }
    }

    private void initializeTrees() {
        try {
            for (int i = 0; i < numberOfTrees; i++) {
                float mapWidth = GameAssetManager.getGameAssetManager().getMap().getWidth();
                float mapHeight = GameAssetManager.getGameAssetManager().getMap().getHeight();

                float x = MathUtils.random(100, mapWidth - 100);
                float y = MathUtils.random(100, mapHeight - 100);

                float playerX = playerController.getPlayer().getPosX();
                float playerY = playerController.getPlayer().getPosY();

                float distance = Vector2.dst(playerX, playerY, x, y);
                if (distance > 200) {
                    Enemy newEnemy = new Enemy(x, y, EnemyType.TREE);
                    enemies.add(newEnemy);
                } else {
                    i--;
                }
            }
            areTreesPlaced = true;
        } catch (Exception e) {
            System.err.println("Error initializing trees: " + e.getMessage());
            areTreesPlaced = true;
        }
    }

    private void initializeTentacles() {
        long elapsedTime = (TimeUtils.millis() - gameController.getGame().getStartTime()) / 1000; //seconds
        int tentacleCount = Math.toIntExact(elapsedTime / 30);
        for (int i = 0; i < tentacleCount; i++) {
            float mapWidth = GameAssetManager.getGameAssetManager().getMap().getWidth();
            float mapHeight = GameAssetManager.getGameAssetManager().getMap().getHeight();

            float x = MathUtils.random(50, mapWidth - 50);
            float y = MathUtils.random(50, mapHeight - 50);
            float playerX = playerController.getPlayer().getPosX();
            float playerY = playerController.getPlayer().getPosY();

            float distance = Vector2.dst(playerX, playerY, x, y);
            if (distance > 400) {
                Enemy newEnemy = new Enemy(x, y, EnemyType.TENTACLE_MONSTER);
                enemies.add(newEnemy);
            } else {
                i--;
            }
        }
    }

    private void initializeEyebats() {
        long elapsedTime = (TimeUtils.millis() - gameController.getGame().getStartTime()) / 1000; //seconds
        if (elapsedTime > (gameController.getChosenTime() / 4)) {
            int eyebatCount = Math.toIntExact(((4 * elapsedTime) - gameController.getChosenTime() + 30) / 30);
            for (int i = 0; i < eyebatCount; i++) {
                float mapWidth = GameAssetManager.getGameAssetManager().getMap().getWidth();
                float mapHeight = GameAssetManager.getGameAssetManager().getMap().getHeight();

                float x = MathUtils.random(50, mapWidth - 50);
                float y = MathUtils.random(50, mapHeight - 50);
                float playerX = playerController.getPlayer().getPosX();
                float playerY = playerController.getPlayer().getPosY();

                float distance = Vector2.dst(playerX, playerY, x, y);
                if (distance > 300) {
                    Enemy newEnemy = new Enemy(x, y, EnemyType.EYEBAT);
                    enemies.add(newEnemy);
                } else {
                    i--;
                }
            }
        }
    }

    public void updateTentacles(float delta) {
        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();
        Vector2 playerPos = new Vector2(playerX, playerY);

        for (Enemy enemy : enemies) {
            if (enemy.getEnemyType().equals(EnemyType.TENTACLE_MONSTER)) {
                Vector2 enemyPos = new Vector2(enemy.getPosX(), enemy.getPosY());
                Vector2 direction = new Vector2(playerPos).sub(enemyPos).nor();

                float speed = 100f;
                Vector2 movement = new Vector2(direction).scl(speed * delta);
                enemy.setPosX(enemy.getPosX() + movement.x);
                enemy.setPosY(enemy.getPosY() + movement.y);
            }
        }
    }

    public void updateEyebats(float delta) {
        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();
        Vector2 playerPos = new Vector2(playerX, playerY);
        for (Enemy enemy : enemies) {
            if (enemy.getEnemyType().equals(EnemyType.EYEBAT)) {
                Vector2 enemyPos = new Vector2(enemy.getPosX(), enemy.getPosY());
                Vector2 direction = new Vector2(playerPos).sub(enemyPos).nor();

                float speed = 70f;
                Vector2 movement = new Vector2(direction).scl(speed * delta);
                enemy.setPosX(enemy.getPosX() + movement.x);
                enemy.setPosY(enemy.getPosY() + movement.y);
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void dispose() {
        // Cancel timer tasks when disposing
        if (tentacleSpawnTask != null) {
            tentacleSpawnTask.cancel();
        }
        if (eyebatSpawnTask != null) {
            eyebatSpawnTask.cancel();
        }

        enemies.clear();
        cachedAnimations.clear();
    }

    public void handleBulletCollisions() {
        ArrayList<Bullet> bullets = weaponController.getBullets();
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle bulletRect = bullet.getBoundingRectangle();
            if (bulletRect == null) continue;

            for (Enemy enemy : enemies) {
                if (bulletRect.overlaps(enemy.getBoundingRectangle())) {
                    enemy.reduceHP(weaponController.getWeapon().getWeaponType().getDamage());
                    if (!enemy.isAlive()) {
                        initializeSeeds(enemy.getEnemyType(), enemy.getPosX(), enemy.getPosY());
                    }
                    bullet.dispose();
                    bulletIterator.remove();
                    break;
                }
            }
        }

        updateEnemies();
    }

    public void handlePlayerCollisions() {
        long currentTime = TimeUtils.nanoTime();

        for (Enemy enemy : enemies) {
            if (playerController.getPlayer().getBoundingRectangle().overlaps(enemy.getBoundingRectangle())) {
                if (currentTime - lastHitTime >= invincibilityDuration) {
                    float damage = 0.1F;
                    playerController.getPlayer().reduceHealth(damage);
                    lastHitTime = currentTime;

                    if (!playerController.getPlayer().isAlive()) {
                        navigateToMainMenu();
                    }
                }
            }
        }
    }


    public void updateEnemies() {
        enemies.removeIf(enemy -> !enemy.isAlive());
    }

    public void navigateToMainMenu() {
        Main.getMain().getScreen().dispose();
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

    public void initializeSeeds(EnemyType enemyType, float x, float y) {
        Seed seed = new Seed(enemyType, x, y);
        seed.getSprite().setPosition(x, y);
        seed.getSprite().setScale(2f);
        seeds.add(seed);
    }

    public void handlePlayerSeedCollisions() {
        Iterator<Seed> iterator = seeds.iterator();
        while (iterator.hasNext()) {
            Seed seed = iterator.next();
            if (playerController.getPlayer().getBoundingRectangle().overlaps(seed.getboundingRectangle())) {
                playerController.getPlayer().increaseXp(3);
                if (playerController.getPlayer().checkAbilityUpdate()) {
                    playerController.getPlayer().updateLevel();
                    showLevelUpWindow();
                }
                iterator.remove();
                break;
            }
        }
    }

    private void showLevelUpWindow() {
        gamePaused = true;
        playerController.setInputEnabled(false);

        if (gameController.getView() != null) {
            UpdatePlayerWindow levelUpWindow = new UpdatePlayerWindow(
                GameAssetManager.getGameAssetManager().getSkin(),
                playerController.getPlayer(), weaponController
            );

            levelUpWindow.setOnComplete(() -> {
                gamePaused = false;
                playerController.setInputEnabled(true);
                playerController.getPlayer().updateLevel();

                com.badlogic.gdx.Gdx.input.setInputProcessor(gameController.getView());
            });

            com.badlogic.gdx.Gdx.input.setInputProcessor(gameController.getView().getStage());
            gameController.getView().getStage().addActor(levelUpWindow);
        }
    }

    public void pauseGame() {
        gamePaused = true;
        playerController.setInputEnabled(false);
    }

    public void resumeGame() {
        gamePaused = false;
        playerController.setInputEnabled(true);
    }

    public boolean isGamePaused() {
        return gamePaused;
    }
}
