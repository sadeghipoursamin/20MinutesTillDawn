package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;

public class PlayerDeathWindow extends Window {
    private Label titleLabel;
    private Label messageLabel;
    private TextButton backButton;
    private Runnable onBack;

    public PlayerDeathWindow(Skin skin) {
        super("", skin);

        initializeUI(skin);
        setupListeners();

        this.setSize(1200, 700);
        this.setPosition(
            (Gdx.graphics.getWidth() - this.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - this.getHeight()) / 2f
        );
        this.setModal(true);
        this.setMovable(false);
    }

    private void initializeUI(Skin skin) {
        // Title
        titleLabel = new Label("YOU DIED!", skin, "title");
        titleLabel.setColor(Color.RED);
        titleLabel.setFontScale(2f);

        // Message
        messageLabel = new Label("Better Luck Next Time...", skin);
        messageLabel.setColor(Color.LIGHT_GRAY);

        // Back button
        backButton = new TextButton("Back", skin);
        backButton.setColor(Color.DARK_GRAY);
        backButton.getLabel().setFontScale(1.5f);

        // Layout
        this.add(titleLabel).center().padBottom(20);
        this.row();
        this.add(messageLabel).center().padBottom(40);
        this.row();
        this.add(backButton).width(250).height(80).center();
    }

    private void setupListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                if (onBack != null) {
                    onBack.run();
                }
                remove();
            }
        });
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
