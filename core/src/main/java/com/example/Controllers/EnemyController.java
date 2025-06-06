package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.example.Main;
import com.example.Models.Bullet;
import com.example.Models.Enemy;
import com.example.Models.Seed;
import com.example.Models.enums.EnemyType;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GameSaveSystem;
import com.example.Views.GameCompletionWindow;
import com.example.Views.GameView;
import com.example.Views.MainMenuView;
import com.example.Views.UpdatePlayerWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EnemyController {
    private final long invincibilityDuration = 1_000_000_000L; // 1 second
    private final long elderDashCooldown = 5000; // 5 seconds
    private final float elderBarrierDamageInterval = 1000; // 1 second
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
    private boolean autoAimEnabled = false;
    private ArrayList<Bullet> eyebatBullets = new ArrayList<>();
    private Timer.Task tentacleSpawnTask;
    private Timer.Task eyebatSpawnTask;
    private Map<Enemy, Long> eyebatLastShotTime = new HashMap<>();
    private Map<Enemy, Vector2> enemyKnockbackVelocities = new HashMap<>();
    private Map<Enemy, Float> enemyKnockbackDurations = new HashMap<>();
    private Timer.Task elderSpawnTask;
    private Timer.Task elderDashTask;
    private Enemy elderEnemy = null;
    private boolean elderSpawned = false;
    private long elderLastDashTime = 0;
    // Elder barrier properties
    private boolean elderBarrierActive = false;
    private float elderBarrierRadius;
    private float elderBarrierMaxRadius;
    private float elderBarrierX, elderBarrierY;
    private Texture elderBarrierTexture;
    private long elderLastBarrierDamage = 0;

    //death animation
    private Animation<Texture> deathAnimation;
    private Map<Vector2, Float> deathAnimations = new HashMap<>();
    private float deathAnimationDuration = 0.4f;

    //damage animation
    private Animation<Texture> damageAnimation;
    private Map<Vector2, Float> damageAnimations = new HashMap<>();
    private float damageAnimationDuration = 0.3f;

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
        deathAnimation = GameAssetManager.getGameAssetManager().deathAnimation();
        damageAnimation = GameAssetManager.getGameAssetManager().damageAnimation();

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
        if (autoAimEnabled) {
            updateAutoAimCursor();
        }

        updateEyebats(deltaTime);
        updateBullets();
        updateTentacles(deltaTime);
        updateElder(deltaTime);
        handleBulletCollisions();
        handlePlayerCollisions();
        handlePlayerSeedCollisions();
        handleEyebatBulletCollisions();
        handleEyebatShooting();
        updateKnockbackEffects(deltaTime);
        updateDeathAnimations(deltaTime);
        updateDamageAnimations(deltaTime);
    }

    public void tentacleSpawn() {
        tentacleSpawnTask = new Timer.Task() {
            @Override
            public void run() {
                if (!gamePaused) {
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
                if (!gamePaused) {
                    initializeEyebats();
                }
            }
        };
        Timer.schedule(eyebatSpawnTask, 10, 10);
    }

    private void playDamageAnimation(float x, float y) {
        float offsetY = 50f;
        damageAnimations.put(new Vector2(x, y + offsetY), 0f);
    }

    private void updateDamageAnimations(float deltaTime) {
        Iterator<Map.Entry<Vector2, Float>> iterator = damageAnimations.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Vector2, Float> entry = iterator.next();
            float animationTime = entry.getValue() + deltaTime;

            if (animationTime >= damageAnimationDuration) {
                iterator.remove();
            } else {
                entry.setValue(animationTime);
            }
        }
    }

    private void renderDamageAnimations(SpriteBatch batch) {
        if (damageAnimation == null) return;

        for (Map.Entry<Vector2, Float> entry : damageAnimations.entrySet()) {
            Vector2 position = entry.getKey();
            float animationTime = entry.getValue();

            if (animationTime < damageAnimationDuration) {
                Texture currentFrame = damageAnimation.getKeyFrame(animationTime, false);
                if (currentFrame != null) {
                    float scale = 1.5f;
                    float alpha = 1f - (animationTime / damageAnimationDuration);

                    batch.setColor(1f, 1f, 1f, alpha);
                    batch.draw(
                        currentFrame,
                        position.x - (currentFrame.getWidth() * scale) / 2,
                        position.y - (currentFrame.getHeight() * scale) / 2,
                        currentFrame.getWidth() * scale,
                        currentFrame.getHeight() * scale
                    );
                    batch.setColor(1f, 1f, 1f, 1f); // Reset color
                }
            }
        }
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

                float scale = 1f;
                if (enemy.getEnemyType().equals(EnemyType.TREE)) {
                    scale = 2.5f;
                }
                if (enemy.getEnemyType().equals(EnemyType.TENTACLE_MONSTER)) {
                    scale = 2.f;
                }
                if (enemy.getEnemyType().equals(EnemyType.ELDER)) {
                    scale = 3.0f;
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
        }

        renderDeathAnimations(batch);
        renderDamageAnimations(batch);

        for (Seed seed : seeds) {
            if (seed != null && seed.getSprite() != null) {
                seed.getSprite().draw(batch);
            }
        }

        for (Bullet bullet : eyebatBullets) {
            if (bullet != null && bullet.getEyebatBulletSprite() != null) {
                bullet.getEyebatBulletSprite().draw(batch);
            }
        }
    }

    private void renderDeathAnimations(SpriteBatch batch) {
        if (deathAnimation == null) return;

        Iterator<Map.Entry<Vector2, Float>> iterator = deathAnimations.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Vector2, Float> entry = iterator.next();
            Vector2 position = entry.getKey();
            float animationTime = entry.getValue();

            if (animationTime < deathAnimationDuration) {
                Texture currentFrame = deathAnimation.getKeyFrame(animationTime, false);
                if (currentFrame != null) {
                    float scale = 2.0f;
                    batch.draw(
                        currentFrame,
                        position.x - (currentFrame.getWidth() * scale) / 2,
                        position.y - (currentFrame.getHeight() * scale) / 2,
                        currentFrame.getWidth() * scale,
                        currentFrame.getHeight() * scale
                    );
                }
            } else {
                iterator.remove();
            }
        }
    }

    private void playDeathAnimation(float x, float y) {
        deathAnimations.put(new Vector2(x, y), 0f);
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

    private void updateDeathAnimations(float deltaTime) {
        for (Map.Entry<Vector2, Float> entry : deathAnimations.entrySet()) {
            entry.setValue(entry.getValue() + deltaTime);
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
                    eyebatLastShotTime.put(newEnemy, TimeUtils.millis());
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

    private void handleEyebatShooting() {
        long currentTime = TimeUtils.millis();
        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();

        for (Enemy enemy : enemies) {
            if (enemy.getEnemyType().equals(EnemyType.EYEBAT) && enemy.isAlive()) {
                Long lastShotTime = eyebatLastShotTime.computeIfAbsent(enemy, k -> currentTime);

                if (currentTime - lastShotTime >= 3000) { // 3000 milliseconds = 3 seconds
                    Bullet bullet = new Bullet(enemy.getPosX(), enemy.getPosY(), false, false);
                    bullet.setDamage(1);
                    bullet.setInitializationTime(currentTime);

                    Vector2 direction = new Vector2(playerX - enemy.getPosX(), playerY - enemy.getPosY()).nor();
                    bullet.getEyebatBulletSprite().setPosition(enemy.getPosX(), enemy.getPosY());
                    bullet.setDirection(direction);
                    eyebatBullets.add(bullet);

                    eyebatLastShotTime.put(enemy, currentTime);
                }
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void dispose() {
        if (tentacleSpawnTask != null) {
            tentacleSpawnTask.cancel();
        }
        if (eyebatSpawnTask != null) {
            eyebatSpawnTask.cancel();
        }
        if (elderSpawnTask != null) {
            elderSpawnTask.cancel();
        }
        if (elderDashTask != null) {
            elderDashTask.cancel();
        }
        if (elderBarrierTexture != null) {
            elderBarrierTexture.dispose();
        }

        enemies.clear();
        cachedAnimations.clear();
        eyebatLastShotTime.clear();
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
                    applySmoothKnockbackToEnemy(enemy, bullet);

                    enemy.reduceHP(weaponController.getWeapon().getWeaponType().getDamage());
                    if (!enemy.isAlive()) {
                        playDeathAnimation(enemy.getPosX(), enemy.getPosY());

                        playerController.getPlayer().increaseKillCount();
                        initializeSeeds(enemy.getEnemyType(), enemy.getPosX(), enemy.getPosY());
                        if (enemy.getEnemyType().equals(EnemyType.EYEBAT)) {
                            eyebatLastShotTime.remove(enemy);
                        }
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

                    playDamageAnimation(
                        playerController.getPlayer().getPosX(),
                        playerController.getPlayer().getPosY()
                    );

                    if (!playerController.getPlayer().isAlive()) {
                        handlePlayerDeath();
                    }
                }
            }
        }
    }

    public void updateEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (!enemy.isAlive()) {
                if (enemy.getEnemyType().equals(EnemyType.EYEBAT)) {
                    eyebatLastShotTime.remove(enemy);
                }
                iterator.remove();
            }
        }
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
                GameAssetManager.getGameAssetManager().popSound();
                int xpGain = getXpValueForSeed(seed);

                boolean canLevelUp = playerController.getPlayer().gainXpAndCheckLevelUp(xpGain);

                if (canLevelUp) {
                    playerController.getPlayer().updateLevel();
                    showLevelUpWindow();
                }

                iterator.remove();
                break;
            }
        }
    }

    private int getXpValueForSeed(Seed seed) {
        return 3;
    }

    public void showLevelUpWindow() {
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

    public Enemy getClosestEnemy() {
        if (enemies.isEmpty()) return null;

        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();
        Enemy closest = null;
        float closestDistance = Float.MAX_VALUE;

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

            float distance = Vector2.dst(enemy.getPosX(), enemy.getPosY(), playerX, playerY);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = enemy;
            }
        }
        return closest;
    }

    private void updateAutoAimCursor() {
        Enemy closest = getClosestEnemy();
        if (closest == null || gameController == null || gameController.getView() == null) return;

        Vector3 enemyWorldPos = new Vector3(closest.getPosX(), closest.getPosY(), 0);
        Vector3 screenCoords = gameController.getView().getCamera().project(enemyWorldPos);

        int screenX = (int) screenCoords.x;
        int screenY = Gdx.graphics.getHeight() - (int) screenCoords.y;

        Gdx.input.setCursorPosition(screenX, screenY);
    }

    public void changeAutoAimState() {
        autoAimEnabled = !autoAimEnabled;
    }

    public void updateBullets() {
        Iterator<Bullet> iterator = eyebatBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            float speed = 5.0f;

            bullet.getEyebatBulletSprite().setX(bullet.getEyebatBulletSprite().getX() + bullet.getDirection().x * speed);
            bullet.getEyebatBulletSprite().setY(bullet.getEyebatBulletSprite().getY() + bullet.getDirection().y * speed);

            bullet.setX(bullet.getEyebatBulletSprite().getX());
            bullet.setY(bullet.getEyebatBulletSprite().getY());

            long timeNow = TimeUtils.millis();
            long timeSinceCreation = timeNow - bullet.getInitializationTime();
            if (timeSinceCreation > 2000) {
                iterator.remove();
            }
        }
    }

    public void handleEyebatBulletCollisions() {
        Iterator<Bullet> iterator = eyebatBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (playerController.getPlayer().getBoundingRectangle().overlaps(bullet.getBoundingRectangleForEyebat())) {
                playerController.getPlayer().reduceHealth(bullet.getDamage());

                playDamageAnimation(
                    playerController.getPlayer().getPosX(),
                    playerController.getPlayer().getPosY()
                );

                iterator.remove();

                if (!playerController.getPlayer().isAlive()) {
                    handlePlayerDeath();
                }
                break;
            }
        }
    }

    private void applySmoothKnockbackToEnemy(Enemy enemy, Bullet bullet) {
        Vector2 knockbackDirection = new Vector2(bullet.getDirection().x, bullet.getDirection().y);

        float knockbackSpeed = 200f;

        float knockbackDuration = 0.3f;

        enemyKnockbackVelocities.put(enemy, knockbackDirection.scl(knockbackSpeed));
        enemyKnockbackDurations.put(enemy, knockbackDuration);
    }

    private void updateKnockbackEffects(float deltaTime) {
        Iterator<Map.Entry<Enemy, Vector2>> velocityIterator = enemyKnockbackVelocities.entrySet().iterator();

        while (velocityIterator.hasNext()) {
            Map.Entry<Enemy, Vector2> entry = velocityIterator.next();
            Enemy enemy = entry.getKey();
            Vector2 velocity = entry.getValue();

            Float duration = enemyKnockbackDurations.get(enemy);
            if (duration == null || duration <= 0) {
                velocityIterator.remove();
                enemyKnockbackDurations.remove(enemy);
                continue;
            }

            float newX = enemy.getPosX() + velocity.x * deltaTime;
            float newY = enemy.getPosY() + velocity.y * deltaTime;

            float mapWidth = GameAssetManager.getGameAssetManager().getMap().getWidth();
            float mapHeight = GameAssetManager.getGameAssetManager().getMap().getHeight();
            newX = MathUtils.clamp(newX, 50, mapWidth - 50);
            newY = MathUtils.clamp(newY, 50, mapHeight - 50);

            enemy.setPosX(newX);
            enemy.setPosY(newY);

            duration -= deltaTime;
            velocity.scl(0.95f);
            enemyKnockbackDurations.put(enemy, duration);
        }
    }

    public void elderSpawn() {
        elderSpawnTask = new Timer.Task() {
            @Override
            public void run() {
                if (!gamePaused && !elderSpawned) {
                    long elapsedTime = (TimeUtils.millis() - gameController.getGame().getStartTime()) / 1000;
                    long halfGameTime = gameController.getChosenTime() / 2;

                    if (gameController.getTimeSurvived() >= gameController.getChosenTime() * 30) {
                        initializeElder();
                        elderSpawned = true;
                        this.cancel();
                    }
                }
            }
        };
        Timer.schedule(elderSpawnTask, 1, 1);
    }
    

    public void initializeElder() {
        try {
            float mapWidth = GameAssetManager.getGameAssetManager().getMap().getWidth();
            float mapHeight = GameAssetManager.getGameAssetManager().getMap().getHeight();

            float x, y;
            int edge = MathUtils.random(0, 3);

            y = switch (edge) {
                case 0 -> {
                    x = MathUtils.random(100, mapWidth - 100);
                    yield mapHeight - 100;
                }
                case 1 -> {
                    x = mapWidth - 100;
                    yield MathUtils.random(100, mapHeight - 100);
                }
                case 2 -> {
                    x = MathUtils.random(100, mapWidth - 100);
                    yield 100;
                }
                default -> {
                    x = 100;
                    yield MathUtils.random(100, mapHeight - 100);
                }
            };

            elderEnemy = new Enemy(x, y, EnemyType.ELDER);
            enemies.add(elderEnemy);
            elderLastDashTime = TimeUtils.millis();
            elderSpawned = true;

            initializeElderBarrier();

            System.out.println("Elder enemy spawned at: " + x + ", " + y);

        } catch (Exception e) {
            System.err.println("Error initializing elder: " + e.getMessage());
        }
    }

    public void updateElder(float deltaTime) {
        if (elderEnemy == null || !elderEnemy.isAlive()) {
            if (elderBarrierActive) {
                elderBarrierActive = false;
            }
            return;
        }

        long currentTime = TimeUtils.millis();

        if (currentTime - elderLastDashTime >= elderDashCooldown) {
            performElderDash();
            elderLastDashTime = currentTime;
        }

        updateElderBarrier(deltaTime);

        checkElderBarrierCollision(currentTime);
    }

    private void performElderDash() {
        if (elderEnemy == null || !elderEnemy.isAlive()) return;

        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();

        Vector2 elderPos = new Vector2(elderEnemy.getPosX(), elderEnemy.getPosY());
        Vector2 playerPos = new Vector2(playerX, playerY);
        Vector2 direction = new Vector2(playerPos).sub(elderPos).nor();

        float dashSpeed = 1500f;
        Vector2 dashMovement = new Vector2(direction).scl(dashSpeed);

        Timer.Task dashTask = new Timer.Task() {
            final float maxDashTime = 0.5f;
            float dashTime = 0f;

            @Override
            public void run() {
                if (elderEnemy == null || !elderEnemy.isAlive()) {
                    this.cancel();
                    return;
                }

                float deltaTime = Gdx.graphics.getDeltaTime();
                dashTime += deltaTime;

                if (dashTime >= maxDashTime) {
                    this.cancel();
                    return;
                }

                float newX = elderEnemy.getPosX() + dashMovement.x * deltaTime;
                float newY = elderEnemy.getPosY() + dashMovement.y * deltaTime;

                float mapWidth = GameAssetManager.getGameAssetManager().getMap().getWidth();
                float mapHeight = GameAssetManager.getGameAssetManager().getMap().getHeight();
                newX = MathUtils.clamp(newX, 50, mapWidth - 50);
                newY = MathUtils.clamp(newY, 50, mapHeight - 50);

                elderEnemy.setPosX(newX);
                elderEnemy.setPosY(newY);
            }
        };

        Timer.schedule(dashTask, 0, 1 / 60f); // 60 FPS updates

    }

    private void updateElderBarrier(float deltaTime) {
        if (!elderBarrierActive) return;

        long gameElapsedTime = (TimeUtils.millis() - gameController.getGame().getStartTime()) / 1000;
        long totalGameTime = gameController.getChosenTime();
        long halfGameTime = totalGameTime / 2;
        long timeAfterElderSpawn = gameElapsedTime - halfGameTime;

        if (timeAfterElderSpawn > 0) {
            float shrinkFactor = (float) timeAfterElderSpawn / (float) (totalGameTime - halfGameTime);
            elderBarrierRadius = elderBarrierMaxRadius * (1f - shrinkFactor * 0.007f);
            elderBarrierRadius = Math.max(elderBarrierRadius, 300f);
        }
    }

    private void initializeElderBarrier() {
        elderBarrierActive = true;
        elderBarrierMaxRadius = Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 2.0f;
        elderBarrierRadius = elderBarrierMaxRadius;
        elderBarrierX = Gdx.graphics.getWidth() / 2.0f;
        elderBarrierY = Gdx.graphics.getHeight() / 2.0f;

    }

    public boolean isElderBarrierActive() {
        return elderBarrierActive && elderEnemy != null && elderEnemy.isAlive();
    }

    public float getElderBarrierRadius() {
        return elderBarrierRadius;
    }

    public float getElderBarrierX() {
        return elderBarrierX;
    }

    public float getElderBarrierY() {
        return elderBarrierY;
    }

    private void checkElderBarrierCollision(long currentTime) {
        if (!elderBarrierActive) return;

        if (elderEnemy == null || !elderEnemy.isAlive()) {
            elderBarrierActive = false;
            return;
        }
        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();

        Vector3 playerWorldPos = new Vector3(playerX, playerY, 0);
        Vector3 playerScreenPos = gameController.getView().getCamera().project(playerWorldPos);

        float screenCenterX = Gdx.graphics.getWidth() / 2f;
        float screenCenterY = Gdx.graphics.getHeight() / 2f;

        float distanceFromCenter = Vector2.dst(
            playerScreenPos.x, playerScreenPos.y,
            screenCenterX, screenCenterY
        );

        if (distanceFromCenter > elderBarrierRadius) {
            if (currentTime - elderLastBarrierDamage >= elderBarrierDamageInterval) {
                playerController.getPlayer().reduceHealth(0.5f);
                elderLastBarrierDamage = currentTime;

                playDamageAnimation(playerX, playerY);

                System.out.println("Player outside barrier! Distance: " + distanceFromCenter + ", Radius: " + elderBarrierRadius);

                if (!playerController.getPlayer().isAlive()) {
                    handlePlayerDeath();
                }
            }
        }
    }

    public void handlePlayerDeath() {
        if (gameController != null && gameController.getView() != null) {
            gameController.endGameDueToDeath();

            GameCompletionWindow completionWindow = new GameCompletionWindow(
                GameAssetManager.getGameAssetManager().getSkin(),
                gameController,
                false
            );

            completionWindow.setOnMainMenu(this::navigateToMainMenu);

            completionWindow.setOnPlayAgain(() -> {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new GameView(
                    new GameController(
                        gameController.getHero(),
                        gameController.getWeaponType(),
                        gameController.getChosenTime()
                    ),
                    GameAssetManager.getGameAssetManager().getSkin()
                ));
            });

            gameController.getView().getStage().addActor(completionWindow);
            com.badlogic.gdx.Gdx.input.setInputProcessor(gameController.getView().getStage());

            pauseGame();
        } else {
            navigateToMainMenu();
        }
    }

    public boolean isElderSpawned() {
        return elderSpawned;
    }

    public void setElderSpawned(boolean elderSpawned) {
        this.elderSpawned = elderSpawned;
    }

    public long getElderLastDashTime() {
        return elderLastDashTime;
    }

    public void setElderLastDashTime(long elderLastDashTime) {
        this.elderLastDashTime = elderLastDashTime;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public ArrayList<Seed> getSeeds() {
        return seeds;
    }

    public void addSeed(Seed seed) {
        this.seeds.add(seed);
    }

    public void clearSeeds() {
        this.seeds.clear();
    }

    public void restoreElderState(boolean spawned, long lastDashTime, boolean barrierActive, float barrierRadius) {
        this.elderSpawned = spawned;
        this.elderLastDashTime = lastDashTime;
        this.elderBarrierActive = barrierActive;
        this.elderBarrierRadius = barrierRadius;

        if (spawned && elderEnemy == null) {
            initializeElder();
        }
    }

    public void restoreEnemyState(java.util.List<GameSaveSystem.EnemySaveData> savedEnemies) {
        // Clear existing enemies
        enemies.clear();

        // Recreate enemies from saved data
        for (GameSaveSystem.EnemySaveData enemyData : savedEnemies) {
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
                        enemies.add(enemy);

                        // If it's an eyebat, restore shooting state
                        if (enemyType == EnemyType.EYEBAT) {
                            eyebatLastShotTime.put(enemy, enemyData.lastShotTime);
                        }

                        // If it's an elder, update elder reference
                        if (enemyType == EnemyType.ELDER) {
                            elderEnemy = enemy;
                            elderSpawned = true;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error restoring enemy: " + e.getMessage());
            }
        }

        System.out.println("Restored " + enemies.size() + " enemies from save data");
    }

    private EnemyType getEnemyTypeByName(String name) {
        for (EnemyType type : EnemyType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public void restoreSeedsState(java.util.List<GameSaveSystem.SeedSaveData> savedSeeds) {
        seeds.clear();

        for (GameSaveSystem.SeedSaveData seedData : savedSeeds) {
            try {
                EnemyType enemyType = getEnemyTypeByName(seedData.enemyTypeName);
                if (enemyType != null) {
                    Seed seed = new Seed(enemyType, seedData.posX, seedData.posY);
                    seed.getSprite().setPosition(seedData.posX, seedData.posY);
                    seed.getSprite().setScale(2f);
                    seeds.add(seed);
                }
            } catch (Exception e) {
                System.err.println("Error restoring seed: " + e.getMessage());
            }
        }

        System.out.println("Restored " + seeds.size() + " seeds from save data");
    }

    public java.util.List<GameSaveSystem.SeedSaveData> getCurrentSeedsData() {
        java.util.List<GameSaveSystem.SeedSaveData> seedsData = new java.util.ArrayList<>();

        for (Seed seed : seeds) {
            GameSaveSystem.SeedSaveData seedData = new GameSaveSystem.SeedSaveData();
            seedData.posX = seed.getX();
            seedData.posY = seed.getY();
            seedsData.add(seedData);
        }

        return seedsData;
    }

    public void stopAllSpawnTimers() {
        if (tentacleSpawnTask != null) {
            tentacleSpawnTask.cancel();
            tentacleSpawnTask = null;
        }
        if (eyebatSpawnTask != null) {
            eyebatSpawnTask.cancel();
            eyebatSpawnTask = null;
        }
        if (elderSpawnTask != null) {
            elderSpawnTask.cancel();
            elderSpawnTask = null;
        }
        if (elderDashTask != null) {
            elderDashTask.cancel();
            elderDashTask = null;
        }
    }

    public void restartSpawnTimers(GameSaveSystem.GameProgressData gameProgress) {
        stopAllSpawnTimers();

        if (gameProgress.tentacleSpawnActive) {
            tentacleSpawn();
        }

        if (gameProgress.eyebatSpawnActive) {
            eyeBatSpawn();
        }


    }


}
