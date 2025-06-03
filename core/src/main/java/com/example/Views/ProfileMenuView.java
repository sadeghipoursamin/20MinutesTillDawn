package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.ProfileMenuController;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.enums.Language;
import com.example.Models.utilities.AvatarManager;

public class ProfileMenuView implements Screen {
    private final ProfileMenuController controller;

    // UI Components
    private Stage stage;
    private Table mainTable;
    private ScrollPane scrollPane;
    private Image backgroundImage;
    private Texture backgroundTexture;

    // Profile Display
    private Label titleLabel;
    private Image avatarImage;
    private Label avatarNameLabel;
    private Label usernameDisplayLabel;
    private Label userStatsLabel;

    // Action Buttons
    private TextButton changeUsernameButton;
    private TextButton changePasswordButton;
    private TextButton deleteAccountButton;
    private TextButton chooseAvatarButton;
    private TextButton backButton;

    // Status Display
    private Label statusLabel;

    // User Data
    private User currentUser;

    public ProfileMenuView(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        this.currentUser = App.getCurrentUser();

        initializeComponents(skin);
        controller.setView(this);
    }

    private void initializeComponents(Skin skin) {
        // Main containers
        mainTable = new Table(skin);

        // Title
        titleLabel = new Label("USER PROFILE", skin, "title");
        titleLabel.setColor(Color.CYAN);

        // Avatar display
        avatarImage = new Image();
        avatarNameLabel = new Label("", skin);
        avatarNameLabel.setColor(Color.YELLOW);
        updateAvatarDisplay();

        // User info display
        usernameDisplayLabel = new Label("", skin);
        usernameDisplayLabel.setColor(Color.WHITE);

        userStatsLabel = new Label("", skin);
        userStatsLabel.setColor(Color.LIGHT_GRAY);

        // Action buttons
        changeUsernameButton = new TextButton(Language.ChangeUsernameButton.getText(), skin);
        changeUsernameButton.setColor(Color.GREEN);

        changePasswordButton = new TextButton(Language.ChangePasswordButton.getText(), skin);
        changePasswordButton.setColor(Color.YELLOW);

        deleteAccountButton = new TextButton(Language.DeleteAccountButton.getText(), skin);
        deleteAccountButton.setColor(Color.RED);

        chooseAvatarButton = new TextButton(Language.ChooseAvatarButton.getText(), skin);
        chooseAvatarButton.setColor(Color.MAGENTA);

        backButton = new TextButton(Language.Back.getText(), skin);
        backButton.setColor(Color.GRAY);

        // Status label
        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.GREEN);

        updateUserDisplay();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Background setup
        setupBackground();

        // Layout setup
        setupLayout();

        stage.addActor(scrollPane);
    }

    private void setupBackground() {
        try {
            backgroundTexture = new Texture("backGround.png");
            backgroundImage = new Image(backgroundTexture);
            backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.err.println("Could not load background texture: " + e.getMessage());
        }
    }

    private void setupLayout() {
        mainTable.setFillParent(true);
        mainTable.top().pad(30);

        // Title
        mainTable.add(titleLabel).colspan(2).center().padBottom(30);
        mainTable.row();

        // Avatar section
        Table avatarSection = createAvatarSection();
        mainTable.add(avatarSection).colspan(2).center().padBottom(30);
        mainTable.row();

        // User info section
        Table userInfoSection = createUserInfoSection();
        mainTable.add(userInfoSection).colspan(2).center().padBottom(30);
        mainTable.row();

        // Action buttons section
        Table actionsSection = createActionsSection();
        mainTable.add(actionsSection).colspan(2).center().padBottom(20);
        mainTable.row();

        // Status message
        mainTable.add(statusLabel).colspan(2).center().padBottom(20);
        mainTable.row();

        // Back button
        mainTable.add(backButton).colspan(2).center().width(200).height(60);

        // Create scroll pane
        scrollPane = new ScrollPane(mainTable, com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin());
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
    }

    private Table createAvatarSection() {
        Table avatarSection = new Table();

        // Avatar display with frame
        Table avatarFrame = new Table();
        avatarFrame.setBackground("default-round"); // Add frame background if available
        avatarFrame.add(avatarImage).size(150, 150).pad(10);

        // Avatar info and change button
        Table avatarInfo = new Table();
        avatarInfo.add(avatarNameLabel).padBottom(10);
        avatarInfo.row();
        avatarInfo.add(chooseAvatarButton).width(200).height(60);

        avatarSection.add(avatarFrame).padRight(30);
        avatarSection.add(avatarInfo);

        return avatarSection;
    }

    private Table createUserInfoSection() {
        Table userInfoSection = new Table();

        // Username display
        Table usernameSection = new Table();
        Label usernameLabel = new Label("Username:", com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin());
        usernameLabel.setColor(Color.YELLOW);
        usernameSection.add(usernameLabel).padRight(10);
        usernameSection.add(usernameDisplayLabel).expandX().left();

        userInfoSection.add(usernameSection).fillX().padBottom(15);
        userInfoSection.row();

        // Stats display
        Label statsHeaderLabel = new Label("Statistics:", com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin());
        statsHeaderLabel.setColor(Color.YELLOW);
        userInfoSection.add(statsHeaderLabel).left().padBottom(5);
        userInfoSection.row();

        userInfoSection.add(userStatsLabel).left().padBottom(20);
        userInfoSection.row();

        return userInfoSection;
    }

    private Table createActionsSection() {
        Table actionsSection = new Table();

        // First row of buttons
        actionsSection.add(changeUsernameButton).width(250).height(60).pad(10);
        actionsSection.add(changePasswordButton).width(250).height(60).pad(10);
        actionsSection.row();

        // Second row
        actionsSection.add(deleteAccountButton).width(250).height(60).pad(10);
        actionsSection.add().width(250).height(60).pad(10); // Empty space for symmetry

        return actionsSection;
    }

    private void updateUserDisplay() {
        if (currentUser != null) {
            // Update username display
            usernameDisplayLabel.setText(currentUser.getUsername());

            // Update stats display
            String statsText = String.format(
                "Score: %d\nTotal Kills: %d\nGames Played: %d\nLongest Survival: %s\nTotal Play Time: %s",
                currentUser.getScore(),
                currentUser.getTotalKills(),
                currentUser.getGamesPlayed(),
                currentUser.getFormattedSurvivalTime(),
                currentUser.getFormattedTotalPlayTime()
            );
            userStatsLabel.setText(statsText);
        } else {
            usernameDisplayLabel.setText("Guest User");
            userStatsLabel.setText("No statistics available for guest users");
        }
    }

    private void updateAvatarDisplay() {
        if (currentUser != null) {
            try {
                String avatarPath = currentUser.getAvatarPath();
                Texture avatarTexture = AvatarManager.getInstance().getAvatarTexture(avatarPath);
                avatarImage.setDrawable(new TextureRegionDrawable(avatarTexture));

                // Update avatar name
                String avatarName = currentUser.getAvatarDisplayName();
                avatarNameLabel.setText(avatarName);

                System.out.println("Avatar updated for user: " + currentUser.getUsername() +
                    ", path: " + avatarPath + ", name: " + avatarName);

            } catch (Exception e) {
                System.err.println("Error loading avatar: " + e.getMessage());
                setDefaultAvatar();
            }
        } else {
            setDefaultAvatar();
        }
    }

    private void setDefaultAvatar() {
        try {
            Texture defaultAvatar = AvatarManager.getInstance().getAvatarTexture(null);
            avatarImage.setDrawable(new TextureRegionDrawable(defaultAvatar));
            avatarNameLabel.setText("Default Avatar");
        } catch (Exception e) {
            System.err.println("Error setting default avatar: " + e.getMessage());
        }
    }

    public void refreshAvatarDisplay() {
        updateAvatarDisplay();
        updateUserDisplay(); // Also refresh user data in case it changed
    }

    public void showStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setColor(color);

        // Clear message after 3 seconds
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                statusLabel.setText("");
            }
        }, 3.0f);
    }

    public void showSuccessMessage(String message) {
        showStatusMessage("✓ " + message, Color.GREEN);
    }

    public void showErrorMessage(String message) {
        showStatusMessage("✗ " + message, Color.RED);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        if (backgroundImage != null) {
            backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        }
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
        if (stage != null) {
            stage.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }

    // Getters for controller
    public Stage getStage() {
        return stage;
    }

    public TextButton getUsernameButton() {
        return changeUsernameButton;
    }

    public TextButton getPassword() {
        return changePasswordButton;
    }

    public TextButton getDeleteAccountButton() {
        return deleteAccountButton;
    }

    public TextButton getChooseAvatarButton() {
        return chooseAvatarButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }

    public Label getErrorLabel() {
        return statusLabel; // Reusing status label for backward compatibility
    }

    public Table getTable() {
        return mainTable;
    }

    public ProfileMenuController getController() {
        return controller;
    }
}
