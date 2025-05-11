package com.example.Views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ResetPasswordWindow extends Window {
    private TextField newPassword;
    private Label newPasswordLabel;
    private TextField repeatPassword;
    private Label repeatPasswordLabel;
    private Label errorLabel;
    private TextButton reset;
    private Runnable onComplete;
    public ResetPasswordWindow(Skin skin, User user) {
        super("Reset Password!", skin);
        this.newPassword = new TextField("", skin);
        this.newPasswordLabel = new Label("New Password:", skin);
        this.repeatPassword = new TextField("", skin);
        this.repeatPasswordLabel = new Label("Repeat Password:", skin);
        this.reset = new TextButton("Reset", skin);
        this.errorLabel = new Label("", skin);

        //password
        this.add(newPasswordLabel).height(40).padRight(20);
        this.add(newPassword).width(400).height(80);
        this.row().pad(20, 0, 20, 0);

        this.add(repeatPasswordLabel).height(40).padRight(20);
        this.add(repeatPassword).width(400).height(80);
        this.row().pad(20, 0, 20, 0);

        this.add(reset).colspan(2).center();
        this.row().pad(10, 0, 10, 0);

        this.setSize(700, 700);
        this.setPosition(310, 0);

        reset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!newPassword.getText().equals(repeatPassword.getText())) {
                    errorLabel.setText("Passwords do not match!");
                    return;
                }

                if(!isValidPass(newPassword.getText())) {
                    errorLabel.setText("Password is Weak!");
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

    public TextField getNewPassword() {
        return newPassword;
    }

    public Label getNewPasswordLabel() {
        return newPasswordLabel;
    }

    public TextField getRepeatPassword() {
        return repeatPassword;
    }

    public Label getRepeatPasswordLabel() {
        return repeatPasswordLabel;
    }

    public TextButton getReset() {
        return reset;
    }

    public Runnable getOnComplete() {
        return onComplete;
    }

    public boolean isValidPass(String password){
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&*()_]).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
