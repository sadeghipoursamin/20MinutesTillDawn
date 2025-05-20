package com.example.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.example.Models.enums.Hero;
import com.example.Models.utilities.GameAssetManager;

public class Player {
    private Texture shana = GameAssetManager.getGameAssetManager().getShanaTex();
    private Texture diamond = GameAssetManager.getGameAssetManager().getDiamondTex();
    private Texture lilith = GameAssetManager.getGameAssetManager().getLilithTex();
    private Texture dasher = GameAssetManager.getGameAssetManager().getDasherTex();
    private Texture scarlet = GameAssetManager.getGameAssetManager().getScarletTex();
    private Sprite shanaSprite = new Sprite(shana);
    private Sprite diamondSprite = new Sprite(diamond);
    private Sprite lilithSprite = new Sprite(lilith);
    private Sprite dasherSprite = new Sprite(dasher);
    private Sprite scarletSprite = new Sprite(scarlet);
    private Hero hero;
    private float posX = 0;
    private float posY = 0;
    private float playerHealth;
    private CollisionRect rect;
    private float time = 0;
    private float speed;
    private boolean isPlayerIdle = true;
    private boolean isPlayerRunning = false;

    public Player(Hero hero) {
        this.hero = hero;
        this.playerHealth = hero.getHP();
        this.speed = hero.getSpeed();
        basedOnHero();
    }

    public void basedOnHero() {
        switch (hero) {
            case SHANA -> {
                shanaSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                shanaSprite.setSize(shana.getWidth() * 3, shana.getHeight() * 3);
                rect = new CollisionRect((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight(), shana.getWidth() * 3, shana.getHeight() * 3);
            }
            case DIAMOND -> {
                diamondSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                diamondSprite.setSize(diamond.getWidth() * 3, diamond.getHeight() * 3);
                rect = new CollisionRect((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight(), diamond.getWidth() * 3, diamond.getHeight() * 3);
            }
            case LILITH -> {
                lilithSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                lilithSprite.setSize(lilith.getWidth() * 3, lilith.getHeight() * 3);
                rect = new CollisionRect((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight(), lilith.getWidth() * 3, lilith.getHeight() * 3);
            }
            case DASHER -> {
                dasherSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                dasherSprite.setSize(dasher.getWidth() * 3, dasher.getHeight() * 3);
                rect = new CollisionRect((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight(), dasher.getWidth() * 3, dasher.getHeight() * 3);
            }
            case SCARLET -> {
                scarletSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                scarletSprite.setSize(scarlet.getWidth() * 3, scarlet.getHeight() * 3);
                rect = new CollisionRect((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight(), scarlet.getWidth() * 3, scarlet.getHeight() * 3);
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public Texture getShana() {
        return shana;
    }

    public void setShana(Texture shana) {
        this.shana = shana;
    }

    public Sprite getShanaSprite() {
        return shanaSprite;
    }

    public void setShanaSprite(Sprite shanaSprite) {
        this.shanaSprite = shanaSprite;
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

    public float getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(float playerHealth) {
        this.playerHealth = playerHealth;
    }

    public CollisionRect getRect() {
        return rect;
    }

    public void setRect(CollisionRect rect) {
        this.rect = rect;
    }


    public boolean isPlayerIdle() {
        return isPlayerIdle;
    }

    public void setPlayerIdle(boolean playerIdle) {
        isPlayerIdle = playerIdle;
    }

    public boolean isPlayerRunning() {
        return isPlayerRunning;
    }

    public void setPlayerRunning(boolean playerRunning) {
        isPlayerRunning = playerRunning;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Texture getDiamond() {
        return diamond;
    }

    public Texture getLilith() {
        return lilith;
    }

    public Texture getDasher() {
        return dasher;
    }

    public Texture getScarlet() {
        return scarlet;
    }

    public Sprite getDiamondSprite() {
        return diamondSprite;
    }

    public Sprite getLilithSprite() {
        return lilithSprite;
    }

    public Sprite getDasherSprite() {
        return dasherSprite;
    }

    public Sprite getScarletSprite() {
        return scarletSprite;
    }

    public Hero getHero() {
        return hero;
    }

    public Sprite getHeroSprite() {
        switch (hero) {
            case SHANA -> {
                return shanaSprite;
            }
            case SCARLET -> {
                return scarletSprite;
            }
            case DIAMOND -> {
                return diamondSprite;
            }
            case LILITH -> {
                return lilithSprite;
            }
            case DASHER -> {
                return dasherSprite;
            }
        }
        return null;
    }
}
