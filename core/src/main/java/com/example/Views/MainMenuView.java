package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.MainMenuController;
import com.example.Main;


public class MainMenuView implements Screen {
    private Stage stage;
    private final TextButton playButton;
    private final TextButton settingsButton;
    private final TextButton profileButton;
    private final TextButton scoreboardButton;
    private final TextButton hintButton;
    private final Label gameTitle;
    private final TextField field;
    public Table table;
    private final MainMenuController controller;

    public MainMenuView(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.playButton = new TextButton("Play", skin);
        this.settingsButton = new TextButton("Settings", skin);
        this.profileButton = new TextButton("Profile", skin);
        this.scoreboardButton = new TextButton("Scoreboard", skin);
        this.hintButton = new TextButton("Hint", skin);
        this.gameTitle = new Label("Main Menu", skin);
        this.field = new TextField("this is a field", skin);
        this.table = new Table();

        controller.setView(this);

    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        table.add(gameTitle);
        table.row().pad(10,0,10,0);
        table.add(field).width(500);
        table.row().pad(10,0,10,0);
        table.add(playButton);
        table.row().pad(10,0,10,0);
        table.add(settingsButton);
        table.row().pad(10,0,10,0);
        table.add(profileButton);
        table.row().pad(10,0,10,0);
        table.add(scoreboardButton);
        table.row().pad(10,0,10,0);
        table.add(hintButton);

        stage.addActor(table);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0,0);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.handleMainMenuButtons();
    }

    @Override
    public void resize(int i, int i1) {

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

    }

    public Stage getStage() {
        return stage;
    }

    public TextButton getPlayButton() {
        return playButton;
    }

    public TextButton getSettingsButton() {
        return settingsButton;
    }

    public TextButton getProfileButton() {
        return profileButton;
    }

    public TextButton getScoreboardButton() {
        return scoreboardButton;
    }

    public TextButton getHintButton() {
        return hintButton;
    }

    public Label getGameTitle() {
        return gameTitle;
    }

    public TextField getField() {
        return field;
    }

    public Table getTable() {
        return table;
    }

    public MainMenuController getController() {
        return controller;
    }
}
