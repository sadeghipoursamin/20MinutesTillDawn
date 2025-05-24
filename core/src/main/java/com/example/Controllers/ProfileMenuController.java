package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.utilities.GameAssetManager;
import com.example.Views.*;

public class ProfileMenuController {
    private ProfileMenuView view;

    public void setView(ProfileMenuView view) {
        this.view = view;
        handleProfileMenuButtons();
    }

    public void handleProfileMenuButtons() {
        view.getPassword().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();
                ResetPasswordWindow resetPasswordWindow = new ResetPasswordWindow(GameAssetManager.getGameAssetManager().getSkin(), App.getCurrentUser());
                resetPasswordWindow.setOnComplete(() -> {
                    navigateToMainMenu();
                    App.getCurrentUser().setPassword(resetPasswordWindow.getNewPassword().getText());
                });
                view.getStage().addActor(resetPasswordWindow);
            }
        });

        view.getUsernameButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();
                ResetUsernameWindow resetUsernameWindow = new ResetUsernameWindow(GameAssetManager.getGameAssetManager().getSkin(), App.getCurrentUser());
                resetUsernameWindow.setOnComplete(() -> {
                    navigateToMainMenu();
                    App.getCurrentUser().setUsername(resetUsernameWindow.getNewUsername().getText());
                });
                view.getStage().addActor(resetUsernameWindow);
            }
        });

        view.getDeleteAccountButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();
                DeleteAccountWindow deleteAccountWindow = new DeleteAccountWindow(GameAssetManager.getGameAssetManager().getSkin(), App.getCurrentUser());
                deleteAccountWindow.setOnComplete(() -> {
                    navigateToOpening();
                    App.getUsers().remove(App.getCurrentUser().getUsername());
                    App.setCurrentUser(null);
                    App.save();
                });
                view.getStage().addActor(deleteAccountWindow);
            }
        });

        view.getChooseAvatarButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.playSound();
                EnhancedAvatarSelectionWindow avatarWindow = new EnhancedAvatarSelectionWindow(
                    GameAssetManager.getGameAssetManager().getSkin(),
                    App.getCurrentUser(),
                    (selectedAvatarPath) -> {
                        // Update the user's avatar
                        App.getCurrentUser().setAvatarPath(selectedAvatarPath);
                        App.getCurrentUser().setCustomAvatar(!selectedAvatarPath.contains("predefined"));

                        // Save the changes
                        App.save();

                        // Refresh the avatar display
                        if (view != null) {
                            view.refreshAvatarDisplay();
                        }

                        // Show success message
                        view.getErrorLabel().setText("Avatar updated successfully!");
                        view.getErrorLabel().setColor(com.badlogic.gdx.graphics.Color.GREEN);

                        // Clear message after 3 seconds
                        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                            @Override
                            public void run() {
                                if (view != null) {
                                    view.getErrorLabel().setText("");
                                }
                            }
                        }, 3.0f);
                    }
                );
                view.getStage().addActor(avatarWindow);
            }
        });
    }

    public void navigateToMainMenu() {
        Main.getMain().getScreen().dispose();
        MainMenuView mainMenuView = new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin());
        Main.getMain().setScreen(mainMenuView);
        mainMenuView.getErrorLabel().setText("Reset Password successfully!");
    }

    public void navigateToOpening() {
        Main.getMain().getScreen().dispose();
        OpeningMenuView openingMenuView = new OpeningMenuView(new OpeningMenuController(), GameAssetManager.getGameAssetManager().getSkin());
        Main.getMain().setScreen(openingMenuView);
    }
}
