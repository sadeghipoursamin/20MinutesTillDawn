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
    private float timeSurvived = 0f;
    private boolean isLoadedGame = false;

    public GameController(Hero hero, WeaponType weaponType, long timeInSec) {
        this.chosenTime = timeInSec;
        this.hero = hero;
        this.weaponType = weaponType;
    }

    public void updateGame() {
        if (!isLoadedGame && isGameTimeExpired() && !enemyController.isGamePaused()) {
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

    public float getTimeRemaining() {
        if (game == null) {
            return 0;
        }


        return chosenTime * 60 - timeSurvived;
    }

    public boolean isGameTimeExpired() {
        return getTimeRemaining() <= 0;
    }

    private void endGameDueToTime() {
        enemyController.pauseGame();

        updateUserStatsOnGameEnd(true);

        GameCompletionWindow completionWindow = new GameCompletionWindow(
            GameAssetManager.getGameAssetManager().getSkin(),
            this,
            false
        );
    }

    public void endGameDueToDeath() {
        enemyController.pauseGame();

        updateUserStatsOnGameEnd(false);
    }

    private void updateUserStatsOnGameEnd(boolean completedSuccessfully) {
        if (App.getCurrentUser() != null && playerController != null) {
            Player player = playerController.getPlayer();

            long currentTime = TimeUtils.millis();
            long survivalTimeMs = currentTime - game.getStartTime();
            long survivalTimeSeconds = survivalTimeMs / 1000;

            int kills = player.getKillCount();
            int level = player.getLevel();

            int baseScore = completedSuccessfully ? 1000 : 0; // Bonus for completing
            int killScore = kills * 50; // 50 points per kill
            int survivalScore = (int) (survivalTimeSeconds * 5); // 5 points per second
            int levelScore = level * 100; // 100 points per level

            int finalScore = baseScore + killScore + survivalScore + levelScore;

            ScoreboardMenuController.updateUserStats(
                App.getCurrentUser(),
                kills,
                survivalTimeSeconds,
                finalScore
            );
        }
    }

    public boolean isLoadedGame() {
        return isLoadedGame;
    }

    public void setLoadedGame(boolean isLoadedGame) {
        this.isLoadedGame = isLoadedGame;
    }

    public void update(float delta) {
        timeSurvived += delta;
    }

    public float getTimeSurvived() {
        return timeSurvived;
    }

    public void setTimeSurvived(float timeSurvived) {
        this.timeSurvived = timeSurvived;
    }


    public void initializeLoadedGame(float savedTimeSurvived) {
        this.timeSurvived = savedTimeSurvived;
        this.isLoadedGame = true;

        // Adjust the game start time to account for already survived time
        long currentTime = TimeUtils.millis();
        long adjustedStartTime = currentTime - (long) (savedTimeSurvived * 1000);
        this.game.setStartTime(adjustedStartTime);

        System.out.println("Loaded game initialized with " + savedTimeSurvived + " seconds survived");
    }

    public void saveCurrentGameState() {
        if (enemyController != null) {
            enemyController.stopAllSpawnTimers();
        }
    }


}
