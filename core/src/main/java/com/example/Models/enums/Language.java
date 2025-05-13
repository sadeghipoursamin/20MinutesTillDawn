package com.example.Models.enums;

import com.example.Models.App;

import java.util.Objects;

public enum Language {
    Login("Login", "S'indentifier"),
    QUIT("Exit", "Sortir");


    private final String english;
    private final String french;

    Language(String english, String french) {
        this.english = english;
        this.french = french;
    }

    public String getText() {
        return Objects.equals(App.getLanguage(), "en") ? english : french;
    }
}
