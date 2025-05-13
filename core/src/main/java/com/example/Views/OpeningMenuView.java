package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.OpeningMenuController;
import com.example.Main;
import com.example.Models.enums.Language;

public class OpeningMenuView implements Screen {

    private final Label welcomeLabel;
    private final OpeningMenuController controller;
    private final TextButton loginButton;
    private final TextButton signupButton;
    private final TextButton exitButton;
    private final TextButton changeLanguageButton;
    public Table table;
    private Stage stage;
    private Image image;
    private Texture texture;

    public OpeningMenuView(OpeningMenuController controller, Skin skin) {
        this.controller = controller;
        this.table = new Table();
        this.welcomeLabel = new Label("", skin, "title");
        this.loginButton = new TextButton(Language.Login.getText(), skin);
        this.signupButton = new TextButton("Signup", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.changeLanguageButton = new TextButton("Language: English", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);

        texture = new Texture(Gdx.files.internal("opening.png"));
        image = new Image(texture);
        image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.addActor(image);

        table.add(welcomeLabel).colspan(2).center();
        table.row().pad(20, 0, 20, 0);

        table.add(signupButton).colspan(2).center();
        table.row().pad(15, 0, 5, 0);

        table.add(loginButton).colspan(2).center();
        table.row().pad(5, 0, 5, 0);

        table.add(changeLanguageButton).colspan(2).center();
        table.row().pad(5, 0, 5, 0);

        table.add(exitButton).colspan(2).center();
        table.row().pad(5, 0, 5, 0);


        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.handleOpeningMenu();
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

    }

    public Stage getStage() {
        return stage;
    }

    public Label getWelcomeLabel() {
        return welcomeLabel;
    }

    public Table getTable() {
        return table;
    }

    public OpeningMenuController getController() {
        return controller;
    }

    public TextButton getLoginButton() {
        return loginButton;
    }

    public TextButton getSignupButton() {
        return signupButton;
    }

    public TextButton getExitButton() {
        return exitButton;
    }

    public TextButton getChangeLanguageButton() {
        return changeLanguageButton;
    }
}
