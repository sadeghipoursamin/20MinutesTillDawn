package com.example.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.Settings;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GrayscaleShader;
import com.example.Views.GameView;
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
        view.getMusicVolumeSlider().setValue(settings.getMusicVolume());
        view.getSfxToggle().setChecked(settings.isSfxEnabled());
        view.getAutoReloadToggle().setChecked(settings.isAutoReloadEnabled());
        view.getGrayscaleToggle().setChecked(settings.isGrayscaleEnabled());
        view.getGrayscaleToggle().setChecked(settings.isGrayscaleEnabled());
    }


    public void handleSettingsControls() {
        view.getMusicVolumeSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = view.getMusicVolumeSlider().getValue();
                settings.setMusicVolume(volume);
                GameAssetManager.getGameAssetManager().setMusicVolume(volume);
            }
        });

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

        view.getAutoReloadToggle().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean autoReloadEnabled = view.getAutoReloadToggle().isChecked();
                settings.setAutoReloadEnabled(autoReloadEnabled);
            }
        });

        view.getGrayscaleToggle().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean grayscaleEnabled = view.getGrayscaleToggle().isChecked();
                settings.setGrayscaleEnabled(grayscaleEnabled);

                if (grayscaleEnabled) {
                    Main.getBatch().setShader(GrayscaleShader.getShader());
                } else {
                    Main.getBatch().setShader(null);
                }
            }
        });
        setupKeyBindingButton(view.getMoveUpButton(), "Move Up", settings::setMoveUpKey);
        setupKeyBindingButton(view.getMoveDownButton(), "Move Down", settings::setMoveDownKey);
        setupKeyBindingButton(view.getMoveLeftButton(), "Move Left", settings::setMoveLeftKey);
        setupKeyBindingButton(view.getMoveRightButton(), "Move Right", settings::setMoveRightKey);
        setupKeyBindingButton(view.getShootButton(), "Shoot", settings::setShootKey);
        setupKeyBindingButton(view.getReloadButton(), "Reload", settings::setReloadKey);

        view.getBackButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.playSound();
                App.saveSettings();
                navigateToMainMenu();
            }
        });

        view.getLightHaloToggle().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean lightHaloEnabled = view.getLightHaloToggle().isChecked();
                settings.setLightHaloEnabled(lightHaloEnabled);

                if (Main.getMain().getScreen() instanceof GameView) {
                    GameView gameView = (GameView) Main.getMain().getScreen();
                    gameView.getController().getPlayerController().getPlayer().setLightEnabled(lightHaloEnabled);
                }
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
                });
            }
        });
    }

    private void navigateToMainMenu() {
        Main.getMain().getScreen().dispose();
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

    @FunctionalInterface
    private interface KeyBindingSetter {
        void set(int keyCode);
    }
}
