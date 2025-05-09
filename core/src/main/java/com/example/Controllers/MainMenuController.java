package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.Main;
import com.example.Models.GameAssetManager;
import com.example.Views.MainMenuView;

public class MainMenuController {
    private MainMenuView view;
    public void setView(MainMenuView view) {
        this.view = view;
    }


    public void handleMainMenuButtons() {
        if(view!= null){
            if(view.getPlayButton().isChecked() && view.getField().getText().equals("samin")){
                Main.getMain().getScreen().dispose();
//                Main.getMain().setScreen(new PreGameMenuView(new PreGameMenuController(),GameAssetManager.getGameAssetManager().getSkin()));
            }
        }
    }
}
