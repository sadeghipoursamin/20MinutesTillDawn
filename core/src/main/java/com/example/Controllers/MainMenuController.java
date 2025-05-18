package com.example.Controllers;

import com.example.Main;
import com.example.Models.App;
import com.example.Models.PreGame;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.MainMenuView;
import com.example.Views.PreGameMenuView;
import com.example.Views.ProfileMenuView;
import com.example.Views.SettingsMenuView;

public class MainMenuController {
    private MainMenuView view;
    private PreGame preGame;

    public void setView(MainMenuView view) {
        this.view = view;
        this.preGame = new PreGame();

        if (App.getSettings().isGrayscaleEnabled()) {
            Main.getBatch().setShader(GrayscaleShader.getShader());
        } else {
            Main.getBatch().setShader(null);
        }
    }


    public void handleMainMenuButtons() {
        if (view != null) {
            if (view.getPlayButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new PreGameMenuView(new PreGameMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getProfileButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new ProfileMenuView(new ProfileMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getSettingsButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new SettingsMenuView(new SettingsMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }
        }
    }
}
