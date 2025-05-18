package com.example.Models;

import com.badlogic.gdx.Input;
import com.example.Main;
import com.example.Models.utilities.GrayscaleShader;

public class Settings {
    // Music settings
    private float musicVolume;
    private String currentMusic;

    // SFX settings
    private boolean sfxEnabled;

    // Key bindings
    private int moveUpKey;
    private int moveDownKey;
    private int moveLeftKey;
    private int moveRightKey;
    private int shootKey;
    private int reloadKey;

    // Game settings
    private boolean autoReloadEnabled;
    private boolean grayscaleEnabled;

    // Default constructor with default values
    public Settings() {
        // Default music settings
        this.musicVolume = 0.5f;
        this.currentMusic = "Main Theme";

        // Default SFX settings
        this.sfxEnabled = true;

        // Default key bindings
        this.moveUpKey = Input.Keys.W;
        this.moveDownKey = Input.Keys.S;
        this.moveLeftKey = Input.Keys.A;
        this.moveRightKey = Input.Keys.D;
        this.shootKey = Input.Keys.SPACE;
        this.reloadKey = Input.Keys.R;

        // Default game settings
        this.autoReloadEnabled = true;
        this.grayscaleEnabled = false;
    }

    // Getters and setters

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public String getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(String currentMusic) {
        this.currentMusic = currentMusic;
    }

    public boolean isSfxEnabled() {
        return sfxEnabled;
    }

    public void setSfxEnabled(boolean sfxEnabled) {
        this.sfxEnabled = sfxEnabled;
    }

    public int getMoveUpKey() {
        return moveUpKey;
    }

    public void setMoveUpKey(int moveUpKey) {
        this.moveUpKey = moveUpKey;
    }

    public int getMoveDownKey() {
        return moveDownKey;
    }

    public void setMoveDownKey(int moveDownKey) {
        this.moveDownKey = moveDownKey;
    }

    public int getMoveLeftKey() {
        return moveLeftKey;
    }

    public void setMoveLeftKey(int moveLeftKey) {
        this.moveLeftKey = moveLeftKey;
    }

    public int getMoveRightKey() {
        return moveRightKey;
    }

    public void setMoveRightKey(int moveRightKey) {
        this.moveRightKey = moveRightKey;
    }

    public int getShootKey() {
        return shootKey;
    }

    public void setShootKey(int shootKey) {
        this.shootKey = shootKey;
    }

    public int getReloadKey() {
        return reloadKey;
    }

    public void setReloadKey(int reloadKey) {
        this.reloadKey = reloadKey;
    }

    public boolean isAutoReloadEnabled() {
        return autoReloadEnabled;
    }

    public void setAutoReloadEnabled(boolean autoReloadEnabled) {
        this.autoReloadEnabled = autoReloadEnabled;
    }

    public boolean isGrayscaleEnabled() {
        return grayscaleEnabled;
    }

    public void setGrayscaleEnabled(boolean grayscaleEnabled) {
        this.grayscaleEnabled = grayscaleEnabled;

        // Apply the setting globally if Main has been initialized
        if (Main.getBatch() != null) {
            if (grayscaleEnabled) {
                Main.getBatch().setShader(GrayscaleShader.getShader());
            } else {
                Main.getBatch().setShader(null);
            }
        }
    }
}
