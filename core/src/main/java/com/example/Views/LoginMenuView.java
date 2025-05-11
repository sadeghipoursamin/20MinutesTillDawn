package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.LoginMenuController;
import com.example.Controllers.SignupMenuController;
import com.example.Main;

public class LoginMenuView implements Screen {

    private Stage stage;
    private final Label loginTitle;
    public Table table;
    private final LoginMenuController controller;
    private final TextField username;
    private final Label usernameLabel;
    private final TextField password;
    private final Label passwordLabel;
    private final TextButton forgotPassword;
    private final TextButton loginButton;
    private Label errorLabel;


    public LoginMenuView(LoginMenuController controller, Skin skin) {
        this.controller = controller;
        this.username = new TextField("", skin);
        this.usernameLabel = new Label("Username: ", skin);
        this.password = new TextField("", skin);
        this.passwordLabel = new Label("Password: ", skin);
        this.forgotPassword = new TextButton("Forgot Password?", skin);
        this.loginButton = new TextButton("Signup", skin);
        this.loginTitle = new Label("Login Menu", skin, "title");
        this.table = new Table();
        this.errorLabel = new Label("", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.add(loginTitle).colspan(2).center();
        table.row().pad(20, 0, 20, 0);

        //username
        table.add(usernameLabel).height(40).padRight(20);
        table.add(username).width(400).height(80);
        table.row().pad(20, 0, 20, 0);

        //password
        table.add(passwordLabel).height(40).padRight(20);
        table.add(password).width(400).height(80);
        table.row().pad(20, 0, 20, 0);

        table.add(loginButton).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        table.add(forgotPassword).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        table.add(errorLabel).colspan(2).center();
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
