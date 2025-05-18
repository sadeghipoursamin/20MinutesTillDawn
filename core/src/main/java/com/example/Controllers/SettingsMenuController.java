package com.example.Controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.Settings;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.MainMenuView;
import com.example.Views.SettingsMenuView;

public class SettingsMenuController {
    private SettingsMenuView view;
    private Settings settings;

    public void setView(SettingsMenuView view) {
        this.view = view;
        this.settings = App.getSettings();
        initializeUIWithCurrentSettings();
        handleSettingsControls();
    }

    private void initializeUIWithCurrentSettings() {
        // Set initial values for UI components based on current settings
        view.getMusicVolumeSlider().setValue(settings.getMusicVolume());
        view.getSfxToggle().setChecked(settings.isSfxEnabled());
        view.getAutoReloadToggle().setChecked(settings.isAutoReloadEnabled());
        view.getGrayscaleToggle().setChecked(settings.isGrayscaleEnabled());

        // Set the initial key binding labels
        updateKeyBindingLabels();
    }

    private void updateKeyBindingLabels() {
        view.getMoveUpKeyLabel().setText(Input.Keys.toString(settings.getMoveUpKey()));
        view.getMoveDownKeyLabel().setText(Input.Keys.toString(settings.getMoveDownKey()));
        view.getMoveLeftKeyLabel().setText(Input.Keys.toString(settings.getMoveLeftKey()));
        view.getMoveRightKeyLabel().setText(Input.Keys.toString(settings.getMoveRightKey()));
        view.getShootKeyLabel().setText(Input.Keys.toString(settings.getShootKey()));
        view.getReloadKeyLabel().setText(Input.Keys.toString(settings.getReloadKey()));
    }

    public void handleSettingsControls() {
        // Music volume change listener
        view.getMusicVolumeSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = view.getMusicVolumeSlider().getValue();
                settings.setMusicVolume(volume);
                GameAssetManager.getGameAssetManager().setMusicVolume(volume);
            }
        });

        // Music selection listener
        view.getMusicSelectBox().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedMusic = view.getMusicSelectBox().getSelected();
                settings.setCurrentMusic(selectedMusic);
                GameAssetManager.getGameAssetManager().changeMusic(selectedMusic);
            }
        });

        // SFX toggle listener
        view.getSfxToggle().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean sfxEnabled = view.getSfxToggle().isChecked();
                settings.setSfxEnabled(sfxEnabled);
            }
        });

        // Auto-reload toggle listener
        view.getAutoReloadToggle().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean autoReloadEnabled = view.getAutoReloadToggle().isChecked();
                settings.setAutoReloadEnabled(autoReloadEnabled);
            }
        });

        // Grayscale toggle listener
// In SettingsMenuController.java, modify the grayscale toggle listener to apply the shader immediately
        view.getGrayscaleToggle().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean grayscaleEnabled = view.getGrayscaleToggle().isChecked();
                settings.setGrayscaleEnabled(grayscaleEnabled);

                // Apply the change immediately
                if (grayscaleEnabled) {
                    Main.getBatch().setShader(GrayscaleShader.getShader());
                } else {
                    Main.getBatch().setShader(null);
                }
            }
        });
        // Key binding buttons listeners
        setupKeyBindingButton(view.getMoveUpButton(), "Move Up", settings::setMoveUpKey);
        setupKeyBindingButton(view.getMoveDownButton(), "Move Down", settings::setMoveDownKey);
        setupKeyBindingButton(view.getMoveLeftButton(), "Move Left", settings::setMoveLeftKey);
        setupKeyBindingButton(view.getMoveRightButton(), "Move Right", settings::setMoveRightKey);
        setupKeyBindingButton(view.getShootButton(), "Shoot", settings::setShootKey);
        setupKeyBindingButton(view.getReloadButton(), "Reload", settings::setReloadKey);

        // Back to main menu button
        view.getBackButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();
                // Save settings before returning to main menu
                App.saveSettings();
                navigateToMainMenu();
            }
        });
    }

    private void setupKeyBindingButton(Actor button, String actionName, KeyBindingSetter setter) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.playSound();
                view.showKeyBindingWindow(actionName, (keyCode) -> {
                    setter.set(keyCode);
                    updateKeyBindingLabels();
                });
            }
        });
    }

    private void navigateToMainMenu() {
        Main.getMain().getScreen().dispose();
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

    // Functional interface for setting key bindings
    @FunctionalInterface
    private interface KeyBindingSetter {
        void set(int keyCode);
    }
}
