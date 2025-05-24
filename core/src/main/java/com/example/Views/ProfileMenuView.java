package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.ProfileMenuController;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.enums.Language;
import com.example.Models.utilities.AvatarManager;

public class ProfileMenuView implements Screen {
    private final ProfileMenuController controller;
    private final TextButton usernameButton;
    private final TextButton password;
    private final TextButton deleteAccountButton;
    private final TextButton chooseAvatarButton;
    public Table table;
    private Image avatarImage;
    private TextButton changeAvatarButton;
    private Stage stage;
    private Label errorLabel;
    private Image image;
    private Texture texture;

    public ProfileMenuView(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        table = new Table();
        this.usernameButton = new TextButton(Language.ChangeUsername.getText(), skin);
        this.password = new TextButton(Language.ChangePassword.getText(), skin);
        this.deleteAccountButton = new TextButton(Language.DeleteAccountButton.getText(), skin);
        this.chooseAvatarButton = new TextButton(Language.ChooseAvatarButton.getText(), skin);
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

    private void updateAvatarDisplay() {
        User currentUser = App.getCurrentUser();
        if (currentUser != null) {
            String avatarPath = currentUser.getAvatarPath();
            if (avatarPath != null && !avatarPath.isEmpty()) {
                Texture avatarTexture = AvatarManager.getInstance().getAvatarTexture(avatarPath);
                avatarImage.setDrawable(new TextureRegionDrawable(avatarTexture));
            } else {
                // Set default avatar
                Texture defaultAvatar = AvatarManager.getInstance().getAvatarTexture(null);
                avatarImage.setDrawable(new TextureRegionDrawable(defaultAvatar));
            }
        }
    }

    public void refreshAvatarDisplay() {
        updateAvatarDisplay();
    }

}
