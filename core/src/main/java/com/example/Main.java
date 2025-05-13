package com.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.Controllers.OpeningMenuController;
import com.example.Models.App;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.OpeningMenuView;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;
    private static Sound clickSound;
    Texture openingMenuTexture;
    Texture signUpBackground;
    Texture mainBackGround;

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
        // TODO: check sfx setting
        clickSound.play();
    }

    @Override
    public void create() {
        openingMenuTexture = new Texture("opening.png");
        signUpBackground = new Texture("backGround.png");
        mainBackGround = new Texture("mainBackGround.png");
        main = this;
        batch = new SpriteBatch();
        App.load();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/click.wav"));

        main.setScreen(new OpeningMenuView(new OpeningMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        App.save();
        batch.dispose();
    }
}
