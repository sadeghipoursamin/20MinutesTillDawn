package com.example.Controllers;

import com.example.Main;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.PreGame;
import com.example.Views.MainMenuView;
import com.example.Views.PreGameMenuView;
import com.example.Views.ProfileMenuView;

public class MainMenuController {
    private MainMenuView view;
    private PreGame preGame;
    public void setView(MainMenuView view) {
        this.view = view;
        this.preGame = new PreGame();
    }


    public void handleMainMenuButtons() {
        if(view!= null){
            if(view.getPlayButton().isChecked()){
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new PreGameMenuView(new PreGameMenuController(),GameAssetManager.getGameAssetManager().getSkin()));
            }

            if(view.getProfileButton().isChecked()){
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new ProfileMenuView(new ProfileMenuController(),GameAssetManager.getGameAssetManager().getSkin()));
            }

            if(view.getPreGameButton().isChecked()){
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new PreGameMenuView(new PreGameMenuController(),GameAssetManager.getGameAssetManager().getSkin()));
            }
        }
    }
}
