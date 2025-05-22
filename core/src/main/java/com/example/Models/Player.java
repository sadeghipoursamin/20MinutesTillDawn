package com.example.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.example.Models.enums.Ability;
import com.example.Models.enums.Hero;
import com.example.Models.utilities.GameAssetManager;

import java.util.ArrayList;

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
    private int xp;
    private float posX = 0;
    private float posY = 0;
    private float playerHealth;
    private float time = 0;
    private float speed;
    private boolean isPlayerIdle = true;
    private LightHalo lightHalo;
    private boolean lightEnabled = true;
    private boolean isRunning = false;
    private boolean isAlive;
    private Ability chosenAbility;
    private ArrayList<Ability> abilities;
    private int level;


    public Player(Hero hero) {
        this.hero = hero;
        this.level = 0;
        this.xp = 0;
        this.isAlive = true;
        this.playerHealth = hero.getHP();
        this.speed = hero.getSpeed();
        this.abilities = new ArrayList<>();
        this.chosenAbility = null;
        basedOnHero();

        Texture lightTexture = GameAssetManager.getGameAssetManager().getLightHaloTexture();
        lightHalo = new LightHalo(lightTexture, 300, getLightColorForHero(hero));
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

    public void setSpeed(float speed) {
        this.speed = speed;
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

    private Color getLightColorForHero(Hero hero) {
        switch (hero) {
            case SHANA:
                return new Color(0.8f, 0.3f, 0.8f, 0.6f); // Purple light
            case DIAMOND:
                return new Color(0.3f, 0.6f, 0.9f, 0.6f); // Blue light
            case LILITH:
                return new Color(0.9f, 0.1f, 0.2f, 0.6f); // Red light
            case DASHER:
                return new Color(0.2f, 0.8f, 0.3f, 0.6f); // Green light
            case SCARLET:
                return new Color(1.0f, 0.6f, 0.0f, 0.6f); // Orange light
            default:
                return new Color(1.0f, 0.8f, 0.4f, 0.6f); // Default warm light
        }
    }

    public LightHalo getLightHalo() {
        return lightHalo;
    }

    public boolean isLightEnabled() {
        return lightEnabled;
    }

    public void setLightEnabled(boolean enabled) {
        this.lightEnabled = enabled;
    }

    public void updateLightColor(Color color) {
        if (lightHalo != null) {
            lightHalo.setColor(color);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        this.isRunning = true;
    }

    public Rectangle getBoundingRectangle() {
        getHeroSprite().setPosition(posX, posY);
        return getHeroSprite().getBoundingRectangle();
    }

    public void reduceHealth(float amount) {
        playerHealth -= amount;
        if (playerHealth <= 0) {
            playerHealth = 0;
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void increaseXp(int amount) {
        this.xp += amount;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public void setChosenAbility(Ability ability) {
        this.chosenAbility = ability;
    }

    public boolean checkAbilityUpdate() {
        int neededXp = 20 * (level + 1);
        return xp >= neededXp;
    }

    public void updateLevel() {
        this.level++;
        this.xp = 0;

    }

}
