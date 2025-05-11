package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.*;

public class ProfileMenuController {
    private ProfileMenuView view;
    public void setView(ProfileMenuView view){
        this.view = view;
        handleProfileMenuButtons();
    }

    public void handleProfileMenuButtons(){
        view.getPassword().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ResetPasswordWindow resetPasswordWindow = new ResetPasswordWindow(GameAssetManager.getGameAssetManager().getSkin(), App.getCurrentUser());
                resetPasswordWindow.setOnComplete(() -> {
                    navigateToMainMenu();
                    App.getCurrentUser().setPassword(resetPasswordWindow.getNewPassword().getText());
                });
                view.getStage().addActor(resetPasswordWindow);
            }
        });

        view.getUsernameButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ResetUsernameWindow resetUsernameWindow = new ResetUsernameWindow(GameAssetManager.getGameAssetManager().getSkin(), App.getCurrentUser());
                resetUsernameWindow.setOnComplete(() -> {
                    navigateToMainMenu();
                    App.getCurrentUser().setUsername(resetUsernameWindow.getNewUsername().getText());
                });
                view.getStage().addActor(resetUsernameWindow);
            }
        });
    }

    public void navigateToMainMenu() {
        Main.getMain().getScreen().dispose();
        MainMenuView mainMenuView = new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin());
        Main.getMain().setScreen(mainMenuView);
        mainMenuView.getErrorLabel().setText("Reset Password successfully!");
    }
}
