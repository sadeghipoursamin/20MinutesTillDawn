package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.HintMenuController;
import com.example.Main;
import com.example.Models.utilities.GameAssetManager;


public class HintMenuView implements Screen {
    private Stage stage;
    private Texture lilith;
    private Texture dasher;
    private Texture scarlet;
    private Label lilithLabel;
    private Label dasherLabel;
    private Label scarletLabel;
    private Label lilithTitleLabel;
    private Label dasherTitleLabel;
    private Label scarletTitleLabel;
    private Label cheatLabel;
    private Label cheatLabel2;
    private Label cheatLabel3;
    private Label cheatLabel4;
    private Label cheatTitleLabel;
    private Label abilityLabel;
    private Label abilityLabel2;
    private Label abilityLabel3;
    private Label abilityLabel4;
    private Label abilityLabel5;
    private Label abilityTitleLabel;
    private Label keyTitleLabel;
    private Label moveUpLabel;
    private TextButton moveUpButton;
    private Label moveDownLabel;
    private TextButton moveDownButton;
    private Label moveLeftLabel;
    private TextButton moveLeftButton;
    private Label moveRightLabel;
    private TextButton moveRightButton;
    private Label shootLabel;
    private TextButton shootButton;
    private Label reloadLabel;
    private TextButton reloadButton;
    private Image image;
    private Table table;
    private Texture background;
    private Label menuTitleLabel;
    private TextButton backButton;
    private HintMenuController controller;

    public HintMenuView(HintMenuController controller, Skin skin) {
        table = new Table(skin);
        table.top().pad(10);
        table.setFillParent(true);
        this.controller = controller;

        lilith = GameAssetManager.getGameAssetManager().getLilithPortraitTex();
        dasher = GameAssetManager.getGameAssetManager().getDasherPortraitTex();
        scarlet = GameAssetManager.getGameAssetManager().getScarletPortraitTex();

        // Title Labels
        lilithTitleLabel = new Label("Lilith", skin, "title");
        dasherTitleLabel = new Label("Dasher", skin, "title");
        scarletTitleLabel = new Label("Scarlet", skin, "title");
        menuTitleLabel = new Label("Hint Menu", skin, "title");

        // Info Labels
        lilithLabel = new Label("HP: 5 | Speed: 3", skin);
        dasherLabel = new Label("HP: 2 | Speed: 10", skin);
        scarletLabel = new Label("HP: 3 | Speed: 5", skin);

        // Cheat & Ability Sections
        cheatTitleLabel = new Label("Cheats", skin, "title");
        cheatLabel = new Label("Ammo -> Ammo Refill", skin);
        cheatLabel2 = new Label("Boss Fight -> Starts a Boss Fight", skin);
        cheatLabel3 = new Label("Health -> Restore Health if Dead", skin);
        cheatLabel4 = new Label("Time -> Reduces Game Time", skin);
        abilityTitleLabel = new Label("Abilities", skin, "title");
        abilityLabel = new Label("Vitality: increases HP max (+1)", skin);
        abilityLabel2 = new Label("Damager: increases damage for 25% for 10 seconds", skin);
        abilityLabel3 = new Label("Procrease: increases projectile (+1)", skin);
        abilityLabel4 = new Label("Ammocrease: increases Ammo (+5)", skin);
        abilityLabel5 = new Label("Speedy: Multiplies speed by 2 for 10 seconds", skin);

        // Controls
        keyTitleLabel = new Label("Key Bindings", skin);
        moveUpLabel = new Label("Move Up", skin);
        moveUpButton = new TextButton("W", skin);
        moveDownLabel = new Label("Move Down", skin);
        moveDownButton = new TextButton("S", skin);
        moveLeftLabel = new Label("Move Left", skin);
        moveLeftButton = new TextButton("A", skin);
        moveRightLabel = new Label("Move Right", skin);
        moveRightButton = new TextButton("D", skin);
        shootLabel = new Label("Shoot", skin);
        shootButton = new TextButton("Left Click", skin);
        reloadLabel = new Label("Reload", skin);
        reloadButton = new TextButton("R", skin);
        background = new Texture("background.png");
        backButton = new TextButton("Back", skin);
        controller.setView(this);
    }


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Background
        image = new Image(background);
        image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(image);

        table.clear();
        table.top().pad(30).setFillParent(true);

        table.add(menuTitleLabel).colspan(2).center().padBottom(40);
        table.row();

        Table characterTable = new Table();

        // Section: Characters
        characterTable.add(lilithTitleLabel).left().padRight(20);
        characterTable.add(new Image(lilith)).size(100, 100).padRight(20);
        characterTable.add(lilithLabel).left();
        characterTable.row().padBottom(20);

        characterTable.add(dasherTitleLabel).left().padRight(20);
        characterTable.add(new Image(dasher)).size(100, 100).padRight(20);
        characterTable.add(dasherLabel).left();
        characterTable.row().padBottom(20);

        characterTable.add(scarletTitleLabel).left().padRight(20);
        characterTable.add(new Image(scarlet)).size(100, 100).padRight(20);
        characterTable.add(scarletLabel).left();
        characterTable.row().padTop(30);

        // Section: Cheats
        characterTable.add(cheatTitleLabel).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(cheatLabel).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(cheatLabel2).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(cheatLabel3).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(cheatLabel4).colspan(3).left();
        characterTable.row().padTop(30);

        // Section: Abilities
        characterTable.add(abilityTitleLabel).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(abilityLabel).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(abilityLabel2).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(abilityLabel3).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(abilityLabel4).colspan(3).left().padBottom(5);
        characterTable.row();
        characterTable.add(abilityLabel5).colspan(3).left();
        characterTable.row().padTop(30);

        // Section: Key Bindings
        characterTable.add(keyTitleLabel).colspan(3).left().padBottom(10);
        characterTable.row().padTop(30);

        characterTable.add(moveUpLabel).left();
        characterTable.add(moveUpButton).left();
        characterTable.row().padBottom(10);

        characterTable.add(moveDownLabel).left();
        characterTable.add(moveDownButton).left();
        characterTable.row().padBottom(10);

        characterTable.add(moveLeftLabel).left();
        characterTable.add(moveLeftButton).left();
        characterTable.row().padBottom(10);

        characterTable.add(moveRightLabel).left();
        characterTable.add(moveRightButton).left();
        characterTable.row().padTop(20);

        characterTable.add(shootLabel).left();
        characterTable.add(shootButton).left();
        characterTable.row().padBottom(10);

        characterTable.add(reloadLabel).left();
        characterTable.add(reloadButton).left();
        characterTable.row();

        characterTable.add(backButton).left();
        characterTable.row();

        // Scrollable Container
        ScrollPane scrollPane = new ScrollPane(characterTable);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        table.add(scrollPane).expand().fill().colspan(2);
        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.handleHintMenuButtons();
    }


    @Override
    public void resize(int width, int height) {

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
        if (background != null) {
            background.dispose();
        }
    }

    public TextButton getBackButton() {
        return backButton;
    }
}
