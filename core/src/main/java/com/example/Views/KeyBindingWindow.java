package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;

import java.util.function.Consumer;

public class KeyBindingWindow extends Window {
    private Label instructionLabel;
    private Label keyPressedLabel;
    private TextButton confirmButton;
    private TextButton cancelButton;
    private int capturedKeyCode = -1;
    private Consumer<Integer> onKeyBound;

    public KeyBindingWindow(String actionName, Skin skin, Consumer<Integer> onKeyBound) {
        super("Set Key for " + actionName, skin);

        this.onKeyBound = onKeyBound;

        // Create UI components
        instructionLabel = new Label("Press any key to bind to '" + actionName + "'", skin);
        keyPressedLabel = new Label("", skin);
        confirmButton = new TextButton("Confirm", skin);
        cancelButton = new TextButton("Cancel", skin);

        // Disable the confirm button initially until a key is pressed
        confirmButton.setDisabled(true);

        // Layout
        this.add(instructionLabel).pad(20);
        this.row();
        this.add(keyPressedLabel).pad(10);
        this.row();

        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(100).pad(5);
        buttonTable.add(cancelButton).width(100).pad(5);

        this.add(buttonTable).pad(10);

        // Set up button handlers
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                if (capturedKeyCode != -1) {
                    if (onKeyBound != null) {
                        onKeyBound.accept(capturedKeyCode);
                    }
                }
                remove();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                remove();
            }
        });

        // Set up the key capture listener
        this.addCaptureListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keyCode) {
                if (keyCode != Input.Keys.ESCAPE) {
                    capturedKeyCode = keyCode;
                    keyPressedLabel.setText("You pressed: " + Input.Keys.toString(keyCode));
                    confirmButton.setDisabled(false);
                }
                return true;
            }
        });

        // Size and position
        this.setSize(300, 200);
        this.setPosition(Gdx.graphics.getWidth() / 2f - 150, Gdx.graphics.getHeight() / 2f - 100);

        // Make the window modal
        this.setModal(true);
    }
}
