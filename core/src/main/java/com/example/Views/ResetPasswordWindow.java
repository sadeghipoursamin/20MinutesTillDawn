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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordWindow extends Window {
    private TextField currentPasswordField;
    private TextField newPasswordField;
    private TextField confirmPasswordField;
    private Label currentPasswordLabel;
    private Label newPasswordLabel;
    private Label confirmPasswordLabel;
    private Label instructionLabel;
    private Label errorLabel;
    private Label strengthLabel;
    private ProgressBar strengthBar;
    private TextButton confirmButton;
    private TextButton cancelButton;
    private CheckBox showPasswordsCheckbox;
    private Runnable onComplete;
    private User user;

    public ResetPasswordWindow(Skin skin, User user) {
        super(Language.ResetPasswordTitle.getText(), skin);
        this.user = user;

        initializeComponents(skin);
        setupLayout();
        setupListeners();

        // Window properties
        this.setSize(600, 600);
        this.setPosition(
            (Gdx.graphics.getWidth() - 600) / 2f,
            (Gdx.graphics.getHeight() - 600) / 2f
        );
        this.setModal(true);
        this.setMovable(true);
    }

    private void initializeComponents(Skin skin) {
        // Labels
        instructionLabel = new Label("Enter your current password and choose a new one:", skin);
        instructionLabel.setColor(Color.WHITE);

        currentPasswordLabel = new Label("Current Password:", skin);
        currentPasswordLabel.setColor(Color.CYAN);

        newPasswordLabel = new Label(Language.NewPasswordLabel.getText(), skin);
        newPasswordLabel.setColor(Color.CYAN);

        confirmPasswordLabel = new Label(Language.RepeatPasswordLabel.getText(), skin);
        confirmPasswordLabel.setColor(Color.CYAN);

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        strengthLabel = new Label("Password Strength:", skin);
        strengthLabel.setColor(Color.YELLOW);

        // Text fields
        currentPasswordField = new TextField("", skin);
        currentPasswordField.setPasswordMode(true);
        currentPasswordField.setPasswordCharacter('*');
        currentPasswordField.setMessageText("Enter current password...");

        newPasswordField = new TextField("", skin);
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');
        newPasswordField.setMessageText("Enter new password...");

        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        confirmPasswordField.setMessageText("Confirm new password...");

        // Password strength indicator
        strengthBar = new ProgressBar(0, 100, 1, false, skin);
        strengthBar.setValue(0);

        // Checkbox for showing passwords
        showPasswordsCheckbox = new CheckBox(" Show Passwords", skin);
        showPasswordsCheckbox.setColor(Color.LIGHT_GRAY);

        // Buttons
        confirmButton = new TextButton("Change Password", skin);
        confirmButton.setColor(Color.GREEN);
        confirmButton.setDisabled(true);

        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setColor(Color.RED);
    }

    private void setupLayout() {
        // Instructions
        this.add(instructionLabel).colspan(2).center().padBottom(20);
        this.row();

        // Current password
        this.add(currentPasswordLabel).left().padRight(10);
        this.add(currentPasswordField).width(300).height(40);
        this.row().padBottom(15);

        // New password
        this.add(newPasswordLabel).left().padRight(10);
        this.add(newPasswordField).width(300).height(40);
        this.row().padBottom(10);

        // Password strength indicator
        this.add(strengthLabel).left().padRight(10);
        this.add(strengthBar).width(300).height(20);
        this.row().padBottom(15);

        // Confirm password
        this.add(confirmPasswordLabel).left().padRight(10);
        this.add(confirmPasswordField).width(300).height(40);
        this.row().padBottom(15);

        // Show passwords checkbox
        this.add(showPasswordsCheckbox).colspan(2).left().padBottom(15);
        this.row();

        // Password requirements
        Label requirementsLabel = new Label("Password Requirements:",
            com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin());
        requirementsLabel.setColor(Color.YELLOW);
        this.add(requirementsLabel).colspan(2).left().padBottom(5);
        this.row();

        Label requirementsText = new Label(
            "• At least 8 characters\n" +
                "• At least one uppercase letter\n" +
                "• At least one number\n" +
                "• At least one special character (@#$%&*()_)",
            com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin()
        );
        requirementsText.setColor(Color.LIGHT_GRAY);
        this.add(requirementsText).colspan(2).left().padBottom(15);
        this.row();

        // Error display
        this.add(errorLabel).colspan(2).center().padBottom(20);
        this.row();

        // Buttons
        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(150).height(50).pad(10);
        buttonTable.add(cancelButton).width(150).height(50).pad(10);

        this.add(buttonTable).colspan(2).center();
    }

    private void setupListeners() {
        // Password field listeners for real-time validation
        newPasswordField.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                // Use timer to update after text has changed
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        updatePasswordStrength();
                        validateForm();
                    }
                }, 0.1f);
                return false;
            }
        });

        confirmPasswordField.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
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

        currentPasswordField.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
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

        // Show passwords checkbox
        showPasswordsCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean showPasswords = showPasswordsCheckbox.isChecked();
                currentPasswordField.setPasswordMode(!showPasswords);
                newPasswordField.setPasswordMode(!showPasswords);
                confirmPasswordField.setPasswordMode(!showPasswords);
            }
        });

        // Confirm button
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                handlePasswordChange();
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

        // Allow Enter key to confirm
        confirmPasswordField.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ENTER && !confirmButton.isDisabled()) {
                    handlePasswordChange();
                    return true;
                }
                return false;
            }
        });
    }

    private void updatePasswordStrength() {
        String password = newPasswordField.getText();
        int strength = calculatePasswordStrength(password);

        strengthBar.setValue(strength);

        // Update color based on strength
        Color strengthColor;
        if (strength < 30) {
            strengthColor = Color.RED;
        } else if (strength < 60) {
            strengthColor = Color.YELLOW;
        } else if (strength < 80) {
            strengthColor = Color.ORANGE;
        } else {
            strengthColor = Color.GREEN;
        }

        strengthBar.setColor(strengthColor);
    }

    private int calculatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int strength = 0;

        // Length bonus
        if (password.length() >= 8) strength += 25;
        if (password.length() >= 12) strength += 15;
        if (password.length() >= 16) strength += 10;

        // Character variety
        if (password.matches(".*[a-z].*")) strength += 10; // lowercase
        if (password.matches(".*[A-Z].*")) strength += 15; // uppercase
        if (password.matches(".*[0-9].*")) strength += 15; // numbers
        if (password.matches(".*[@#$%&*()_].*")) strength += 20; // special chars

        return Math.min(100, strength);
    }

    private void validateForm() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        boolean isValid = true;

        // Check if all fields are filled
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            isValid = false;
        }

        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            isValid = false;
        }

        // Check password strength
        if (!isValidPassword(newPassword)) {
            isValid = false;
        }

        confirmButton.setDisabled(!isValid);
    }

    private void handlePasswordChange() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate current password
        if (!user.getPassword().equals(currentPassword)) {
            showError("Current password is incorrect!");
            return;
        }

        // Validate new password
        if (!isValidPassword(newPassword)) {
            showError("New password does not meet requirements!");
            return;
        }

        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            showError("New passwords do not match!");
            return;
        }

        // Check if new password is different from current
        if (newPassword.equals(currentPassword)) {
            showError("New password must be different from current password!");
            return;
        }

        // If all validations pass, proceed with password change
        confirmPasswordChange(newPassword);
    }

    private void confirmPasswordChange(String newPassword) {
        // Create confirmation dialog
        Dialog confirmDialog = new Dialog("Confirm Password Change",
            com.example.Models.utilities.GameAssetManager.getGameAssetManager().getSkin()) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    // User confirmed the change
                    performPasswordChange(newPassword);
                }
            }
        };

        confirmDialog.text("Are you sure you want to change your password?");
        confirmDialog.button("Yes", true);
        confirmDialog.button("No", false);
        confirmDialog.show(this.getStage());
    }

    private void performPasswordChange(String newPassword) {
        try {
            // Update password
            user.setPassword(newPassword);

            // Save changes
            App.updateUser(user);

            // Show success message
            showSuccess("Password successfully changed!");

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
            showError("An error occurred while changing password: " + e.getMessage());
            System.err.println("Error changing password: " + e.getMessage());
        }
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&*()_]).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
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

    // Getters for backward compatibility
    public TextField getNewPassword() {
        return newPasswordField;
    }

    public Label getNewPasswordLabel() {
        return newPasswordLabel;
    }

    public TextField getRepeatPassword() {
        return confirmPasswordField;
    }

    public Label getRepeatPasswordLabel() {
        return confirmPasswordLabel;
    }

    public TextButton getReset() {
        return confirmButton;
    }

    public Runnable getOnComplete() {
        return onComplete;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
}
