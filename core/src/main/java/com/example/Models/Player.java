package com.example.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.example.Models.enums.Hero;
import com.example.Models.utilities.GameAssetManager;

public class Player {
    private final Texture scarlet = GameAssetManager.getGameAssetManager().getScarletTex();
    private final Sprite scarletSprite = new Sprite(scarlet);
    private final Texture lilith = GameAssetManager.getGameAssetManager().getLilithTex();
    private final Sprite lilithSprite = new Sprite(lilith);
    private final Texture dasher = GameAssetManager.getGameAssetManager().getDasherTex();
    private final Sprite dasherSprite = new Sprite(dasher);
    private final Texture diamond = GameAssetManager.getGameAssetManager().getDiamondTex();
    private final Sprite diamondSprite = new Sprite(diamond);
    private final Texture shana = GameAssetManager.getGameAssetManager().getShanaTex();
    private final Sprite shanaSprite = new Sprite(shana);
    private Hero hero;
    private float posX = 0;
    private float posY = 0;
    private float playerHealth;
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
                shanaSprite.setSize(shana.getWidth() * 2, shana.getHeight() * 2);
            }
            case DIAMOND -> {
                diamondSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                diamondSprite.setSize(diamond.getWidth() * 2, diamond.getHeight() * 2);
            }
            case LILITH -> {
                lilithSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                lilithSprite.setSize(lilith.getWidth() * 2, lilith.getHeight() * 2);
            }
            case DASHER -> {
                dasherSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                dasherSprite.setSize(dasher.getWidth() * 2, dasher.getHeight() * 2);
            }
            case SCARLET -> {
                scarletSprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                scarletSprite.setSize(scarlet.getWidth() * 2, scarlet.getHeight() * 2);
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public Texture getShana() {
        return shana;
    }


    public Sprite getShanaSprite() {
        return shanaSprite;
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
