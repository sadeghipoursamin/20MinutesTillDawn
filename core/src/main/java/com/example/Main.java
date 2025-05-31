package com.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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

        // Load app data (this will now use both JSON and database)
        App.load();

        // Create custom cursor
        Pixmap original = new Pixmap(Gdx.files.internal("MapDetails/HitMarker.png"));
        Pixmap scaled = new Pixmap(32, 32, original.getFormat());
        scaled.drawPixmap(original,
            0, 0, original.getWidth(), original.getHeight(),
            0, 0, scaled.getWidth(), scaled.getHeight());

        Cursor cursor = Gdx.graphics.newCursor(scaled, 0, 0);
        Gdx.graphics.setCursor(cursor);
        original.dispose();
        scaled.dispose();

        // Load click sound
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/click.wav"));

        // Initialize grayscale shader if needed
        if (App.getSettings().isGrayscaleEnabled()) {
            GrayscaleShader.initialize();
            batch.setShader(GrayscaleShader.getShader());
        }

        // Start with opening menu
        setScreen(new OpeningMenuView(new OpeningMenuController(), GameAssetManager.getGameAssetManager().getSkin()));

        System.out.println("Game created and initialized successfully");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void dispose() {
        try {
            // Save app data to both JSON and database before closing
            App.shutdown();

            if (batch != null) {
                batch.dispose();
                batch = null;
            }

            if (clickSound != null) {
                clickSound.dispose();
                clickSound = null;
            }

            // Dispose shader
            GrayscaleShader.dispose();

            // Dispose asset manager
            GameAssetManager.getGameAssetManager().dispose();

            // Dispose current screen
            if (getScreen() != null) {
                getScreen().dispose();
            }

            System.out.println("Game disposed successfully");
        } catch (Exception e) {
            System.err.println("Error during game disposal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        super.pause();
        // Save data when game is paused (mobile devices)
        App.save();
        App.saveSettings();
    }

    @Override
    public void resume() {
        super.resume();
        // Reload data when game is resumed (mobile devices)
        App.load();
    }
}
