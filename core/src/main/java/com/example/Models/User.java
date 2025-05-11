package com.example.Models;

public class User {

    private String username;
    private String password;
    private String securityAnswer;
    private int score;

    public User(String username, String password, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityAnswer = securityAnswer;
        this.score = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public int getScore() {
        return score;
    }
}
