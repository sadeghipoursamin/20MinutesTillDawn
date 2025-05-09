package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.PreGameMenuController;
import com.example.Main;

public class PreGameMenuView implements Screen {
    private Stage stage;
    private final Label gameTitle;
    private final TextButton playButton;
    private Table table;
    private final SelectBox selctHero;
    private final PreGameMenuController controller;

    public PreGameMenuView(PreGameMenuController controller, Skin skin) {
        this.controller = controller;
        this.selctHero = new SelectBox<>(skin);
        this.playButton = new TextButton("Play", skin);
        this.gameTitle = new Label("Pre game menu", skin);
        this.table = new Table();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Array<String> hero = new Array<>();
        hero.add("Hero 1");
        hero.add("Hero 2");
        hero.add("Hero 3");
        selctHero.setItems(hero);
        table.setFillParent(true);
        table.center();
        table.add(gameTitle);
        table.row().pad(10,0,10,0);
        table.add(selctHero);
        table.row().pad(10,0,10,0);
        table.add(playButton);

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,0);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.handlePreGameMenuButtons();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
