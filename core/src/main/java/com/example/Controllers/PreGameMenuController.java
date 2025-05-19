package com.example.Controllers;

import com.example.Main;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.GameView;
import com.example.Views.PreGameMenuView;

public class PreGameMenuController {

    private PreGameMenuView view;

    public void setView(PreGameMenuView view) {
        this.view = view;
    }

    public void handlePreGameMenuButtons() {
        if (view != null) {
            if (view.getPlayButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new GameView(new GameController(), GameAssetManager.getGameAssetManager().getSkin()));
            }
        }
    }
}
