package com.example.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.example.Models.enums.EnemyType;
import com.example.Models.utilities.GameAssetManager;

public class Enemy {
    private GameAssetManager assetManager;
    private Texture treeTexture = GameAssetManager.getGameAssetManager().getTreeMonsterTexture();
    private Sprite treeSprite = new Sprite(treeTexture);
    private Texture eyebatTexture = GameAssetManager.getGameAssetManager().getEyebatTexture();
    private Sprite eyebatSprite = new Sprite(eyebatTexture);
    private Texture elderTexture = GameAssetManager.getGameAssetManager().getElderTexture();
    private Sprite elderSprite = new Sprite(elderTexture);
    private Texture tentacleTexture = GameAssetManager.getGameAssetManager().getTentacleTexture();
    private Sprite tentacleSprite = new Sprite(tentacleTexture);

    private EnemyType enemyType;
    private float posX;
    private float posY;
    private boolean isAlive;
    private int HP;
    private int time = 0;

    private Vector2 direction;

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

    public Texture getTreeTexture() {
        return treeTexture;
    }

    public Sprite getTreeSprite() {
        return treeSprite;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
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

    public Rectangle getBoundingRectangle() {
        treeSprite.setPosition(posX, posY);
        return treeSprite.getBoundingRectangle();
    }

    public void reduceHP(int amount) {
        this.HP -= amount;
        if (this.HP <= 0) {
            this.isAlive = false;
        }
    }

    public void setDead() {
        this.isAlive = false;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }
}
