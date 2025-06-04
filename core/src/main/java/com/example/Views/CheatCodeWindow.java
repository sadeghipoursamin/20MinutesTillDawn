package com.example.Views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Controllers.GameController;
import com.example.Main;

public class CheatCodeWindow extends Window {
    TextButton backButton;
    private Label promptLabel;
    private TextField cheatInput;
    private TextButton submitButton;
    private Label feedbackLabel;

    public CheatCodeWindow(Skin skin, GameController gameController) {
        super("Enter Cheat Code", skin);

        promptLabel = new Label("Type your cheat code below:", skin);
        cheatInput = new TextField("", skin);
        submitButton = new TextButton("Submit", skin);
        feedbackLabel = new Label("", skin);
        this.backButton = new TextButton("Back", skin);

        this.add(promptLabel).pad(10);
        this.row();
        this.add(cheatInput).width(250).pad(10);
        this.row();
        this.add(submitButton).pad(10);
        this.row();
        this.add(feedbackLabel).pad(10);
        this.row();
        this.add(backButton).pad(10);

        this.setSize(600, 500);
        this.setPosition(400, 200);

        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                String code = cheatInput.getText().trim().toLowerCase();
                handleCheatCode(code, gameController);
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                CheatCodeWindow.this.setVisible(false);
                gameController.getView().hideCheatMenu();
            }
        });

    }

    private void handleCheatCode(String code, GameController controller) {
        switch (code) {
            case "time":
                controller.cheatTime();
                feedbackLabel.setText("Duration decreased by 1 minute!");
                break;

            case "level":
                controller.getPlayerController().getPlayer().cheatIncreaseLevel();
                feedbackLabel.setText("Level +1 !");
                CheatCodeWindow.this.setVisible(false);
                controller.getView().hideCheatMenu();
                controller.getEnemyController().showLevelUpWindow();
                break;

            case "health":
                if (controller.getPlayerController().getPlayer().cheatIncreaseHealth(1)) {
                    feedbackLabel.setText("Health increased by 1!");

                } else {
                    feedbackLabel.setText("Health is full!");
                }
                break;
            case "bossfight":
                controller.cheatBossFight();
                break;
            case "ammo":
                controller.getWeaponController().getWeapon().setAmmo(20);
                feedbackLabel.setText("Ammo +20 !");
                break;
            default:
                feedbackLabel.setText("Invalid cheat code!");
                break;
        }

        cheatInput.setText("");
    }


}
