package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.TimeUtils;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.Player;
import com.example.Models.Weapon;
import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;
import com.example.Models.utilities.Game;
import com.example.Views.GameView;

public class GameController {
    private GameView view;
    private long chosenTime;
    private WeaponType weaponType;
    private Hero hero;
    private PlayerController playerController;
    private WorldController worldController;
    private WeaponController weaponController;
    private EnemyController enemyController;
    private Game game;

    public GameController(Hero hero, WeaponType weaponType, long timeInSec) {
        this.chosenTime = timeInSec;
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
        this.game = new Game(TimeUtils.millis(), chosenTime);
        playerController = new PlayerController(new Player(hero));

        playerController.getPlayer().setLightEnabled(App.getSettings().isLightHaloEnabled());

        worldController = new WorldController(playerController);
        weaponController = new WeaponController(new Weapon(weaponType));
        enemyController = new EnemyController(playerController);
        playerController.setEnemyController(enemyController);
        enemyController.setWeaponController(weaponController);
        enemyController.setGameController(this);
        weaponController.getWeapon().setWeaponType(weaponType);
        enemyController.tentacleSpawn();
        enemyController.eyeBatSpawn();
//        enemyController.eyeBatHit();
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

    public Game getGame() {
        return game;
    }

    public long getChosenTime() {
        return chosenTime;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public Hero getHero() {
        return hero;
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
