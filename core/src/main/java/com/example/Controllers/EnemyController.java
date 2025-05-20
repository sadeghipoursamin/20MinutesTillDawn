package com.example.Controllers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.example.Models.Enemy;
import com.example.Models.enums.EnemyType;
import com.example.Models.utilities.GameAssetManager;

import java.util.ArrayList;

public class EnemyController {
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private PlayerController playerController;
    private boolean areTreesPlaced = false;
    private int numberOfTrees = 30;
    private float stateTime = 0f;

    public EnemyController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (!areTreesPlaced) {
            initializeTrees();
        }
    }

    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                Animation<TextureRegion> animation = GameAssetManager.getGameAssetManager()
                    .enemyAnimation(enemy.getEnemyType().getName());

                TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

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
            }
        }
    }

    private void initializeTrees() {
        for (int i = 0; i < numberOfTrees; i++) {
            float x = MathUtils.random(100, GameAssetManager.getGameAssetManager().getMap().getWidth() - 100);
            float y = MathUtils.random(100, GameAssetManager.getGameAssetManager().getMap().getHeight() - 100);

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
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void dispose() {
        enemies.clear();
    }
}
