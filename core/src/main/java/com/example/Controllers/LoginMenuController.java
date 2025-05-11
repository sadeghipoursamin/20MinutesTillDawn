package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Views.LoginMenuView;

public class LoginMenuController {
    private LoginMenuView view;

    public void setView(LoginMenuView view) {
        this.view = view;
    }

    public void handleLogin() {
        view.getLoginButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                String enteredUsername = view.getUsername().getText();
                String enteredPassword = view.getPassword().getText();

                if (enteredUsername.isEmpty()) {
                    view.getErrorLabel().setText("Username is empty you idiot!");
                    return;
                }

                if(enteredPassword.isEmpty()){
                    view.getErrorLabel().setText("Password is empty!");
                    return;
                }

                User loggedInUser = App.findUserByUsername(enteredUsername);
                if (loggedInUser == null) {
                    view.getErrorLabel().setText("Username not found!");
                    return;
                }

                App.setCurrentUser(loggedInUser);

                if(!loggedInUser.getPassword().equals(enteredPassword)){
                    view.getErrorLabel().setText("Wrong password!");
                }
                if(loggedInUser.getPassword().equals(enteredPassword)) {
                    view.getErrorLabel().setText("Logged in successfully!");
                }
            }
        });

        view.getForgotPassword().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                String enteredUsername = view.getUsername().getText();

                if (enteredUsername.isEmpty()) {
                    view.getErrorLabel().setText("Username is empty you idiot!");
                    return;
                }


                User loggedInUser = App.findUserByUsername(enteredUsername);
                if (loggedInUser == null) {
                    view.getErrorLabel().setText("Username not found!");
                    return;
                }

            }
        });
    }
}
