package com.example.Controllers;

import com.example.Main;
import com.example.Models.App;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.HintMenuView;
import com.example.Views.MainMenuView;

public class HintMenuController {
    private HintMenuView hintMenuView;

    public void setView(HintMenuView hintMenuView) {
        this.hintMenuView = hintMenuView;

        if (App.getSettings().isGrayscaleEnabled()) {
            Main.getBatch().setShader(GrayscaleShader.getShader());
        } else {
            Main.getBatch().setShader(null);
        }
    }

    public void handleHintMenuButtons() {
        if (hintMenuView != null) {
            if (hintMenuView.getBackButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }
        }
    }
}
