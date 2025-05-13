package com.example.Controllers;

import com.example.Models.PreGame;
import com.example.Views.PreGameMenuView;

public class PreGameMenuController {

    private PreGameMenuView view;
    private PreGame preGame;

    public void setView(PreGameMenuView view) {
        this.view = view;
        this.preGame = new PreGame();
    }

    public void handlePreGameMenuButtons() {
        if (view != null) {

        }
    }
}
