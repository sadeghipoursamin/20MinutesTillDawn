package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GameLoader;
import com.example.Models.utilities.GameSaveSystem;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.*;

import java.util.List;

public class MainMenuController {
    private MainMenuView view;

    public void setView(MainMenuView view) {
        this.view = view;

        if (App.getSettings().isGrayscaleEnabled()) {
            Main.getBatch().setShader(GrayscaleShader.getShader());
        } else {
            Main.getBatch().setShader(null);
        }

        // Refresh player info when view is set
        if (view != null) {
            view.refreshPlayerInfo();
        }
    }

    public void handleMainMenuButtons() {
        if (view != null) {
            if (view.getPlayButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new PreGameMenuView(new PreGameMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getProfileButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                ProfileMenuView profileView = new ProfileMenuView(new ProfileMenuController(), GameAssetManager.getGameAssetManager().getSkin());
                Main.getMain().setScreen(profileView);
            }

            if (view.getSettingsButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new SettingsMenuView(new SettingsMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getHintButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new HintMenuView(new HintMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getScoreboardButton().isChecked()) {
                Main.playSound();
                Main.getMain().getScreen().dispose();
                ScoreboardMenuController scoreboardController = new ScoreboardMenuController();
                Main.getMain().setScreen(new ScoreboardMenuView(scoreboardController, GameAssetManager.getGameAssetManager().getSkin()));
            }

            if (view.getLoguotButton().isChecked()) {
                Main.playSound();
                App.logout();
                OpeningMenuView openingMenuView = new OpeningMenuView(new OpeningMenuController(), GameAssetManager.getGameAssetManager().getSkin());
                Main.getMain().setScreen(openingMenuView);
            }

            if (view.getContinueGameButton().isChecked()) {
                Main.playSound();
                handleContinueGame();
            }
        }
    }

    private void handleContinueGame() {
        try {
            // Get the most recent save file
            List<String> saves = GameSaveSystem.getAvailableSaves();

            if (saves.isEmpty()) {
                showError("No saved games found!");
                return;
            }

            // Find the most recent save
            String mostRecentSave = null;
            long mostRecentTime = 0;

            for (String saveName : saves) {
                GameSaveSystem.SaveFileInfo info = GameSaveSystem.getSaveInfo(saveName);
                if (info != null && info.timestamp > mostRecentTime) {
                    mostRecentTime = info.timestamp;
                    mostRecentSave = saveName;
                }
            }

            if (mostRecentSave != null) {
                loadGame(mostRecentSave);
            } else {
                showError("No valid save files found!");
            }

        } catch (Exception e) {
            System.err.println("Error continuing game: " + e.getMessage());
            showError("Error loading saved game!");
        }
    }

    private void showLoadGameMenu() {
        try {
            SaveLoadWindow loadWindow = new SaveLoadWindow(
                GameAssetManager.getGameAssetManager().getSkin(),
                SaveLoadWindow.Mode.LOAD,
                null, // No game controller needed for loading
                (saveName) -> {
                    loadGame(saveName);
                }
            );

            Stage tempStage = new Stage(new com.badlogic.gdx.utils.viewport.ScreenViewport());
            tempStage.addActor(loadWindow);

            com.badlogic.gdx.InputProcessor oldProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(tempStage);

        } catch (Exception e) {
            System.err.println("Error showing load menu: " + e.getMessage());
            showError("Error opening load menu!");
        }
    }

    private void loadGame(String saveName) {
        try {
            // Validate the save file
            if (!GameLoader.isValidSaveFile(saveName)) {
                showError("Invalid or corrupted save file!");
                return;
            }

            // Load the game controller from save
            GameController gameController = GameLoader.loadGameFromSave(saveName);

            if (gameController == null) {
                showError("Failed to load save file!");
                return;
            }

            // Dispose current screen
            Main.getMain().getScreen().dispose();

            // Create the game view
            GameView gameView = new GameView(gameController, GameAssetManager.getGameAssetManager().getSkin());
            Main.getMain().setScreen(gameView);

            boolean restored = GameLoader.restoreGameState(gameController, saveName);

            if (!restored) {
                System.err.println("Warning: Game state restoration incomplete");
            }

            System.out.println("Successfully loaded save: " + saveName);

        } catch (Exception e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading saved game: " + e.getMessage());
        }
    }

    private void showError(String message) {
        System.err.println("MainMenu Error: " + message);

        if (view != null && view.getErrorLabel() != null) {
            view.getErrorLabel().setText(message);
            view.getErrorLabel().setColor(com.badlogic.gdx.graphics.Color.RED);

            // Clear error after 5 seconds
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    if (view != null && view.getErrorLabel() != null) {
                        view.getErrorLabel().setText("");
                    }
                }
            }, 5.0f);
        }
    }

    public void refreshView() {
        if (view != null) {
            view.refreshPlayerInfo();
        }
    }
}
