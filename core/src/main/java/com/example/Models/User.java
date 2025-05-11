package com.example.Models;

import com.example.Models.enums.Ability;
import com.example.Models.enums.Hero;
import com.example.Models.enums.Weapon;

public class User {

    private String username;
    private String password;
    private String securityAnswer;
    private Weapon weapon;
    private Hero hero;
    private Ability ability;
    private int score;

    public User(String username, String password, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityAnswer = securityAnswer;
        this.score = 0;
        this.weapon =null;
        this.hero = null;
        this.ability = null;
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

    public void setScore(int score) {
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
