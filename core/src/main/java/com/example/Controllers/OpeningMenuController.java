package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.LoginMenuView;
import com.example.Views.OpeningMenuView;
import com.example.Views.SignupMenuView;

public class OpeningMenuController {
    private OpeningMenuView view;

    public void setView(OpeningMenuView view) {
        this.view = view;
    }

    public void handleOpeningMenu() {
        if (view != null) {
            if (view.getSignupButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new SignupMenuView(new SignupMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getLoginButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new LoginMenuView(new LoginMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getExitButton().isChecked()) {
                Main.playSound();
                Gdx.app.exit();
            }

            if (view.getChangeLanguageButton().isChecked()) {
                Main.playSound();
                App.changeLanguage();
                Main.getMain().setScreen(new OpeningMenuView(new OpeningMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }
        }
    }
}
