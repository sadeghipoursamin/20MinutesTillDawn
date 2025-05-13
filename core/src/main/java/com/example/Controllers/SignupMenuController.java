package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.MainMenuView;
import com.example.Views.SignupMenuView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupMenuController {

    private SignupMenuView view;
    public void setView(SignupMenuView view) {
        this.view = view;
        checkSignup();
    }


    public void checkSignup(){
        view.getSignupButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();
                String enteredUsername = view.getUsername().getText();
                String enteredPassword = view.getPassword().getText();
                String enteredSecurityQuestion = view.getSecurityQuestion().getText();

                if (enteredUsername.isEmpty()) {
                    view.getErrorLabel().setText("Username is empty you idiot!");
                    return;
                }

                if(enteredPassword.isEmpty() || !isValidPass(enteredPassword)){
                    view.getErrorLabel().setText("check your password!");
                    return;
                }

                if (enteredSecurityQuestion.isEmpty()){
                    view.getErrorLabel().setText("Fill the question!");
                    return;
                }

                User newUser = new User(enteredUsername, enteredPassword, enteredSecurityQuestion);
                App.addUser(newUser);
                App.initialize();
                App.save();
                view.getErrorLabel().setText("Signed up successfully!");
                navigateToMainMenu(newUser);
            }
        });

        view.getGuestButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();
                User guestUser = new User("Guest User", "", "");
                navigateToMainMenu(guestUser);
                App.initialize();
            }
        });

    }

    public boolean isValidPass(String password){
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&*()_]).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void navigateToMainMenu(User user) {
        Main.getMain().getScreen().dispose();
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

}
