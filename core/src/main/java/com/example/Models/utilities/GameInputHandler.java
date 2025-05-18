package com.example.Models.utilities;

import com.badlogic.gdx.InputAdapter;
import com.example.Models.App;
import com.example.Models.Settings;

/**
 * Custom input handler that respects the user's key binding settings
 */
public class GameInputHandler extends InputAdapter {

    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean shoot = false;
    private boolean reload = false;

    private InputActionListener actionListener;

    public GameInputHandler(InputActionListener listener) {
        this.actionListener = listener;
    }

    @Override
    public boolean keyDown(int keycode) {
        Settings settings = App.getSettings();

        if (keycode == settings.getMoveUpKey()) {
            moveUp = true;
            if (actionListener != null) actionListener.onMoveUp(true);
            return true;
        }
        if (keycode == settings.getMoveDownKey()) {
            moveDown = true;
            if (actionListener != null) actionListener.onMoveDown(true);
            return true;
        }
        if (keycode == settings.getMoveLeftKey()) {
            moveLeft = true;
            if (actionListener != null) actionListener.onMoveLeft(true);
            return true;
        }
        if (keycode == settings.getMoveRightKey()) {
            moveRight = true;
            if (actionListener != null) actionListener.onMoveRight(true);
            return true;
        }
        if (keycode == settings.getShootKey()) {
            shoot = true;
            if (actionListener != null) actionListener.onShoot(true);
            return true;
        }
        if (keycode == settings.getReloadKey()) {
            reload = true;
            if (actionListener != null) actionListener.onReload();
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Settings settings = App.getSettings();

        if (keycode == settings.getMoveUpKey()) {
            moveUp = false;
            if (actionListener != null) actionListener.onMoveUp(false);
            return true;
        }
        if (keycode == settings.getMoveDownKey()) {
            moveDown = false;
            if (actionListener != null) actionListener.onMoveDown(false);
            return true;
        }
        if (keycode == settings.getMoveLeftKey()) {
            moveLeft = false;
            if (actionListener != null) actionListener.onMoveLeft(false);
            return true;
        }
        if (keycode == settings.getMoveRightKey()) {
            moveRight = false;
            if (actionListener != null) actionListener.onMoveRight(false);
            return true;
        }
        if (keycode == settings.getShootKey()) {
            shoot = false;
            if (actionListener != null) actionListener.onShoot(false);
            return true;
        }

        return false;
    }

    // Getters for the current state
    public boolean isMoveUp() {
        return moveUp;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public boolean isShoot() {
        return shoot;
    }

    public boolean isReload() {
        return reload;
    }

    // Reset the reload flag after handling it
    public void resetReload() {
        reload = false;
    }

    /**
     * Interface for game components to listen to input actions
     */
    public interface InputActionListener {
        void onMoveUp(boolean pressed);

        void onMoveDown(boolean pressed);

        void onMoveLeft(boolean pressed);

        void onMoveRight(boolean pressed);

        void onShoot(boolean pressed);

        void onReload();
    }
}
