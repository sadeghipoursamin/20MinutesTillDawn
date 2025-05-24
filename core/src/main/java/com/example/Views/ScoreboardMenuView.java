package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.ScoreboardMenuController;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;

import java.util.List;

public class ScoreboardMenuView implements Screen {
    private final ScoreboardMenuController controller;

    // UI Components
    private Stage stage;
    private Table mainTable;
    private Table headerTable;
    private Table scoreboardTable;
    private ScrollPane scrollPane;
    private Image backgroundImage;
    private Texture backgroundTexture;

    // Header elements
    private Label titleLabel;
    private TextButton sortScoreButton;
    private TextButton sortUsernameButton;
    private TextButton sortKillsButton;
    private TextButton sortSurvivalButton;
    private TextButton backButton;

    // Column headers
    private Label rankHeader;
    private Label usernameHeader;
    private Label scoreHeader;
    private Label killsHeader;
    private Label survivalHeader;
    private Label gamesHeader;

    public ScoreboardMenuView(ScoreboardMenuController controller, Skin skin) {
        this.controller = controller;

        // Initialize UI components with skin
        initializeComponents(skin);

        controller.setView(this);
    }

    private void initializeComponents(Skin skin) {
        // Main containers
        mainTable = new Table(skin);
        headerTable = new Table(skin);
        scoreboardTable = new Table(skin);

        // Title
        titleLabel = new Label("SCOREBOARD", skin, "title");
        titleLabel.setColor(Color.GOLD);

        // Sort buttons
        sortScoreButton = new TextButton("Score", skin);
        sortScoreButton.setColor(Color.CYAN);

        sortUsernameButton = new TextButton("Name", skin);
        sortUsernameButton.setColor(Color.CYAN);

        sortKillsButton = new TextButton("Kills", skin);
        sortKillsButton.setColor(Color.CYAN);

        sortSurvivalButton = new TextButton("Survival", skin);
        sortSurvivalButton.setColor(Color.CYAN);

        backButton = new TextButton("Back", skin);
        backButton.setColor(Color.RED);

        // Column headers
        rankHeader = new Label("Rank", skin);
        rankHeader.setColor(Color.WHITE);

        usernameHeader = new Label("Username", skin);
        usernameHeader.setColor(Color.WHITE);

        scoreHeader = new Label("Score", skin);
        scoreHeader.setColor(Color.WHITE);

        killsHeader = new Label("Kills", skin);
        killsHeader.setColor(Color.WHITE);

        survivalHeader = new Label("Best Time", skin);
        survivalHeader.setColor(Color.WHITE);

        gamesHeader = new Label("Games", skin);
        gamesHeader.setColor(Color.WHITE);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Background
        try {
            backgroundTexture = new Texture("mainBackGround.png");
            backgroundImage = new Image(backgroundTexture);
            backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.err.println("Could not load background texture: " + e.getMessage());
        }

        setupLayout();
        stage.addActor(mainTable);
    }

    private void setupLayout() {
        mainTable.setFillParent(true);
        mainTable.top().pad(20);

        // Title
        mainTable.add(titleLabel).colspan(4).center().padBottom(30);
        mainTable.row();

        // Sort buttons
        Table sortButtonsTable = new Table();
        sortButtonsTable.add(sortScoreButton).width(150).height(50).pad(5);
        sortButtonsTable.add(sortUsernameButton).width(150).height(50).pad(5);
        sortButtonsTable.add(sortKillsButton).width(150).height(50).pad(5);
        sortButtonsTable.add(sortSurvivalButton).width(150).height(50).pad(5);

        mainTable.add(sortButtonsTable).colspan(4).center().padBottom(20);
        mainTable.row();

        // Headers
        setupHeaders();
        mainTable.add(headerTable).fillX().padBottom(10);
        mainTable.row();

        // Scoreboard content
        scrollPane = new ScrollPane(scoreboardTable, GameAssetManager.getGameAssetManager().getSkin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        mainTable.add(scrollPane).expand().fill().padBottom(20);
        mainTable.row();

        // Back button
        mainTable.add(backButton).width(200).height(60).center();
    }

    private void setupHeaders() {
        headerTable.clear();

        headerTable.pad(10);

        headerTable.add(rankHeader).width(80).center().pad(10);
        headerTable.add(usernameHeader).width(150).center().pad(10);
        headerTable.add(scoreHeader).width(100).center().pad(10);
        headerTable.add(killsHeader).width(100).center().pad(10);
        headerTable.add(survivalHeader).width(120).center().pad(10);
        headerTable.add(gamesHeader).width(100).center().pad(10);
    }

    public void updateScoreboardData(List<User> users, ScoreboardMenuController.SortType sortType, boolean isAscending) {
        scoreboardTable.clear();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            int rank = i + 1;

            addUserRow(user, rank, controller.isCurrentUser(user));
        }
    }

    private void addUserRow(User user, int rank, boolean isCurrentUser) {
        Skin skin = com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin();

        Label rankLabel = new Label(String.valueOf(rank), skin);
        Label usernameLabel = new Label(user.getUsername(), skin);
        Label scoreLabel = new Label(String.valueOf(user.getScore()), skin);
        Label killsLabel = new Label(String.valueOf(user.getTotalKills()), skin);
        Label survivalLabel = new Label(user.getFormattedSurvivalTime(), skin);
        Label gamesLabel = new Label(String.valueOf(user.getGamesPlayed()), skin);

        if (rank <= 3) {
            Color rankColor = getRankColor(rank);
            rankLabel.setColor(rankColor);
            usernameLabel.setColor(rankColor);
            scoreLabel.setColor(rankColor);
            killsLabel.setColor(rankColor);
            survivalLabel.setColor(rankColor);
            gamesLabel.setColor(rankColor);

            float scale = rank == 1 ? 1.4f : (rank == 2 ? 1.2f : 1.1f);
            rankLabel.setFontScale(scale);
            usernameLabel.setFontScale(scale);
            scoreLabel.setFontScale(scale);
            killsLabel.setFontScale(scale);
            survivalLabel.setFontScale(scale);
            gamesLabel.setFontScale(scale);
        }

        if (isCurrentUser) {
            Color currentUserColor = Color.YELLOW;
            rankLabel.setColor(currentUserColor);
            usernameLabel.setColor(currentUserColor);
            scoreLabel.setColor(currentUserColor);
            killsLabel.setColor(currentUserColor);
            survivalLabel.setColor(currentUserColor);
            gamesLabel.setColor(currentUserColor);

            scoreboardTable.pad(5);
        }

        // Add to table
        scoreboardTable.add(rankLabel).width(80).center().pad(8);
        scoreboardTable.add(usernameLabel).width(150).left().pad(8);
        scoreboardTable.add(scoreLabel).width(100).center().pad(8);
        scoreboardTable.add(killsLabel).width(100).center().pad(8);
        scoreboardTable.add(survivalLabel).width(120).center().pad(8);
        scoreboardTable.add(gamesLabel).width(100).center().pad(8);
        scoreboardTable.row();

        if (rank < 10) {
            Label separator = new Label("", skin);
            separator.setColor(Color.GRAY);
            scoreboardTable.add(separator).colspan(6).fillX().height(1).pad(2);
            scoreboardTable.row();
        }
    }

    private Color getRankColor(int rank) {
        switch (rank) {
            case 1:
                return Color.GOLD;      // Gold for 1st place
            case 2:
                return Color.LIGHT_GRAY;  // Silver for 2nd place
            case 3:
                return new Color(0.8f, 0.5f, 0.2f, 1f); // Bronze for 3rd place
            default:
                return Color.WHITE;
        }
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

    // Getters for controller access
    public TextButton getBackButton() {
        return backButton;
    }

    public TextButton getSortScoreButton() {
        return sortScoreButton;
    }

    public TextButton getSortUsernameButton() {
        return sortUsernameButton;
    }

    public TextButton getSortKillsButton() {
        return sortKillsButton;
    }

    public TextButton getSortSurvivalButton() {
        return sortSurvivalButton;
    }

    public Stage getStage() {
        return stage;
    }
}
