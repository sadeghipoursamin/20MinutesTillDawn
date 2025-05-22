package com.example.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.example.Models.utilities.GameAssetManager;

public class Bullet {
    private Texture texture = new Texture(GameAssetManager.getGameAssetManager().getBullet());
    private Sprite sprite;
    private Texture eyebatBulletTexture = GameAssetManager.getGameAssetManager().getEyebatBullet();
    private Sprite eyebatBulletSprite = new Sprite(eyebatBulletTexture);

    private int damage = 5;
    private float x;
    private float y;
    private Vector2 direction;
    private boolean isPlayersBullet;
    private long initializationTime;

    public Bullet(float x, float y, boolean isPlayersBullet) {
        this.isPlayersBullet = isPlayersBullet;
        if (isPlayersBullet) {
            sprite = new Sprite(texture);
            sprite.setSize(20, 20);
            sprite.setX((float) Gdx.graphics.getWidth() / 2);
            sprite.setY((float) Gdx.graphics.getHeight() / 2);
        } else {
            eyebatBulletSprite.setSize(20, 20);
            eyebatBulletSprite.setX((float) Gdx.graphics.getWidth() / 2);
            eyebatBulletSprite.setY((float) Gdx.graphics.getHeight() / 2);
        }

        this.x = x;
        this.y = y;

    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getDamage() {
        return damage;
    }


    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public void dispose() {
        texture.dispose();
    }

    public Rectangle getBoundingRectangle() {
        sprite.setPosition(x, y);
        return sprite.getBoundingRectangle();
    }

    public Rectangle getBoundingRectangleForEyebat() {
        eyebatBulletSprite.setPosition(x, y);
        return eyebatBulletSprite.getBoundingRectangle();
    }

    public Texture getEyebatBulletTexture() {
        return eyebatBulletTexture;
    }

    public Sprite getEyebatBulletSprite() {
        return eyebatBulletSprite;
    }

    public long getInitializationTime() {
        return initializationTime;
    }

    public void setInitializationTime(long initializationTime) {
        this.initializationTime = initializationTime;
    }
}
