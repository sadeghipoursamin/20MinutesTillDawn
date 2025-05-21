package com.example.Controllers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.example.Models.Bullet;
import com.example.Models.Enemy;
import com.example.Models.enums.EnemyType;
import com.example.Models.utilities.GameAssetManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnemyController {
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private PlayerController playerController;
    private boolean areTreesPlaced = false;
    private int numberOfTrees = 50;
    private float stateTime = 0f;
    private Map<EnemyType, Animation<TextureRegion>> cachedAnimations = new HashMap<>();
    private GameController gameController;
    private WeaponController weaponController;

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
        stateTime += deltaTime;
        if (!areTreesPlaced) {
            initializeTrees();
        }

        updateTentacles(deltaTime);
        handleBulletCollisions();
    }

    public void tentacleSpawn() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                initializeTentacles();
            }
        }, 3, 3);
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

                batch.draw(
                    currentFrame,
                    enemy.getPosX(),
                    enemy.getPosY(),
                    currentFrame.getRegionWidth() / 2f,
                    currentFrame.getRegionHeight() / 2f,
                    currentFrame.getRegionWidth(),
                    currentFrame.getRegionHeight(),
                    1f,
                    1f,
                    0
                );
            } catch (Exception e) {
                System.err.println("Error rendering enemy: " + e.getMessage());
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


    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }


    public void dispose() {
        enemies.clear();
        cachedAnimations.clear();
    }

    public void handleBulletCollisions() {
        ArrayList<Bullet> bullets = weaponController.getBullets();
        for (Bullet bullet : bullets) {
            Rectangle bulletRect = bullet.getBoundingRectangle();
            if (bulletRect == null) continue;

            for (Enemy enemy : enemies) {
                if (bulletRect.overlaps(enemy.getBoundingRectangle())) {
//                    Gdx.app.log("Bullet", "Collision Detected"); // 9 times
                    enemy.reduceHP(weaponController.getWeapon().getWeaponType().getDamage());
                    if (enemy.getHP() <= 0) {
                        enemy.setDead();
                    }
                }
            }
            updateEnemies();
        }
    }

    public void updateEnemies() {
        enemies.removeIf(enemy -> !enemy.isAlive());
    }


}
