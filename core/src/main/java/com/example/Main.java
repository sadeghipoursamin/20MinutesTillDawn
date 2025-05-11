package com.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.Controllers.OpeningMenuController;
import com.example.Controllers.SignupMenuController;
import com.example.Models.App;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.OpeningMenuView;
import com.example.Views.SignupMenuView;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private static Main main;
    private static SpriteBatch batch;


    @Override
    public void create() {
        App.load();
        main = this;
        batch = new SpriteBatch();
        main.setScreen(new OpeningMenuView(new OpeningMenuController() , GameAssetManager.getGameAssetManager().getSkin()));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public static Main getMain() {
        return main;
    }

    public static void setMain(Main main) {
        Main.main = main;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }
}
