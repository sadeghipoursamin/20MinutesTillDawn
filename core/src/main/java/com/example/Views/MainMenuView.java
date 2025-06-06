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
import com.example.Controllers.EnhancedAvatarManager;
import com.example.Controllers.MainMenuController;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.enums.Language;

public class MainMenuView implements Screen {
    private final TextButton playButton;
    private final TextButton settingsButton;
    private final TextButton profileButton;
    private final TextButton scoreboardButton;
    private final TextButton hintButton;
    private final TextButton continueGameButton;
    private final TextButton loguotButton;
    private final Label gameTitle;
    private final MainMenuController controller;
    public Table table;
    // Player info components
    private Table playerInfoTable;
    private Image playerAvatarImage;
    private Label playerUsernameLabel;
    private Label playerScoreLabel;
    private Label welcomeLabel;
    private Stage stage;
    private Label errorLabel;
    private Image image;
    private Texture texture;

    public MainMenuView(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.playButton = new TextButton(Language.Play.getText(), skin);
        this.settingsButton = new TextButton(Language.Settings.getText(), skin);
        this.profileButton = new TextButton(Language.Profile.getText(), skin);
        this.scoreboardButton = new TextButton(Language.Leaderboard.getText(), skin);
        this.hintButton = new TextButton(Language.Hint.getText(), skin);
        this.gameTitle = new Label(Language.MainMenu.getText(), skin, "title");
        this.table = new Table();
        this.errorLabel = new Label("", skin);
        this.continueGameButton = new TextButton(Language.Continue.getText(), skin);
        this.loguotButton = new TextButton(Language.Logout.getText(), skin);

        // Initialize player info components
        initializePlayerInfoComponents(skin);

        controller.setView(this);
    }

    private void initializePlayerInfoComponents(Skin skin) {
        // Create player info table
        playerInfoTable = new Table(skin);

        // Avatar image
        playerAvatarImage = new Image();

        // Labels
        welcomeLabel = new Label("Welcome back,", skin);
        welcomeLabel.setColor(Color.CYAN);
        welcomeLabel.setFontScale(1.2f);

        playerUsernameLabel = new Label("", skin);
        playerUsernameLabel.setColor(Color.YELLOW);
        playerUsernameLabel.setFontScale(1.4f);

        playerScoreLabel = new Label("", skin);
        playerScoreLabel.setColor(Color.WHITE);
        playerScoreLabel.setFontScale(1.1f);

        // Update player info
        updatePlayerInfo();
    }

    private void updatePlayerInfo() {
        User currentUser = App.getCurrentUser();

        if (currentUser != null) {
            // Update username
            playerUsernameLabel.setText(currentUser.getUsername());

            // Update score with formatting
            playerScoreLabel.setText("Score: " + formatScore(currentUser.getScore()));

            // Update avatar
            try {
                String avatarPath = currentUser.getAvatarPath();
                Texture avatarTexture = EnhancedAvatarManager.getInstance().getAvatarTexture(avatarPath);
                playerAvatarImage.setDrawable(new TextureRegionDrawable(avatarTexture));
            } catch (Exception e) {
                System.err.println("Error loading avatar for main menu: " + e.getMessage());
                // Set default avatar if loading fails
                setDefaultAvatar();
            }
        } else {
            // Guest user
            playerUsernameLabel.setText("Guest User");
            playerScoreLabel.setText("Score: 0");
            setDefaultAvatar();
        }
    }

    private void setDefaultAvatar() {
        try {
            Texture defaultAvatar = EnhancedAvatarManager.getInstance().getAvatarTexture(null);
            playerAvatarImage.setDrawable(new TextureRegionDrawable(defaultAvatar));
        } catch (Exception e) {
            System.err.println("Error setting default avatar: " + e.getMessage());
        }
    }

    private String formatScore(int score) {
        // Format score with commas for better readability
        if (score >= 1000000) {
            return String.format("%.1fM", score / 1000000.0);
        } else if (score >= 1000) {
            return String.format("%.1fK", score / 1000.0);
        } else {
            return String.valueOf(score);
        }
    }

    private void setupPlayerInfoLayout() {
        playerInfoTable.clear();

        // Create avatar frame with border effect
        Table avatarFrame = new Table();
        avatarFrame.add(playerAvatarImage).size(80, 80).pad(5);

        // Create info section
        Table infoSection = new Table();
        infoSection.add(welcomeLabel).left();
        infoSection.row();
        infoSection.add(playerUsernameLabel).left().padBottom(5);
        infoSection.row();
        infoSection.add(playerScoreLabel).left();

        // Combine avatar and info
        playerInfoTable.add(avatarFrame).padRight(15);
        playerInfoTable.add(infoSection).expandX().fillX().left();
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

        // Setup player info layout
        setupPlayerInfoLayout();

        // Add player info at the top
        table.add(playerInfoTable).colspan(2).left().top().padTop(20).padLeft(20);
        table.row();

        // Add some spacing
        table.add().colspan(2).height(20);
        table.row();

        // Game title
        table.add(gameTitle).colspan(2).center().pad(10);
        table.row().pad(10, 0, 10, 0);

        // Main menu buttons
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
        stage.getViewport().update(i, i1, true);
        if (image != null) {
            image.setSize(stage.getWidth(), stage.getHeight());
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
        if (texture != null) {
            texture.dispose();
        }
    }

    // Method to refresh player info when returning from profile or after login
    public void refreshPlayerInfo() {
        updatePlayerInfo();
        setupPlayerInfoLayout();
    }

    // Getters for controller access
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
}
