package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.LoginMenuView;
import com.example.Views.OpeningMenuView;
import com.example.Views.PreGameMenuView;
import com.example.Views.SignupMenuView;

public class OpeningMenuController {
    private OpeningMenuView view;
    public void setView(OpeningMenuView view) {
        this.view = view;
    }

    public void handleOpeningMenu(){
        if(view!= null){
            if(view.getSignupButton().isChecked()){
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new SignupMenuView(new SignupMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if(view.getLoginButton().isChecked()){
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new LoginMenuView(new LoginMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if(view.getExitButton().isChecked()){
                Gdx.app.exit();
            }
        }
    }
}
