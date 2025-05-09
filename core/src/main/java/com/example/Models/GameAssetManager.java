package com.example.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    private GameAssetManager gameAssetManager;
    private Skin skin = new Skin(Gdx.files.internal("Skin/pixthulhu-ui.json"));

    public GameAssetManager gameAssetManager() {
        if(gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager; 
    }
    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
