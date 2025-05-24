package com.example.Controllers;

import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.MainMenuView;
import com.example.Views.ScoreboardMenuView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreboardMenuController {
    private ScoreboardMenuView view;
    private SortType currentSortType = SortType.SCORE;
    private boolean isAscending = false; // Default to descending for better scores first

    // Method to be called when a game ends
    public static void updateUserStats(User user, int kills, long survivalTimeSeconds, int finalScore) {
        if (user != null && !user.getUsername().equals("Guest User")) {
            // Calculate score based on performance
            int scoreGained = calculateScore(kills, survivalTimeSeconds, finalScore);

            user.updateGameStats(kills, survivalTimeSeconds, scoreGained);
            App.save(); // Save the updated user data
        }
    }

    private static int calculateScore(int kills, long survivalTime, int finalScore) {
        // Score calculation: base score + kill bonus + survival bonus
        int killBonus = kills * 10;
        int survivalBonus = (int) (survivalTime * 2); // 2 points per second survived
        return finalScore + killBonus + survivalBonus;
    }

    public void setView(ScoreboardMenuView view) {
        this.view = view;

        // Apply grayscale if enabled
        if (App.getSettings().isGrayscaleEnabled()) {
            Main.getBatch().setShader(GrayscaleShader.getShader());
        } else {
            Main.getBatch().setShader(null);
        }

        // Initialize with default sorting
        updateScoreboard();
    }

    public void handleScoreboardButtons() {
        if (view != null) {
            if (view.getBackButton().isChecked()) {
                Main.playSound();
                navigateToMainMenu();
            }

            if (view.getSortScoreButton().isChecked()) {
                Main.playSound();
                sortBy(SortType.SCORE);
            }

            if (view.getSortUsernameButton().isChecked()) {
                Main.playSound();
                sortBy(SortType.USERNAME);
            }

            if (view.getSortKillsButton().isChecked()) {
                Main.playSound();
                sortBy(SortType.KILLS);
            }

            if (view.getSortSurvivalButton().isChecked()) {
                Main.playSound();
                sortBy(SortType.SURVIVAL_TIME);
            }
        }
    }

    private void sortBy(SortType sortType) {
        // Toggle ascending/descending if same sort type is clicked
        if (currentSortType == sortType) {
            isAscending = !isAscending;
        } else {
            currentSortType = sortType;
            // Default to descending for scores/kills/survival, ascending for username
            isAscending = (sortType == SortType.USERNAME);
        }

        updateScoreboard();
        updateSortButtonTexts();
    }

    private void updateSortButtonTexts() {
        String ascDesc = isAscending ? " ↑" : " ↓";

        view.getSortScoreButton().setText("Sort by Score" +
            (currentSortType == SortType.SCORE ? ascDesc : ""));
        view.getSortUsernameButton().setText("Sort by Name" +
            (currentSortType == SortType.USERNAME ? ascDesc : ""));
        view.getSortKillsButton().setText("Sort by Kills" +
            (currentSortType == SortType.KILLS ? ascDesc : ""));
        view.getSortSurvivalButton().setText("Sort by Survival" +
            (currentSortType == SortType.SURVIVAL_TIME ? ascDesc : ""));
    }

    public void updateScoreboard() {
        List<User> sortedUsers = getSortedUsers();
        view.updateScoreboardData(sortedUsers, currentSortType, isAscending);
    }

    private List<User> getSortedUsers() {
        List<User> users = new ArrayList<>(App.getUsers().values());

        // Remove users with no game data (score = 0 and no kills)
        users.removeIf(user -> user.getScore() == 0 && user.getTotalKills() == 0 && user.getGamesPlayed() == 0);

        Comparator<User> comparator = getComparator();
        if (!isAscending) {
            comparator = comparator.reversed();
        }

        Collections.sort(users, comparator);

        // Return top 10 users
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
