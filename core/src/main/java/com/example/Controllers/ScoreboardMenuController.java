package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.MainMenuView;
import com.example.Views.ScoreboardMenuView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardMenuController {
    private ScoreboardMenuView view;
    private SortType currentSortType = SortType.SCORE;
    private boolean isAscending = false; // Default to descending for better scores first

    public static void updateUserStats(User user, int kills, long survivalTimeSeconds, int finalScore) {
        if (user != null && !user.getUsername().equals("Guest User")) {
            int scoreGained = calculateScore(kills, survivalTimeSeconds, finalScore);

            user.updateGameStats(kills, survivalTimeSeconds, scoreGained);
            App.save();
        }
    }

    private static int calculateScore(int kills, long survivalTime, int finalScore) {
        int killBonus = kills * 10;
        int survivalBonus = (int) (survivalTime * 2);
        return finalScore + killBonus + survivalBonus;
    }

    public void setView(ScoreboardMenuView view) {
        this.view = view;

        if (App.getSettings().isGrayscaleEnabled()) {
            Main.getBatch().setShader(GrayscaleShader.getShader());
        } else {
            Main.getBatch().setShader(null);
        }

        setupButtonListeners();

        if (view != null) {
            updateScoreboard();
        }
    }

    private void setupButtonListeners() {
        if (view == null) return;

        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                navigateToMainMenu();
            }
        });

        view.getSortScoreButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                sortBy(SortType.SCORE);
            }
        });

        view.getSortUsernameButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                sortBy(SortType.USERNAME);
            }
        });

        view.getSortKillsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                sortBy(SortType.KILLS);
            }
        });

        view.getSortSurvivalButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                sortBy(SortType.SURVIVAL_TIME);
            }
        });
    }

    private void sortBy(SortType sortType) {
        if (currentSortType == sortType) {
            isAscending = !isAscending;
        } else {
            currentSortType = sortType;
            isAscending = (sortType == SortType.USERNAME);
        }

        updateScoreboard();
        updateSortButtonTexts();
    }

    private void updateSortButtonTexts() {
        String ascDesc = isAscending ? " ↑" : " ↓";

        view.getSortScoreButton().setText("Score" +
            (currentSortType == SortType.SCORE ? ascDesc : ""));
        view.getSortUsernameButton().setText("Name" +
            (currentSortType == SortType.USERNAME ? ascDesc : ""));
        view.getSortKillsButton().setText("Kills" +
            (currentSortType == SortType.KILLS ? ascDesc : ""));
        view.getSortSurvivalButton().setText("Survival" +
            (currentSortType == SortType.SURVIVAL_TIME ? ascDesc : ""));
    }

    public void updateScoreboard() {
        List<User> sortedUsers = getSortedUsers();
        view.updateScoreboardData(sortedUsers, currentSortType, isAscending);
    }

    private List<User> getSortedUsers() {
        List<User> users = new ArrayList<>(App.getUsers().values());


        Comparator<User> comparator = getComparator();
        if (!isAscending) {
            comparator = comparator.reversed();
        }

        users.sort(comparator);

        return users.subList(0, Math.min(10, users.size()));
    }

    private Comparator<User> getComparator() {
        switch (currentSortType) {
            case SCORE:
                return Comparator.comparingInt(User::getScore);
            case USERNAME:
                return Comparator.comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER);
            case KILLS:
                return Comparator.comparingInt(User::getTotalKills);
            case SURVIVAL_TIME:
                return Comparator.comparingLong(User::getLongestSurvivalTime);
            default:
                return Comparator.comparingInt(User::getScore);
        }
    }

    public boolean isCurrentUser(User user) {
        return App.getCurrentUser() != null &&
            App.getCurrentUser().getUsername().equals(user.getUsername());
    }

    private void navigateToMainMenu() {
        Main.getMain().getScreen().dispose();
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(),
            GameAssetManager.getGameAssetManager().getSkin()));
    }

    public enum SortType {
        SCORE, USERNAME, KILLS, SURVIVAL_TIME
    }
}
