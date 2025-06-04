package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.example.Controllers.EnemyController;
import com.example.Controllers.GameController;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.Player;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;

import java.util.ArrayList;
import java.util.List;

public class PauseMenuWindow extends Window {
    private GameController gameController;
    private EnemyController enemyController;
    private Player player;

    private TextButton resumeButton;
    private TextButton cheatCodesButton;
    private TextButton abilitiesButton;
    private TextButton giveUpButton;
    private TextButton grayscaleButton;
    private TextButton saveAndExitButton;

    private Label titleLabel;
    private Table mainTable;
    private ScrollPane scrollPane;

    private Table cheatCodesTable;
    private boolean showingCheatCodes = false;

    private Table abilitiesTable;
    private boolean showingAbilities = false;

    private List<String> acquiredAbilities;

    private Runnable onResume;
    private Runnable onGiveUp;
    private Runnable onSaveAndExit;

    public PauseMenuWindow(Skin skin, GameController gameController) {
        super("Game Paused", skin);

        this.gameController = gameController;
        this.enemyController = gameController.getEnemyController();
        this.player = gameController.getPlayerController().getPlayer();
        this.acquiredAbilities = new ArrayList<>();

        initializeUI(skin);
        setupListeners();

        this.setSize(1000, 500);
        this.setPosition(
            (Gdx.graphics.getWidth() - 1000) / 2f,
            (Gdx.graphics.getHeight() - 500) / 2f
        );
        this.setModal(true);
        this.setMovable(false);
    }

    private void initializeUI(Skin skin) {
        // Title
        titleLabel = new Label("GAME PAUSED", skin, "title");
        titleLabel.setColor(Color.YELLOW);

        // Buttons
        resumeButton = new TextButton("Resume Game", skin);
        resumeButton.setColor(Color.GREEN);

        cheatCodesButton = new TextButton("Show Cheat Codes", skin);
        cheatCodesButton.setColor(Color.CYAN);

        abilitiesButton = new TextButton("Show Abilities", skin);
        abilitiesButton.setColor(Color.MAGENTA);

        giveUpButton = new TextButton("Give Up & Exit", skin);
        giveUpButton.setColor(Color.RED);

        grayscaleButton = new TextButton(
            App.getSettings().isGrayscaleEnabled() ? "Disable Grayscale" : "Enable Grayscale",
            skin
        );
        grayscaleButton.setColor(Color.GRAY);

        saveAndExitButton = new TextButton("Save & Exit", skin);
        saveAndExitButton.setColor(Color.ORANGE);

        mainTable = new Table();
        setupMainLayout();

        cheatCodesTable = new Table();
        setupCheatCodesTable(skin);

        abilitiesTable = new Table();
        setupAbilitiesTable(skin);

        this.add(mainTable).expand().fill();
    }

    private void setupMainLayout() {
        mainTable.add(titleLabel).colspan(2).center().padBottom(30);
        mainTable.row();

        mainTable.add(resumeButton).width(250).height(60).pad(10);
        mainTable.add(cheatCodesButton).width(250).height(60).pad(10);
        mainTable.row();

        mainTable.add(abilitiesButton).width(250).height(60).pad(10);
        mainTable.add(giveUpButton).width(250).height(60).pad(10);
        mainTable.row();

        mainTable.add(grayscaleButton).width(250).height(60).pad(10);
        mainTable.add(saveAndExitButton).width(250).height(60).pad(10);
        mainTable.row();

        // Player stats
        Label statsLabel = new Label("Player Stats:", GameAssetManager.getGameAssetManager().getSkin());
        statsLabel.setColor(Color.WHITE);
        mainTable.add(statsLabel).colspan(2).center().padTop(20);
        mainTable.row();

        String statsText = String.format(
            "Level: %d | Health: %.1f | Kills: %d | XP: %d",
            player.getLevel(),
            player.getPlayerHealth(),
            player.getKillCount(),
            player.getXp()
        );
        Label statsValueLabel = new Label(statsText, GameAssetManager.getGameAssetManager().getSkin());
        statsValueLabel.setColor(Color.LIGHT_GRAY);
        mainTable.add(statsValueLabel).colspan(2).center().pad(5);
    }

    private void setupCheatCodesTable(Skin skin) {
        Label cheatTitle = new Label("Cheat Codes", skin, "title");
        cheatTitle.setColor(Color.CYAN);
        cheatCodesTable.add(cheatTitle).center().padBottom(20);
        cheatCodesTable.row();

        String[] cheatCodes = {
            "GODMODE - Infinite Health",
            "AMMO - Infinite Ammo",
            "SPEED - Double Speed",
            "KILLALL - Kill All Enemies",
            "LEVELUP - Gain Level",
            "AUTOAIM - Toggle Auto Aim (SPACE)",
            "NOCLIP - Walk Through Walls",
            "SHOWFPS - Display FPS Counter"
        };

        for (String cheat : cheatCodes) {
            Label cheatLabel = new Label(cheat, skin);
            cheatLabel.setColor(Color.WHITE);
            cheatCodesTable.add(cheatLabel).left().pad(5);
            cheatCodesTable.row();
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                hideCheatCodes();
            }
        });
        cheatCodesTable.add(backButton).center().padTop(20);
    }

    private void setupAbilitiesTable(Skin skin) {
        Label abilitiesTitle = new Label("Acquired Abilities", skin, "title");
        abilitiesTitle.setColor(Color.MAGENTA);
        abilitiesTable.add(abilitiesTitle).center().padBottom(20);
        abilitiesTable.row();

        updateAbilitiesDisplay(skin);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                hideAbilities();
            }
        });
        abilitiesTable.add(backButton).center().padTop(20);
    }

    private void updateAbilitiesDisplay(Skin skin) {

        if (acquiredAbilities.isEmpty()) {
            Label noAbilitiesLabel = new Label("No abilities acquired yet", skin);
            noAbilitiesLabel.setColor(Color.GRAY);
            abilitiesTable.add(noAbilitiesLabel).center().pad(10);
            abilitiesTable.row();
        } else {
            for (String ability : acquiredAbilities) {
                Label abilityLabel = new Label("• " + ability, skin);
                abilityLabel.setColor(Color.WHITE);
                abilitiesTable.add(abilityLabel).left().pad(5);
                abilitiesTable.row();
            }
        }

        Label statsLabel = new Label("Current Bonuses:", skin);
        statsLabel.setColor(Color.YELLOW);
        abilitiesTable.add(statsLabel).left().padTop(10);
        abilitiesTable.row();

        if (player.hasDamageBoost()) {
            Label damageLabel = new Label("• Damage Boost: +" +
                Math.round((player.getDamageMultiplier() - 1) * 100) + "%", skin);
            damageLabel.setColor(Color.GREEN);
            abilitiesTable.add(damageLabel).left().pad(2);
            abilitiesTable.row();
        }

        Label speedLabel = new Label("• Speed: " + player.getSpeed(), skin);
        speedLabel.setColor(Color.GREEN);
        abilitiesTable.add(speedLabel).left().pad(2);
        abilitiesTable.row();
    }

    private void setupListeners() {
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                if (onResume != null) {
                    onResume.run();
                }
                remove();
            }
        });

        cheatCodesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                showCheatCodes();
            }
        });

        abilitiesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                showAbilities();
            }
        });

        giveUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                if (onGiveUp != null) {
                    onGiveUp.run();
                }
                remove();

            }
        });

        grayscaleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                toggleGrayscale();
            }
        });

        saveAndExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                showSaveMenu();
            }
        });

    }

    private void showCheatCodes() {
        this.clear();
        this.add(cheatCodesTable).expand().fill();
        showingCheatCodes = true;
    }

    private void hideCheatCodes() {
        this.clear();
        this.add(mainTable).expand().fill();
        showingCheatCodes = false;
    }

    private void showAbilities() {
        abilitiesTable.clear();
        setupAbilitiesTable(GameAssetManager.getGameAssetManager().getSkin());

        this.clear();
        this.add(abilitiesTable).expand().fill();
        showingAbilities = true;
    }

    private void hideAbilities() {
        this.clear();
        this.add(mainTable).expand().fill();
        showingAbilities = false;
    }

    private void toggleGrayscale() {
        boolean currentState = App.getSettings().isGrayscaleEnabled();
        App.getSettings().setGrayscaleEnabled(!currentState);

        if (!currentState) {
            Main.getBatch().setShader(GrayscaleShader.getShader());
            grayscaleButton.setText("Disable Grayscale");
        } else {
            Main.getBatch().setShader(null);
            grayscaleButton.setText("Enable Grayscale");
        }
    }

    public void addAcquiredAbility(String abilityName) {
        if (!acquiredAbilities.contains(abilityName)) {
            acquiredAbilities.add(abilityName);
        }
    }

    public void setOnResume(Runnable onResume) {
        this.onResume = onResume;
    }

    public void setOnQuit(Runnable onQuit) {
        this.onGiveUp = onQuit;
    }

    public void setOnGiveUp(Runnable onGiveUp) {
        this.onGiveUp = onGiveUp;
    }

    public void setOnSaveAndExit(Runnable onSaveAndExit) {
        this.onSaveAndExit = onSaveAndExit;
    }

    private void showSaveMenu() {
        SaveLoadWindow saveWindow = new SaveLoadWindow(
            GameAssetManager.getGameAssetManager().getSkin(),
            SaveLoadWindow.Mode.SAVE,
            gameController,
            (saveName) -> {
                showSuccess("Game saved as: " + saveName);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        enemyController.navigateToMainMenu();
                    }
                }, 2.0f);
            }
        );

        this.getStage().addActor(saveWindow);
    }

    private void showSuccess(String message) {
        Label successLabel = new Label("✅ " + message,
            GameAssetManager.getGameAssetManager().getSkin());
        successLabel.setColor(Color.GREEN);
        successLabel.setPosition(
            (Gdx.graphics.getWidth() - 400) / 2f,
            Gdx.graphics.getHeight() - 100
        );

        this.getStage().addActor(successLabel);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                successLabel.remove();
            }
        }, 3.0f);
    }
}
