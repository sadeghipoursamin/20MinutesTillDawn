package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Controllers.GameController;
import com.example.Main;
import com.example.Models.Player;

public class GameCompletionWindow extends Window {
    private GameController gameController;
    private Player player;
    private Label titleLabel;
    private Label survivedLabel;
    private Label statsLabel;
    private TextButton mainMenuButton;
    private TextButton playAgainButton;
    private Runnable onMainMenu;
    private Runnable onPlayAgain;

    public GameCompletionWindow(Skin skin, GameController gameController, boolean timeExpired) {
        super(timeExpired ? "Time's Up!" : "Game Over", skin);

        this.gameController = gameController;
        this.player = gameController.getPlayerController().getPlayer();

        initializeUI(skin, timeExpired);
        setupListeners();

        this.setSize(600, 400);
        this.setPosition(
            (Gdx.graphics.getWidth() - 600) / 2f,
            (Gdx.graphics.getHeight() - 400) / 2f
        );
        this.setModal(true);
        this.setMovable(false);
    }

    private void initializeUI(Skin skin, boolean timeExpired) {
        if (timeExpired) {
            titleLabel = new Label("CONGRATULATIONS!", skin, "title");
            titleLabel.setColor(Color.GREEN);
            survivedLabel = new Label("You survived the full duration!", skin);
            survivedLabel.setColor(Color.YELLOW);
        } else {
            titleLabel = new Label("GAME OVER", skin, "title");
            titleLabel.setColor(Color.RED);
            survivedLabel = new Label("You have fallen...", skin);
            survivedLabel.setColor(Color.GRAY);
        }

        long gameDuration = gameController.getChosenTime() - gameController.getTimeRemaining();
        int minutes = (int) (gameDuration / 60);
        int seconds = (int) (gameDuration % 60);

        String statsText = String.format(
            "Time Survived: %02d:%02d\nLevel Reached: %d\nEnemies Killed: %d\nFinal Health: %.1f",
            minutes, seconds,
            player.getLevel(),
            player.getKillCount(),
            player.getPlayerHealth()
        );

        statsLabel = new Label(statsText, skin);
        statsLabel.setColor(Color.WHITE);

        mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.setColor(Color.CYAN);

        playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.setColor(Color.GREEN);

        // Layout
        this.add(titleLabel).center().padBottom(20);
        this.row();
        this.add(survivedLabel).center().padBottom(20);
        this.row();
        this.add(statsLabel).center().padBottom(30);
        this.row();

        Table buttonTable = new Table();
        buttonTable.add(mainMenuButton).width(150).height(60).pad(10);
        buttonTable.add(playAgainButton).width(150).height(60).pad(10);

        this.add(buttonTable).center();
    }

    private void setupListeners() {
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                if (onMainMenu != null) {
                    onMainMenu.run();
                }
                remove();
            }
        });

        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                if (onPlayAgain != null) {
                    onPlayAgain.run();
                }
                remove();
            }
        });
    }

    public void setOnMainMenu(Runnable onMainMenu) {
        this.onMainMenu = onMainMenu;
    }

    public void setOnPlayAgain(Runnable onPlayAgain) {
        this.onPlayAgain = onPlayAgain;
    }
}
