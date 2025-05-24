package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.LoginMenuController;
import com.example.Main;
import com.example.Models.enums.Language;

public class LoginMenuView implements Screen {

    private final Label loginTitle;
    private final LoginMenuController controller;
    private final TextField username;
    private final Label usernameLabel;
    private final TextField password;
    private final Label passwordLabel;
    private final TextButton forgotPassword;
    private final TextButton loginButton;
    public Table table;
    private Stage stage;
    private Label errorLabel;
    private Image image;
    private Texture texture;

    public LoginMenuView(LoginMenuController controller, Skin skin) {
        this.controller = controller;
        this.username = new TextField("", skin);
        this.usernameLabel = new Label(Language.Username.getText(), skin);
        this.password = new TextField("", skin);
        this.passwordLabel = new Label(Language.Password.getText(), skin);
        this.forgotPassword = new TextButton(Language.ForgotPassword.getText(), skin);
        this.loginButton = new TextButton(Language.Login.getText(), skin);
        this.loginTitle = new Label(Language.LoginMenu.getText(), skin, "title");
        this.table = new Table();
        this.errorLabel = new Label("", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);

        texture = new Texture(Gdx.files.internal("backGround.png"));
        image = new Image(texture);
        image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.addActor(image);

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
        ScreenUtils.clear(0, 0, 0, 0);
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

    public Stage getStage() {
        return stage;
    }

    public Label getLoginTitle() {
        return loginTitle;
    }

    public Table getTable() {
        return table;
    }

    public LoginMenuController getController() {
        return controller;
    }

    public TextField getUsername() {
        return username;
    }

    public Label getUsernameLabel() {
        return usernameLabel;
    }

    public TextField getPassword() {
        return password;
    }

    public Label getPasswordLabel() {
        return passwordLabel;
    }

    public TextButton getForgotPassword() {
        return forgotPassword;
    }

    public TextButton getLoginButton() {
        return loginButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
