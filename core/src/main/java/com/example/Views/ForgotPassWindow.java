package com.example.Views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Main;
import com.example.Models.User;

public class ForgotPassWindow extends Window {
    private Label label;
    private TextField answer;
    private TextButton back;

    private Runnable onComplete;
    public ForgotPassWindow(Skin skin, User user) {
        super("Forgot Password?", skin);
        this.back = new TextButton("Back", skin);
        this.answer = new TextField("", skin);
        this.label = new Label("Answer this question!", skin);

        this.add(label);
        this.row();
        this.add(answer).width(200).pad(10);
        this.row();
        this.add(back).padBottom(10);

        this.setSize(500, 500);
        this.setPosition(510, 200);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public TextField getAnswer() {
        return answer;
    }
}
