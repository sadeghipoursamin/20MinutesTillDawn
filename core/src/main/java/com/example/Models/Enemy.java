package com.example.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.example.Models.enums.EnemyType;
import com.example.Models.utilities.GameAssetManager;

public class Enemy {
    private GameAssetManager assetManager;
    private Texture texture = GameAssetManager.getGameAssetManager().getEnemy1Texture();
    private Sprite sprite = new Sprite(texture);
    private EnemyType enemyType;
    private float posX;
    private float posY;
    private boolean isAlive;
    private int HP;
    private int time = 0;

    public Enemy(float x, float y, EnemyType enemyType) {
        this.posX = x;
        this.posY = y;
        this.isAlive = true;
        this.enemyType = enemyType;
        this.HP = enemyType.getHP();
    }


    public GameAssetManager getAssetManager() {
        return assetManager;
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getHP() {
        return HP;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
