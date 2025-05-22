package com.example.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.example.Models.Player;


public class UpdatePlayerWindow extends Window {
    UpdatePlayerWindow(Skin skin, Player player) {
        super("Choose Ability!", skin);
    }

}
