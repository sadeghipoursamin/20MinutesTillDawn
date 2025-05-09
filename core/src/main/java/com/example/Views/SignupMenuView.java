package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
    private final TextButton signupButton;
    private Label errorLabel;



    public SignupMenuView(SignupMenuController controller, Skin skin) {
        this.controller = controller;
        this.username = new TextField("", skin);
        this.usernameLabel = new Label("Username: ", skin);
        this.password = new TextField("", skin);
        this.passwordLabel = new Label("Password: ", skin);
        this.securityQuestion = new TextField("", skin);
        this.securityQuestionLabel = new Label("What's your favorite music band?", skin);
        this.guestButton = new TextButton("Guest", skin);
        this.signupButton = new TextButton("Signup", skin);
        this.signupTitle = new Label("Signup Menu", skin);
        this.table = new Table();
        this.errorLabel = new Label("", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.add(signupTitle).colspan(2).center();
        table.row().pad(20, 0, 20, 0);

        //username
        table.add(usernameLabel).height(40).padRight(20);
        table.add(username).width(400).height(80);
        table.row().pad(20, 0, 20, 0);

        //password
        table.add(passwordLabel).height(40).padRight(20);
        table.add(password).width(400).height(80);
        table.row().pad(20, 0, 20, 0);

        // Security question
        table.add(securityQuestionLabel).height(40).padRight(20);
        table.add(securityQuestion).width(400).height(80);
        table.row().pad(20, 0, 20, 0);

        table.add(signupButton).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        table.add(guestButton).colspan(2).center();
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

    public Stage getStage() {
        return stage;
    }

    public Label getSignupTitle() {
        return signupTitle;
    }

    public Table getTable() {
        return table;
    }

    public SignupMenuController getController() {
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

    public TextField getSecurityQuestion() {
        return securityQuestion;
    }

    public Label getSecurityQuestionLabel() {
        return securityQuestionLabel;
    }

    public TextButton getGuestButton() {
        return guestButton;
    }

    public TextButton getSignupButton() {
        return signupButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
