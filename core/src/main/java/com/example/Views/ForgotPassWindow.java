package com.example.Views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Models.User;

public class ForgotPassWindow extends Window {
    private TextField answer;
    private TextButton back;
    private Label label;
    public ForgotPassWindow(Skin skin, User user) {
        super("Forgot Password?", skin, "title");
        this.back = new TextButton("Back", skin);
        this.answer = new TextField("", skin);

        this.add(answer).width(200).pad(10);
        this.row();
        this.add(back).padBottom(10);

        this.setSize(300, 150);
        this.setPosition(100, 100);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
    }
}
