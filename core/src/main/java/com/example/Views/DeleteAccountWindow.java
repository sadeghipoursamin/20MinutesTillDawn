package com.example.Views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;

public class DeleteAccountWindow extends Window {
    private Label label;
    private TextField password;
    private TextButton back;
    private TextButton delete;
    private Label error;

    private Runnable onComplete;

    public DeleteAccountWindow(Skin skin, User user) {
        super("Delete Account?", skin);
        this.back = new TextButton("Back", skin);
        this.password = new TextField("", skin);
        this.delete = new TextButton("Delete", skin);
        this.label = new Label("Enter Your Password:", skin);
        this.error = new Label("", skin);
        this.add(label);
        this.row();
        this.add(password).width(200).pad(10);
        this.row();
        this.add(error).width(200).pad(10);
        this.row();
        this.add(back).width(200).height(100).pad(10);
        this.add(delete).width(200).height(100).pad(10);

        this.row();

        this.setSize(500, 500);
        this.setPosition(510, 200);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                remove();
            }
        });

        delete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();

                if (!App.getCurrentUser().getPassword().equals(password.getText())) {
                    error.setText("Wrong Password!");
                    error.setColor(Color.RED);
                    return;
                }
                if (onComplete != null) {
                    onComplete.run();
                }
                remove();
            }
        });
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public TextField getPassword() {
        return password;
    }
}
