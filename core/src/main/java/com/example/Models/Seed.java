package com.example.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.example.Models.enums.EnemyType;
import com.example.Models.utilities.GameAssetManager;

public class Seed {
    private Texture texture;
    private Sprite sprite;

    private float x;
    private float y;


    public Seed(EnemyType enemyType, float x, float y) {
        switch (enemyType) {
            case TREE -> {
                texture = GameAssetManager.getGameAssetManager().getTreeSeed();
                sprite = new Sprite(texture);
            }
            case EYEBAT -> {
                texture = GameAssetManager.getGameAssetManager().getEyeSeed();
                sprite = new Sprite(texture);
            }
            case TENTACLE_MONSTER -> {
                texture = GameAssetManager.getGameAssetManager().getTentacleSeed();
                sprite = new Sprite(texture);
            }
            case ELDER -> {
                texture = GameAssetManager.getGameAssetManager().getElderSeed();
                sprite = new Sprite(texture);
            }
        }
        this.x = x;
        this.y = y;
    }

    public void dispose() {
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getboundingRectangle() {
        sprite.setPosition(x, y);
        return sprite.getBoundingRectangle();
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
}
