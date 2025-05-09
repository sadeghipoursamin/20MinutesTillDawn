package com.example.Controllers;

import com.example.Main;
import com.example.Models.GameAssetManager;
import com.example.Views.PreGameMenuView;
import com.example.Views.SignupMenuView;

public class SignupMenuController {

    private SignupMenuView view;
    public void setView(SignupMenuView view) {
        this.view = view;
    }

    public void handleSignupMenuButtons() {
        if(view!= null){
            return;
        }
    }
}
