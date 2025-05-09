package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.MainMenuController;
import com.example.Controllers.SignupMenuController;
import com.example.Main;

public class SignupMenuView implements Screen {
    private Stage stage;
    private final Label signupTitle;
    public Table table;
    private final SignupMenuController controller;
    private final TextField username;
    private final Label usernameLabel;
    private final TextField password;
    private final Label passwordLabel;
    private final TextField securityQuestion;
    private final Label securityQuestionLabel;
    private final TextButton guestButton;

    public SignupMenuView(SignupMenuController controller, Skin skin) {
        this.controller = controller;
        this.username = new TextField("", skin);
        this.usernameLabel = new Label("Username: ", skin);
        this.password = new TextField("", skin);
        this.passwordLabel = new Label("Password: ", skin);
        this.securityQuestion = new TextField("", skin);
        this.securityQuestionLabel = new Label("Security Question: ", skin);
        this.guestButton = new TextButton("Guest", skin);
        this.signupTitle = new Label("Signup Menu", skin);
        this.table = new Table();
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        table.add(signupTitle);
        table.row().pad(20, 400, 20, 0);
        table.add(username).width(400);
        table.row().pad(20, 400, 20, 0);
        table.add(password).width(400);
        table.row().pad(20, 400, 20, 0);
        table.add(securityQuestion).width(400);
        table.row().pad(20, 400, 20, 0);
        table.add(guestButton);
        table.row().pad(10, 0, 10, 0);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,0);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.handleSignupMenuButtons();
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
}
