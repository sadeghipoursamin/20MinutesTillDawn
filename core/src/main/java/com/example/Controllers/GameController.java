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
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.GameCompletionWindow;
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
        if (isGameTimeExpired() && !enemyController.isGamePaused()) {
            endGameDueToTime();
            return;
        }

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
        enemyController.elderSpawn();
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

    public long getTimeRemaining() {
        if (game == null) {
            return 0;
        }

        long currentTime = TimeUtils.millis();
        long elapsedTime = (currentTime - game.getStartTime()) / 1000; // Convert to seconds
        long timeRemaining = chosenTime - elapsedTime;

        return Math.max(0, timeRemaining);
    }

    public boolean isGameTimeExpired() {
        return getTimeRemaining() <= 0;
    }

    private void endGameDueToTime() {
        enemyController.pauseGame();

        // Update user stats before showing completion window
        updateUserStatsOnGameEnd(true);

        GameCompletionWindow completionWindow = new GameCompletionWindow(
            GameAssetManager.getGameAssetManager().getSkin(),
            this,
            false
        );
    }

    // Method to handle game end (called from EnemyController when player dies)
    public void endGameDueToDeath() {
        enemyController.pauseGame();

        // Update user stats before showing completion window
        updateUserStatsOnGameEnd(false);
    }

    private void updateUserStatsOnGameEnd(boolean completedSuccessfully) {
        if (App.getCurrentUser() != null && playerController != null) {
            Player player = playerController.getPlayer();

            // Calculate survival time
            long currentTime = TimeUtils.millis();
            long survivalTimeMs = currentTime - game.getStartTime();
            long survivalTimeSeconds = survivalTimeMs / 1000;

            // Get player stats
            int kills = player.getKillCount();
            int level = player.getLevel();

            // Calculate final score based on performance
            int baseScore = completedSuccessfully ? 1000 : 0; // Bonus for completing
            int killScore = kills * 50; // 50 points per kill
            int survivalScore = (int) (survivalTimeSeconds * 5); // 5 points per second
            int levelScore = level * 100; // 100 points per level

            int finalScore = baseScore + killScore + survivalScore + levelScore;

            // Update user stats using ScoreboardMenuController
            ScoreboardMenuController.updateUserStats(
                App.getCurrentUser(),
                kills,
                survivalTimeSeconds,
                finalScore
            );
        }
    }
}
