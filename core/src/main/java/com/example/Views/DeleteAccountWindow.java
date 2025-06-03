package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.GameAssetManager;

public class DeleteAccountWindow extends Window {
    private Label titleLabel;
    private Label warningLabel;
    private Label instructionLabel;
    private Label passwordLabel;
    private TextField passwordField;
    private Label confirmationLabel;
    private TextField confirmationField;
    private Label errorLabel;
    private TextButton deleteButton;
    private TextButton cancelButton;
    private CheckBox confirmCheckbox;
    private Runnable onComplete;
    private User user;
    private Skin skin;

    public DeleteAccountWindow(Skin skin, User user) {
        super("⚠ DELETE ACCOUNT ⚠", skin);
        this.user = user;

        this.skin = GameAssetManager.getGameAssetManager().getSkin();


        initializeComponents(skin);
        setupLayout();
        setupListeners();

        // Window properties
        this.setSize(600, 650);
        this.setPosition(
            (Gdx.graphics.getWidth() - 600) / 2f,
            (Gdx.graphics.getHeight() - 650) / 2f
        );
        this.setModal(true);
        this.setMovable(true);

        // Set window color to indicate danger
        this.setColor(1f, 0.9f, 0.9f, 1f);
    }

    private void initializeComponents(Skin skin) {
        // Title and warnings
        titleLabel = new Label("DELETE ACCOUNT", skin, "title");
        titleLabel.setColor(Color.RED);

        warningLabel = new Label("⚠ WARNING: This action cannot be undone! ⚠", skin);
        warningLabel.setColor(Color.RED);

        instructionLabel = new Label(
            "Deleting your account will permanently remove:\n" +
                "• All your game statistics\n" +
                "• Your username and profile\n" +
                "• Your avatar and settings\n" +
                "• All saved progress\n\n" +
                "To confirm deletion, please:",
            skin
        );
        instructionLabel.setColor(Color.WHITE);

        // Password verification
        passwordLabel = new Label("Enter your password:", skin);
        passwordLabel.setColor(Color.YELLOW);

        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Your current password...");

        // Confirmation text
        confirmationLabel = new Label("Type 'DELETE' to confirm:", skin);
        confirmationLabel.setColor(Color.YELLOW);

        confirmationField = new TextField("", skin);
        confirmationField.setMessageText("Type DELETE here...");

        // Final confirmation checkbox
        confirmCheckbox = new CheckBox(" I understand this action is permanent and cannot be undone", skin);
        confirmCheckbox.setColor(Color.RED);

        // Error display
        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        // Buttons
        deleteButton = new TextButton("DELETE ACCOUNT PERMANENTLY", skin);
        deleteButton.setColor(Color.RED);
        deleteButton.setDisabled(true);

        cancelButton = new TextButton("Cancel (Keep Account)", skin);
        cancelButton.setColor(Color.GREEN);
    }

    private void setupLayout() {
        // Title
        this.add(titleLabel).colspan(2).center().padBottom(20);
        this.row();

        // Warning
        this.add(warningLabel).colspan(2).center().padBottom(20);
        this.row();

        // Instructions
        this.add(instructionLabel).colspan(2).left().padBottom(25);
        this.row();

        // Password verification
        this.add(passwordLabel).left().padRight(10);
        this.add(passwordField).width(300).height(40);
        this.row().padBottom(15);

        // Confirmation text
        this.add(confirmationLabel).left().padRight(10);
        this.add(confirmationField).width(300).height(40);
        this.row().padBottom(20);

        // Final confirmation checkbox
        this.add(confirmCheckbox).colspan(2).left().padBottom(20);
        this.row();

        // Statistics display (what will be lost)
        if (user != null) {
            Label statsLabel = new Label("You will lose the following data:", skin);
            statsLabel.setColor(Color.ORANGE);
            this.add(statsLabel).colspan(2).left().padBottom(10);
            this.row();

            String statsText = String.format(
                "• Score: %d points\n" +
                    "• Total Kills: %d\n" +
                    "• Games Played: %d\n" +
                    "• Total Play Time: %s",
                user.getScore(),
                user.getTotalKills(),
                user.getGamesPlayed(),
                user.getFormattedTotalPlayTime()
            );

            Label userStatsLabel = new Label(statsText, skin);
            userStatsLabel.setColor(Color.LIGHT_GRAY);
            this.add(userStatsLabel).colspan(2).left().padBottom(20);
            this.row();
        }

        // Error display
        this.add(errorLabel).colspan(2).center().padBottom(15);
        this.row();

        // Buttons
        Table buttonTable = new Table();
        buttonTable.add(cancelButton).width(200).height(60).pad(10);
        buttonTable.add(deleteButton).width(250).height(60).pad(10);

        this.add(buttonTable).colspan(2).center();
    }

    private void setupListeners() {
        // Real-time validation as user types
        passwordField.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        validateForm();
                    }
                }, 0.1f);
                return false;
            }
        });

        confirmationField.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        validateForm();
                    }
                }, 0.1f);
                return false;
            }
        });

        confirmCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                validateForm();
            }
        });

        // Delete button
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                handleAccountDeletion();
            }
        });

        // Cancel button
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                remove();
            }
        });
    }

    private void validateForm() {
        String password = passwordField.getText();
        String confirmation = confirmationField.getText();
        boolean checkboxChecked = confirmCheckbox.isChecked();

        boolean isValid = !password.isEmpty() &&
            "DELETE".equals(confirmation) &&
            checkboxChecked;

        deleteButton.setDisabled(!isValid);

        // Update UI feedback
        if (confirmation.length() > 0 && !"DELETE".equals(confirmation)) {
            confirmationField.setColor(Color.RED);
        } else if ("DELETE".equals(confirmation)) {
            confirmationField.setColor(Color.GREEN);
        } else {
            confirmationField.setColor(Color.WHITE);
        }
    }

    private void handleAccountDeletion() {
        String enteredPassword = passwordField.getText();

        // Verify password
        if (!user.getPassword().equals(enteredPassword)) {
            showError("Incorrect password!");
            return;
        }

        // Final confirmation dialog
        createFinalConfirmationDialog();
    }

    private void createFinalConfirmationDialog() {
        Dialog finalConfirmDialog = new Dialog("FINAL CONFIRMATION",
            com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin()) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    performAccountDeletion();
                }
            }
        };

        finalConfirmDialog.text(
            "This is your LAST CHANCE!\n\n" +
                "Are you absolutely sure you want to\n" +
                "permanently delete your account?\n\n" +
                "Username: " + user.getUsername()
        );

        finalConfirmDialog.button("YES, DELETE FOREVER", true).getButtonTable().getCells().first().getActor().setColor(Color.RED);
        finalConfirmDialog.button("NO, KEEP ACCOUNT", false).getButtonTable().getCells().get(1).getActor().setColor(Color.GREEN);

        finalConfirmDialog.show(this.getStage());
    }

    private void performAccountDeletion() {
        try {
            String deletedUsername = user.getUsername();

            // Remove user from storage
            App.removeUser(user.getUsername());

            // Clear current user if it was the deleted one
            if (App.getCurrentUser() != null && App.getCurrentUser().getUsername().equals(user.getUsername())) {
                App.setCurrentUser(null);
            }

            // Show success message
            showSuccess("Account '" + deletedUsername + "' has been permanently deleted.");

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
            showError("An error occurred while deleting account: " + e.getMessage());
            System.err.println("Error deleting account: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText("❌ " + message);
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
        errorLabel.setText("✅ " + message);
        errorLabel.setColor(Color.GREEN);
    }

    // Getters for backward compatibility
    public TextField getPassword() {
        return passwordField;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
}
