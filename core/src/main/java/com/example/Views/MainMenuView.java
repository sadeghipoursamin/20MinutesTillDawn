package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.MainMenuController;
import com.example.Main;


public class MainMenuView implements Screen {
    private final TextButton playButton;
    private final TextButton settingsButton;
    private final TextButton profileButton;
    private final TextButton scoreboardButton;
    private final TextButton hintButton;
    private final TextButton continueGameButton;
    private final TextButton loguotButton;
    private final TextButton showInfoButton;
    private final Label gameTitle;
    private final MainMenuController controller;
    public Table table;
    private Stage stage;
    private Label errorLabel;
    private Image image;
    private Texture texture;

    public MainMenuView(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.playButton = new TextButton("Play", skin);
        this.settingsButton = new TextButton("Settings", skin);
        this.profileButton = new TextButton("Profile", skin);
        this.scoreboardButton = new TextButton("Scoreboard", skin);
        this.hintButton = new TextButton("Hint", skin);
        this.gameTitle = new Label("Main Menu", skin, "title");
        this.table = new Table();
        this.errorLabel = new Label("", skin);
        this.continueGameButton = new TextButton("Continue Game", skin);
        this.loguotButton = new TextButton("Logout", skin);
        this.showInfoButton = new TextButton("Show Info", skin);

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);

        texture = new Texture(Gdx.files.internal("mainBackGround.png"));
        image = new Image(texture);
        image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.addActor(image);

        table.center();

        table.add(gameTitle).colspan(2).center().pad(10);
        table.row().pad(10, 0, 10, 0);

        table.add(playButton).width(400).height(100).pad(10);
        table.add(settingsButton).width(400).height(100).pad(10);
        table.row();

        table.add(profileButton).width(400).height(100).pad(10);
        table.add(scoreboardButton).width(400).height(100).pad(10);
        table.row();

        table.add(hintButton).width(400).height(100).pad(10);
        table.add(continueGameButton).width(400).height(100).pad(10);
        table.row();

        table.add(loguotButton).width(400).height(100).pad(10);
        table.add(showInfoButton).width(400).height(100);
        table.row();


        table.add(errorLabel).colspan(2).center().pad(5);

        stage.addActor(table);
    }


    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 0);
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


    public Table getTable() {
        return table;
    }

    public MainMenuController getController() {
        return controller;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public TextButton getContinueGameButton() {
        return continueGameButton;
    }

    public TextButton getLoguotButton() {
        return loguotButton;
    }

    public TextButton getShowInfoButton() {
        return showInfoButton;
    }

}
