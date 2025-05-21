package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.example.Main;
import com.example.Models.Player;
import com.example.Models.Weapon;
import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;
import com.example.Views.GameView;

public class GameController {
    private GameView view;
    private int time;
    private WeaponType weaponType;
    private Hero hero;
    private PlayerController playerController;
    private WorldController worldController;
    private WeaponController weaponController;
    private EnemyController enemyController;

    public GameController(Hero hero, WeaponType weaponType, int timeInSec) {
        this.time = timeInSec;
        this.hero = hero;
        this.weaponType = weaponType;
    }

    public void updateGame() {
        worldController.update();
        playerController.update();
        weaponController.update();
        enemyController.update(Gdx.graphics.getDeltaTime());
        enemyController.render(Main.getBatch());

        OrthographicCamera camera = view.getCamera();
        Main.getBatch().setProjectionMatrix(camera.combined);

    }

    public GameView getView() {
        return view;
    }

    public void setView(GameView view) {
        this.view = view;
        playerController = new PlayerController(new Player(hero));
        enemyController = new EnemyController(playerController);
        worldController = new WorldController(playerController);
        weaponController = new WeaponController(new Weapon(weaponType));
        weaponController.getWeapon().setWeaponType(weaponType);
        enemyController.handleBulletCollisions(weaponController.getBullets());
//        enemyController.updateEnemies();

        weaponController.setPlayerController(playerController);
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public WorldController getWorldController() {
        return worldController;
    }

    public WeaponController getWeaponController() {
        return weaponController;
    }


}
