package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.example.Main;
import com.example.Models.Player;
import com.example.Models.Weapon;
import com.example.Views.GameView;

public class GameController {
    private GameView view;
    private PlayerController playerController;
    private WorldController worldController;
    private WeaponController weaponController;
    private EnemyController enemyController;

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
        playerController = new PlayerController(new Player());
        enemyController = new EnemyController(playerController);
        worldController = new WorldController(playerController);
        weaponController = new WeaponController(new Weapon());

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
