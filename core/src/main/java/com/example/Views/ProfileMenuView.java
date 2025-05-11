package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.ProfileMenuController;
import com.example.Controllers.SignupMenuController;
import com.example.Main;

public class ProfileMenuView implements Screen {
    private Stage stage;
    public Table table;
    private final ProfileMenuController controller;
    private final TextButton usernameButton;
    private final TextButton password;
    private final TextButton deleteAccountButton;
    private final TextButton chooseAvatarButton;
    private Label errorLabel;

    public ProfileMenuView(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        table = new Table();
        this.usernameButton = new TextButton("Change Username", skin);
        this.password = new TextButton("Change Password", skin);
        this.deleteAccountButton = new TextButton("Delete Account", skin);
        this.chooseAvatarButton = new TextButton("Choose Avatar", skin);
        this.errorLabel = new Label("", skin);

        controller.setView(this);
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.add(usernameButton).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        table.add(password).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        table.add(deleteAccountButton).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        table.add(chooseAvatarButton).colspan(2).center();
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

    public Table getTable() {
        return table;
    }

    public ProfileMenuController getController() {
        return controller;
    }

    public TextButton getUsernameButton() {
        return usernameButton;
    }

    public TextButton getPassword() {
        return password;
    }

    public TextButton getDeleteAccountButton() {
        return deleteAccountButton;
    }

    public TextButton getChooseAvatarButton() {
        return chooseAvatarButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
