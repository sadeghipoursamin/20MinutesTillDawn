package com.example.Controllers;

import com.example.Main;
import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;
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
                String heroName = view.getSelectHero().getSelected();
                Hero hero = Hero.getHeroByName(heroName);
                String weaponString = view.getSelectWeapon().getSelected();
                WeaponType weaponType = WeaponType.getWeaponTypeByName(weaponString);
                String time = view.getSelectTime().getSelected();
                int timeInSec = Integer.parseInt(time);
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new GameView(new GameController(hero, weaponType, timeInSec), GameAssetManager.getGameAssetManager().getSkin()));
            }
        }
    }
}
