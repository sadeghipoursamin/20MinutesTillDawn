package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.enums.Language;

public class ResetUsernameWindow extends Window {
    private TextField newUsernameField;
    private Label newUsernameLabel;
    private Label instructionLabel;
    private Label errorLabel;
    private TextButton confirmButton;
    private TextButton cancelButton;
    private Runnable onComplete;
    private User user;

    public ResetUsernameWindow(Skin skin, User user) {
        super(Language.ResetUsernameTitle.getText(), skin);
        this.user = user;

        initializeComponents(skin);
        setupLayout();
        setupListeners();

        // Window properties
        this.setSize(500, 400);
        this.setPosition(
            (Gdx.graphics.getWidth() - 500) / 2f,
            (Gdx.graphics.getHeight() - 400) / 2f
        );
        this.setModal(true);
        this.setMovable(true);
    }

    private void initializeComponents(Skin skin) {
        // Labels
        instructionLabel = new Label("Enter your new username below:", skin);
        instructionLabel.setColor(Color.WHITE);

        newUsernameLabel = new Label(Language.NewUsernameLabel.getText(), skin);
        newUsernameLabel.setColor(Color.CYAN);

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        // Text field
        newUsernameField = new TextField("", skin);
        newUsernameField.setMessageText("Enter new username...");

        // Buttons
        confirmButton = new TextButton("Confirm Change", skin);
        confirmButton.setColor(Color.GREEN);

        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setColor(Color.RED);
    }

    private void setupLayout() {
        // Current username display
        Label currentUsernameLabel = new Label("Current Username: " + user.getUsername(),
            com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin());
        currentUsernameLabel.setColor(Color.YELLOW);

        this.add(currentUsernameLabel).colspan(2).center().padBottom(20);
        this.row();

        // Instructions
        this.add(instructionLabel).colspan(2).center().padBottom(15);
        this.row();

        // New username input
        this.add(newUsernameLabel).left().padRight(10);
        this.add(newUsernameField).width(250).height(40);
        this.row().padBottom(15);

        // Error display
        this.add(errorLabel).colspan(2).center().padBottom(20);
        this.row();

        // Buttons
        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(120).height(50).pad(10);
        buttonTable.add(cancelButton).width(120).height(50).pad(10);

        this.add(buttonTable).colspan(2).center();
    }

    private void setupListeners() {
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                handleUsernameChange();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                remove();
            }
        });

        // Allow Enter key to confirm
        newUsernameField.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ENTER) {
                    handleUsernameChange();
                    return true;
                }
                return false;
            }
        });
    }

    private void handleUsernameChange() {
        String newUsername = newUsernameField.getText().trim();

        // Validation
        if (newUsername.isEmpty()) {
            showError("Username cannot be empty!");
            return;
        }

        if (newUsername.length() < 3) {
            showError("Username must be at least 3 characters long!");
            return;
        }

        if (newUsername.length() > 20) {
            showError("Username cannot be longer than 20 characters!");
            return;
        }

        if (newUsername.equals(user.getUsername())) {
            showError("New username must be different from current username!");
            return;
        }

        // Check for special characters
        if (!newUsername.matches("^[a-zA-Z0-9_-]+$")) {
            showError("Username can only contain letters, numbers, underscores, and hyphens!");
            return;
        }

        // Check if username already exists
        if (App.findUserByUsername(newUsername) != null) {
            showError("Username '" + newUsername + "' is already taken!");
            return;
        }

        // If all validations pass, proceed with username change
        confirmUsernameChange(newUsername);
    }

    private void confirmUsernameChange(String newUsername) {
        // Create confirmation dialog
        Dialog confirmDialog = new Dialog("Confirm Username Change",
            com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin()) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    // User confirmed the change
                    performUsernameChange(newUsername);
                }
            }
        };

        confirmDialog.text("Are you sure you want to change your username to:\n'" + newUsername + "'?");
        confirmDialog.button("Yes", true);
        confirmDialog.button("No", false);
        confirmDialog.show(this.getStage());
    }

    private void performUsernameChange(String newUsername) {
        try {
            // Remove old username from users map
            App.getUsers().remove(user.getUsername());

            // Update username
            String oldUsername = user.getUsername();
            user.setUsername(newUsername);

            // Add with new username
            App.getUsers().put(newUsername, user);

            // Save changes
            App.save();

            // Show success message
            showSuccess("Username successfully changed from '" + oldUsername + "' to '" + newUsername + "'!");

            // Complete the operation
            if (onComplete != null) {
                onComplete.run();
            }

            // Close window after a short delay
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    remove();
                }
            }, 2.0f);

        } catch (Exception e) {
            showError("An error occurred while changing username: " + e.getMessage());
            System.err.println("Error changing username: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setColor(Color.RED);

        // Clear error after 5 seconds
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                errorLabel.setText("");
            }
        }, 5.0f);
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setColor(Color.GREEN);
    }

    // Getters
    public TextField getNewUsername() {
        return newUsernameField;
    }

    public Label getNewUsernameLabel() {
        return newUsernameLabel;
    }

    public TextButton getResetButton() {
        return confirmButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public Runnable getOnComplete() {
        return onComplete;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
}
