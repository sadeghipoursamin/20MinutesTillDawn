package com.example.Models;

import com.example.Models.enums.Ability;
import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;
import com.example.Models.utilities.AvatarManager;

public class User {

    private String username;
    private String password;
    private String securityAnswer;
    private WeaponType weaponType;
    private Hero hero;
    private Ability ability;
    private int score;

    // Avatar properties
    private String avatarPath;
    private boolean isCustomAvatar;

    // Statistics
    private int totalKills;
    private long longestSurvivalTime; // in seconds
    private int gamesPlayed;
    private long totalPlayTime; // in seconds

    public User(String username, String password, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityAnswer = securityAnswer;
        this.score = 0;
        this.weaponType = null;
        this.hero = null;
        this.ability = null;

        // Initialize avatar with default
        this.avatarPath = null; // Will be set to default when needed
        this.isCustomAvatar = false;

        // Initialize statistics
        this.totalKills = 0;
        this.longestSurvivalTime = 0;
        this.gamesPlayed = 0;
        this.totalPlayTime = 0;
    }

    // Avatar methods
    public String getAvatarPath() {
        // Return default avatar if none is set
        if (avatarPath == null || avatarPath.isEmpty()) {
            return AvatarManager.getInstance().getDefaultAvatarPath();
        }
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public boolean isCustomAvatar() {
        return isCustomAvatar;
    }

    public void setCustomAvatar(boolean customAvatar) {
        isCustomAvatar = customAvatar;
    }

    public String getAvatarDisplayName() {
        return AvatarManager.getInstance().getAvatarDisplayName(getAvatarPath());
    }

    // Existing getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public void addKills(int kills) {
        this.totalKills += kills;
    }

    public long getLongestSurvivalTime() {
        return longestSurvivalTime;
    }

    public void setLongestSurvivalTime(long longestSurvivalTime) {
        this.longestSurvivalTime = longestSurvivalTime;
    }

    public void updateSurvivalTime(long survivalTime) {
        if (survivalTime > this.longestSurvivalTime) {
            this.longestSurvivalTime = survivalTime;
        }
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public void addPlayTime(long playTime) {
        this.totalPlayTime += playTime;
    }

    public String getFormattedSurvivalTime() {
        long minutes = longestSurvivalTime / 60;
        long seconds = longestSurvivalTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getFormattedTotalPlayTime() {
        long hours = totalPlayTime / 3600;
        long minutes = (totalPlayTime % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public void updateGameStats(int kills, long survivalTime, int scoreGained) {
        this.addKills(kills);
        this.updateSurvivalTime(survivalTime);
        this.addPlayTime(survivalTime);
        this.setScore(this.score + scoreGained);
        this.incrementGamesPlayed();
    }

    public boolean hasValidAvatar() {
        return avatarPath != null && !avatarPath.isEmpty() &&
            AvatarManager.getInstance().isValidAvatar(avatarPath);
    }

    public void resetToDefaultAvatar() {
        this.avatarPath = AvatarManager.getInstance().getDefaultAvatarPath();
        this.isCustomAvatar = false;
    }

    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", score=" + score +
            ", avatarPath='" + avatarPath + '\'' +
            ", isCustomAvatar=" + isCustomAvatar +
            ", totalKills=" + totalKills +
            ", gamesPlayed=" + gamesPlayed +
            '}';
    }
}
