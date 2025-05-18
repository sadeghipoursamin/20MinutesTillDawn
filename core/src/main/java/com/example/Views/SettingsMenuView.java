package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.SettingsMenuController;
import com.example.Main;
import com.example.Models.utilities.GameAssetManager;

import java.util.function.Consumer;

public class SettingsMenuView implements Screen {
    private final SettingsMenuController controller;

    // UI elements
    private Stage stage;
    private Table table;
    private Image backgroundImage;
    private Texture backgroundTexture;

    // Music Settings
    private Label musicVolumeLabel;
    private Slider musicVolumeSlider;
    private Label musicSelectLabel;
    private SelectBox<String> musicSelectBox;

    // SFX Settings
    private Label sfxLabel;
    private CheckBox sfxToggle;

    // Keyboard Controls
    private Label keyBindingsLabel;
    private Label moveUpLabel;
    private Label moveUpKeyLabel;
    private TextButton moveUpButton;
    private Label moveDownLabel;
    private Label moveDownKeyLabel;
    private TextButton moveDownButton;
    private Label moveLeftLabel;
    private Label moveLeftKeyLabel;
    private TextButton moveLeftButton;
    private Label moveRightLabel;
    private Label moveRightKeyLabel;
    private TextButton moveRightButton;
    private Label shootLabel;
    private Label shootKeyLabel;
    private TextButton shootButton;
    private Label reloadLabel;
    private Label reloadKeyLabel;
    private TextButton reloadButton;

    // Game Settings
    private Label gameSettingsLabel;
    private Label autoReloadLabel;
    private CheckBox autoReloadToggle;
    private Label grayscaleLabel;
    private CheckBox grayscaleToggle;

    // Navigation
    private TextButton backButton;

    public SettingsMenuView(SettingsMenuController controller, Skin skin) {
        this.controller = controller;

        // Initialize UI elements

        // Music Settings
        this.musicVolumeLabel = new Label("Music Volume:", skin);
        this.musicVolumeSlider = new Slider(0, 1, 0.01f, false, skin);
        this.musicSelectLabel = new Label("Background Music:", skin);
        this.musicSelectBox = new SelectBox<>(skin);

        // SFX Settings
        this.sfxLabel = new Label("Sound Effects:", skin);
        this.sfxToggle = new CheckBox("Enabled", skin);

        // Keyboard Controls
//        this.keyBindingsLabel = new Label("Keyboard Controls", skin, "title");
        this.moveUpLabel = new Label("Up:", skin);
        this.moveUpKeyLabel = new Label("", skin);
        this.moveUpButton = new TextButton("W", skin);
        this.moveDownLabel = new Label("Down:", skin);
        this.moveDownKeyLabel = new Label("", skin);
        this.moveDownButton = new TextButton("S", skin);
        this.moveLeftLabel = new Label("Left:", skin);
        this.moveLeftKeyLabel = new Label("", skin);
        this.moveLeftButton = new TextButton("A", skin);
        this.moveRightLabel = new Label("Right:", skin);
        this.moveRightKeyLabel = new Label("", skin);
        this.moveRightButton = new TextButton("D", skin);
        this.shootLabel = new Label("Auto Shoot:", skin);
        this.shootKeyLabel = new Label("", skin);
        this.shootButton = new TextButton("Space", skin);
        this.reloadLabel = new Label("Reload:", skin);
        this.reloadKeyLabel = new Label("", skin);
        this.reloadButton = new TextButton("R", skin);

        // Game Settings
        this.gameSettingsLabel = new Label("Game Settings", skin, "title");
        this.autoReloadLabel = new Label("Auto-Reload:", skin);
        this.autoReloadToggle = new CheckBox("Enabled", skin);
        this.grayscaleLabel = new Label("Grayscale Mode:", skin);
        this.grayscaleToggle = new CheckBox("Enabled", skin);

        // Navigation
        this.backButton = new TextButton("Back", skin);

        // Initialize music options
        Array<String> musicOptions = new Array<>();
        musicOptions.add("Sweating Bullets");

        this.musicSelectBox.setItems(musicOptions);

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // First, create the table
        table = new Table();

        // Set up background
        backgroundTexture = new Texture(Gdx.files.internal("backGround.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add background to stage directly
        stage.addActor(backgroundImage);

        // Title
        Label titleLabel = new Label("Settings", GameAssetManager.getGameAssetManager().getSkin(), "title");
        table.add(titleLabel).colspan(4).center().padBottom(30);
        table.row();

        // Music Volume
        table.add(musicVolumeLabel).left().padRight(10);
        table.add(musicVolumeSlider).width(200).padRight(20);
        table.add(musicSelectLabel).left().padRight(10);
        table.add(musicSelectBox).width(200);
        table.row().pad(10);

        // SFX Toggle
        table.add(sfxLabel).left().padRight(10);
        table.add(sfxToggle).left();
        table.add().colspan(2);
        table.row().pad(20);

        // Game Settings
        table.add(gameSettingsLabel).colspan(4).center().padTop(20).padBottom(10);
        table.row();

        // Auto-Reload
        table.add(autoReloadLabel).left().padRight(10);
        table.add(autoReloadToggle).left();

        // Grayscale Mode
        table.add(grayscaleLabel).left().padRight(10);
        table.add(grayscaleToggle).left();
        table.row().pad(20);

        // Keyboard Controls
        Table controlsTable = new Table();
        controlsTable.add(moveUpLabel).width(100).left();
        moveUpLabel.setColor(Color.valueOf("7F29FF"));
        controlsTable.add(moveUpKeyLabel).width(80).center();
        controlsTable.add(moveUpButton).width(100);
        moveUpButton.setColor(Color.valueOf("EC2F7B"));
        controlsTable.row().pad(5);

        // Move Down
        controlsTable.add(moveDownLabel).width(100).left();
        moveDownLabel.setColor(Color.valueOf("7F29FF"));
        controlsTable.add(moveDownKeyLabel).width(80).center();
        controlsTable.add(moveDownButton).width(100);
        moveDownButton.setColor(Color.valueOf("EC2F7B"));
        controlsTable.row().pad(5);

        // Move Left
        controlsTable.add(moveLeftLabel).width(100).left();
        moveLeftLabel.setColor(Color.valueOf("7F29FF"));
        controlsTable.add(moveLeftKeyLabel).width(80).center();
        controlsTable.add(moveLeftButton).width(100);
        moveLeftButton.setColor(Color.valueOf("EC2F7B"));
        controlsTable.row().pad(5);

        // Move Right
        controlsTable.add(moveRightLabel).width(100).left();
        moveRightLabel.setColor(Color.valueOf("7F29FF"));
        controlsTable.add(moveRightKeyLabel).width(80).center();
        controlsTable.add(moveRightButton).width(100);
        moveRightButton.setColor(Color.valueOf("EC2F7B"));
        controlsTable.row().pad(5);

        // Shoot
        controlsTable.add(shootLabel).width(100).left();
        shootLabel.setColor(Color.valueOf("7F29FF"));
        controlsTable.add(shootKeyLabel).width(80).center();
        controlsTable.add(shootButton).width(100);
        shootButton.setColor(Color.valueOf("EC2F7B"));
        controlsTable.row().pad(5);

        // Reload
        controlsTable.add(reloadLabel).width(100).left();
        reloadLabel.setColor(Color.valueOf("7F29FF"));
        controlsTable.add(reloadKeyLabel).width(80).center();
        controlsTable.add(reloadButton).width(100);
        reloadButton.setColor(Color.valueOf("EC2F7B"));

        table.add(controlsTable).colspan(4).center().padTop(10);
        table.row().pad(30);

        // Back Button
        table.add(backButton).colspan(4).center().width(200).height(100);
        backButton.setColor(Color.valueOf("fc8eac"));
        table.row().pad(20);

        ScrollPane scrollPane = new ScrollPane(table, GameAssetManager.getGameAssetManager().getSkin());
        scrollPane.setFillParent(true);

        scrollPane.setScrollingDisabled(true, false);

        table.pad(20, 20, 50, 20);

        stage.addActor(scrollPane);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        if (backgroundImage != null) {
            backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }

    public void showKeyBindingWindow(String actionName, Consumer<Integer> keyBindingCallback) {
        KeyBindingWindow keyBindingWindow = new KeyBindingWindow(actionName, GameAssetManager.getGameAssetManager().getSkin(), keyBindingCallback);
        stage.addActor(keyBindingWindow);
    }

    // Getters
    public Slider getMusicVolumeSlider() {
        return musicVolumeSlider;
    }

    public SelectBox<String> getMusicSelectBox() {
        return musicSelectBox;
    }

    public CheckBox getSfxToggle() {
        return sfxToggle;
    }

    public TextButton getMoveUpButton() {
        return moveUpButton;
    }

    public Label getMoveUpKeyLabel() {
        return moveUpKeyLabel;
    }

    public TextButton getMoveDownButton() {
        return moveDownButton;
    }

    public Label getMoveDownKeyLabel() {
        return moveDownKeyLabel;
    }

    public TextButton getMoveLeftButton() {
        return moveLeftButton;
    }

    public Label getMoveLeftKeyLabel() {
        return moveLeftKeyLabel;
    }

    public TextButton getMoveRightButton() {
        return moveRightButton;
    }

    public Label getMoveRightKeyLabel() {
        return moveRightKeyLabel;
    }

    public TextButton getShootButton() {
        return shootButton;
    }

    public Label getShootKeyLabel() {
        return shootKeyLabel;
    }

    public TextButton getReloadButton() {
        return reloadButton;
    }

    public Label getReloadKeyLabel() {
        return reloadKeyLabel;
    }

    public CheckBox getAutoReloadToggle() {
        return autoReloadToggle;
    }

    public CheckBox getGrayscaleToggle() {
        return grayscaleToggle;
    }

    public TextButton getBackButton() {
        return backButton;
    }

    public Stage getStage() {
        return stage;
    }
}
