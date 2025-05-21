package com.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.Controllers.OpeningMenuController;
import com.example.Models.App;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.OpeningMenuView;


public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;
    private static Sound clickSound;

    public static Main getMain() {
        return main;
    }

    public static void setMain(Main main) {
        Main.main = main;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static void playSound() {
        if (App.getSettings().isSfxEnabled() && clickSound != null) {
            clickSound.play();
        }
    }

    @Override
    public void create() {
        main = this;

        batch = new SpriteBatch();

        App.load();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/click.wav"));

        if (App.getSettings().isGrayscaleEnabled()) {
            GrayscaleShader.initialize();
            batch.setShader(GrayscaleShader.getShader());
        }

        setScreen(new OpeningMenuView(new OpeningMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void dispose() {
        App.save();
        App.saveSettings();

        if (batch != null) {
            batch.dispose();
            batch = null;
        }

        if (clickSound != null) {
            clickSound.dispose();
            clickSound = null;
        }

        GrayscaleShader.dispose();

        GameAssetManager.getGameAssetManager().dispose();

        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
