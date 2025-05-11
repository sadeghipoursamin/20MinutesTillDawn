package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.ForgotPassWindow;
import com.example.Views.LoginMenuView;
import com.example.Views.MainMenuView;

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
                navigateToMainMenu();
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
                ForgotPassWindow forgotPassWindow = new ForgotPassWindow(GameAssetManager.getGameAssetManager().getSkin(), loggedInUser);
                forgotPassWindow.setOnComplete(() -> {
                    navigateToLoginMenu(loggedInUser);
                });
                view.getStage().addActor(forgotPassWindow);

            }

            public void navigateToLoginMenu(User logedInUser) {
                Main.getMain().getScreen().dispose();
                LoginMenuView loginMenuView = new LoginMenuView(new LoginMenuController(), GameAssetManager.getGameAssetManager().getSkin());
                Main.getMain().setScreen(loginMenuView);
                loginMenuView.getErrorLabel().setText(logedInUser.getPassword());
            }
        });
    }

    public void navigateToMainMenu() {
        Main.getMain().getScreen().dispose();
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }
}
