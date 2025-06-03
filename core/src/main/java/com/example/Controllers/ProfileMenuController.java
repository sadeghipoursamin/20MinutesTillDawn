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
        setupButtonHandlers();
    }

    private void setupButtonHandlers() {
        if (view == null) return;

        setupPasswordChangeHandler();
        setupUsernameChangeHandler();
        setupDeleteAccountHandler();
        setupEnhancedAvatarChangeHandler(); // Updated method
        setupBackButtonHandler();
    }

    private void setupPasswordChangeHandler() {
        view.getPassword().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();

                if (App.getCurrentUser() == null) {
                    view.showErrorMessage("No user logged in!");
                    return;
                }

                ResetPasswordWindow resetPasswordWindow = new ResetPasswordWindow(
                    GameAssetManager.getGameAssetManager().getSkin(),
                    App.getCurrentUser()
                );

                resetPasswordWindow.setOnComplete(() -> {
                    view.showSuccessMessage("Password changed successfully!");
                    App.save();
                });

                view.getStage().addActor(resetPasswordWindow);
            }
        });
    }

    private void setupUsernameChangeHandler() {
        view.getUsernameButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();

                if (App.getCurrentUser() == null) {
                    view.showErrorMessage("No user logged in!");
                    return;
                }

                ResetUsernameWindow resetUsernameWindow = new ResetUsernameWindow(
                    GameAssetManager.getGameAssetManager().getSkin(),
                    App.getCurrentUser()
                );

                resetUsernameWindow.setOnComplete(() -> {
                    view.showSuccessMessage("Username changed successfully!");
                    view.refreshAvatarDisplay();
                });

                view.getStage().addActor(resetUsernameWindow);
            }
        });
    }

    private void setupDeleteAccountHandler() {
        view.getDeleteAccountButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();

                if (App.getCurrentUser() == null) {
                    view.showErrorMessage("No user logged in!");
                    return;
                }

                if ("Guest User".equals(App.getCurrentUser().getUsername())) {
                    view.showErrorMessage("Cannot delete guest account!");
                    return;
                }

                DeleteAccountWindow deleteAccountWindow = new DeleteAccountWindow(
                    GameAssetManager.getGameAssetManager().getSkin(),
                    App.getCurrentUser()
                );

                deleteAccountWindow.setOnComplete(() -> {
                    navigateToOpening();
                });

                view.getStage().addActor(deleteAccountWindow);
            }
        });
    }

    // Enhanced avatar change handler with all three features
    private void setupEnhancedAvatarChangeHandler() {
        view.getChooseAvatarButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.playSound();

                if (App.getCurrentUser() == null) {
                    view.showErrorMessage("No user logged in!");
                    return;
                }

                // Create the enhanced avatar selection window
                EnhancedAvatarSelectionWindow avatarWindow = new EnhancedAvatarSelectionWindow(
                    GameAssetManager.getGameAssetManager().getSkin(),
                    App.getCurrentUser(),
                    (selectedAvatarPath) -> handleAvatarSelection(selectedAvatarPath)
                );

                view.getStage().addActor(avatarWindow);
            }
        });
    }

    private void setupBackButtonHandler() {
        if (view.getBackButton() != null) {
            view.getBackButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Main.playSound();
                    navigateToMainMenu();
                }
            });
        }
    }

    private void handleAvatarSelection(String selectedAvatarPath) {
        try {
            // Update the user's avatar
            App.getCurrentUser().setAvatarPath(selectedAvatarPath);

            // Determine if it's a custom avatar
            boolean isCustom = selectedAvatarPath.contains("custom_");
            App.getCurrentUser().setCustomAvatar(isCustom);

            // Save the changes
            App.updateUser(App.getCurrentUser());

            // Refresh the avatar display in the view
            view.refreshAvatarDisplay();

            // Show success message
            view.showSuccessMessage("Avatar updated successfully!");

            System.out.println("Avatar updated for user " + App.getCurrentUser().getUsername() +
                " to: " + selectedAvatarPath + " (Custom: " + isCustom + ")");

        } catch (Exception e) {
            view.showErrorMessage("Failed to update avatar: " + e.getMessage());
            System.err.println("Error updating avatar: " + e.getMessage());
        }
    }

    public void navigateToMainMenu() {
        try {
            Main.getMain().getScreen().dispose();
            MainMenuView mainMenuView = new MainMenuView(
                new MainMenuController(),
                GameAssetManager.getGameAssetManager().getSkin()
            );
            Main.getMain().setScreen(mainMenuView);
        } catch (Exception e) {
            System.err.println("Error navigating to main menu: " + e.getMessage());
        }
    }

    public void navigateToOpening() {
        try {
            Main.getMain().getScreen().dispose();
            OpeningMenuView openingMenuView = new OpeningMenuView(
                new OpeningMenuController(),
                GameAssetManager.getGameAssetManager().getSkin()
            );
            Main.getMain().setScreen(openingMenuView);
        } catch (Exception e) {
            System.err.println("Error navigating to opening menu: " + e.getMessage());
        }
    }

    private boolean isValidUser() {
        return App.getCurrentUser() != null &&
            App.getCurrentUser().getUsername() != null &&
            !App.getCurrentUser().getUsername().isEmpty();
    }

    private void handleProfileError(String operation, Exception e) {
        String errorMessage = "Error during " + operation + ": " + e.getMessage();
        System.err.println(errorMessage);

        if (view != null) {
            view.showErrorMessage("An error occurred. Please try again.");
        }
    }
}
