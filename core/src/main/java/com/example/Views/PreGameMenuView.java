package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.PreGameMenuController;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.enums.Hero;
import com.example.Models.enums.WeaponType;

public class PreGameMenuView implements Screen {
    private final Label gameTitle;
    private final TextButton playButton;
    private final SelectBox<String> selectHero;
    private final SelectBox<String> selectWeapon;
    private final SelectBox<String> selectTime;
    private final Label heroLabel;
    private final Label weaponLabel;
    private final Label timeLabel;
    private final PreGameMenuController controller;
    private Stage stage;
    private Table table;
    private Texture texture;
    private Image image;

    public PreGameMenuView(PreGameMenuController controller, Skin skin) {
        this.controller = controller;
        controller.setView(this);
        this.selectHero = new SelectBox<>(skin);
        this.playButton = new TextButton("Play", skin);
        this.gameTitle = new Label("Pre-game menu", skin, "title");
        this.table = new Table();
        this.selectWeapon = new SelectBox<>(skin);
        this.selectTime = new SelectBox<>(skin);
        this.heroLabel = new Label("Select Hero:", skin);
        this.weaponLabel = new Label("Select Weapon:", skin);
        this.timeLabel = new Label("Select Time:", skin);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        selectHero();
        setSelectWeapon();
        setSelectTime();

        table.setFillParent(true);

        texture = new Texture(Gdx.files.internal("backGround.png"));
        image = new Image(texture);
        image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.addActor(image);

        table.center();

        table.add(gameTitle).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        table.add(heroLabel).left().padRight(10);
        table.add(selectHero).width(300);
        table.row().pad(10, 0, 10, 0);

        table.add(weaponLabel).left().padRight(10);
        table.add(selectWeapon).width(300);
        table.row().pad(10, 0, 10, 0);

        table.add(timeLabel).left().padRight(10);
        table.add(selectTime).width(300);
        table.row().pad(10, 0, 10, 0);

        table.add(playButton).colspan(2).center();
        table.row().pad(10, 0, 10, 0);

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
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

    public void selectHero() {
        Array<String> heroes = new Array<>();
        for (Hero hero : App.getHeroes()) {
            heroes.add(hero.getName());
        }
        selectHero.setItems(heroes);
    }

    public void setSelectWeapon() {
        Array<String> weapons = new Array<>();
        for (WeaponType weaponType : App.getWeapons()) {
            weapons.add(weaponType.getName());
        }
        selectWeapon.setItems(weapons);
    }

    public void setSelectTime() {
        Array<String> times = new Array<>();
        times.add("2");
        times.add("5");
        times.add("10");
        times.add("20");
        selectTime.setItems(times);
    }

    public Label getGameTitle() {
        return gameTitle;
    }

    public TextButton getPlayButton() {
        return playButton;
    }

    public SelectBox<String> getSelectHero() {
        return selectHero;
    }

    public SelectBox<String> getSelectWeapon() {
        return selectWeapon;
    }

    public SelectBox<String> getSelectTime() {
        return selectTime;
    }

    public Label getHeroLabel() {
        return heroLabel;
    }

    public Label getWeaponLabel() {
        return weaponLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public PreGameMenuController getController() {
        return controller;
    }

    public Stage getStage() {
        return stage;
    }

    public Table getTable() {
        return table;
    }

    public Texture getTexture() {
        return texture;
    }

    public Image getImage() {
        return image;
    }
}
