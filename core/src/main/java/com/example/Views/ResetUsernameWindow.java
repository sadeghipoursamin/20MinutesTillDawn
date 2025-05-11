package com.example.Views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Models.User;

public class ResetUsernameWindow extends Window {
    private TextField newUsername;
    private Label newUsernameLabel;
    private TextButton resetButton;
    private Runnable onComplete;
    private Label errorLabel;

    public ResetUsernameWindow(Skin skin, User user) {
        super("Reset Username!", skin);
        this.newUsername = new TextField("", skin);
        this.newUsernameLabel = new Label("New Username:", skin);
        this.resetButton = new TextButton("Reset", skin);
        this.errorLabel = new Label("", skin);

        this.add(newUsernameLabel).height(40).padRight(20);
        this.add(newUsername).width(400).height(80);
        this.row().pad(20, 0, 20, 0);


        this.add(resetButton).colspan(2).center();
        this.row().pad(10, 0, 10, 0);

        this.setSize(300, 300);
        this.setPosition(500, 0);

        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(user.getUsername().equals(newUsername.getText())){
                    errorLabel.setText("Username Must be Distinct");
                    return;
                }
                if(newUsername.getText().equals("")){
                    errorLabel.setText("Username Must not be Empty");
                    return;
                }
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public TextField getNewUsername() {
        return newUsername;
    }

    public Label getNewUsernameLabel() {
        return newUsernameLabel;
    }

    public TextButton getResetButton() {
        return resetButton;
    }

    public Runnable getOnComplete() {
        return onComplete;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
