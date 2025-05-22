package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.example.Main;
import com.example.Models.Player;
import com.example.Models.utilities.GameAssetManager;

public class PlayerController {
    private Player player;
    private boolean recentlyFlipped = false;
    private Texture map;
    private float width;
    private float height;
    private boolean inputEnabled = true;
    private EnemyController enemyController;

    public PlayerController(Player player) {
        this.player = player;
        this.map = new Texture(Gdx.files.internal("MapDetails/map.png"));
        this.width = map.getWidth();
        this.height = map.getHeight();
        map.dispose();
    }

    public void setEnemyController(EnemyController enemyController) {
        this.enemyController = enemyController;
    }

    public void handlePlayerInput() {
        if (!inputEnabled) {
            return;
        }
        float newX = player.getPosX();
        float newY = player.getPosY();
        boolean movingLeft = false;
        boolean isMoving = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += player.getSpeed();
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += player.getSpeed();
            isMoving = true;
            if (player.getHeroSprite().isFlipX()) {
                player.getHeroSprite().setFlip(false, false);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= player.getSpeed();
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= player.getSpeed();
            isMoving = true;
            movingLeft = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            enemyController.changeAutoAimState();
        }


        if (movingLeft && !player.getHeroSprite().isFlipX()) {
            player.getHeroSprite().setFlip(true, false);
        }

        float halfWidth = player.getHeroSprite().getWidth() / 2f;
        float halfHeight = player.getHeroSprite().getHeight() / 2f;

        newX = MathUtils.clamp(newX, halfWidth, width - halfWidth);
        newY = MathUtils.clamp(newY, halfHeight, height - halfHeight);

        player.setPosX(newX);
        player.setPosY(newY);

        player.setRunning(isMoving);
        player.setPlayerIdle(!isMoving);
    }


    public void idleAnimation() {
        Animation<Texture> animation;

        animation = GameAssetManager.getGameAssetManager().idleAnimation(player.getHero().getName());


        boolean wasFlipped = player.getHeroSprite().isFlipX();

        player.getHeroSprite().setRegion(animation.getKeyFrame(player.getTime()));

        if (wasFlipped) {
            player.getHeroSprite().setFlip(true, false);
        }
        if (!animation.isAnimationFinished(player.getTime())) {
            player.setTime(player.getTime() + Gdx.graphics.getDeltaTime());
        } else {
            player.setTime(0);
        }

        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void runningAnimation() {
        Animation<Texture> animation = GameAssetManager.getGameAssetManager().runAnimation(player.getHero().getName());

        boolean wasFlipped = player.getHeroSprite().isFlipX();

        player.getHeroSprite().setRegion(animation.getKeyFrame(player.getTime()));

        if (wasFlipped) {
            player.getHeroSprite().setFlip(true, false);
        }
        if (!animation.isAnimationFinished(player.getTime())) {
            player.setTime(player.getTime() + Gdx.graphics.getDeltaTime());
        } else {
            player.setTime(0);
        }

        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public void update() {
        handlePlayerInput();

        if (player.isRunning()) {
            runningAnimation();
        } else {
            idleAnimation();
        }

        player.getHeroSprite().setPosition(player.getPosX(), player.getPosY());
        player.getHeroSprite().draw(Main.getBatch());

        if (player.isLightEnabled()) {
            player.getLightHalo().update(player.getPosX(), player.getPosY(), Gdx.graphics.getDeltaTime());
        }
    }


    public void renderLight() {
        if (player.isLightEnabled()) {
            Main.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            player.getLightHalo().render(Main.getBatch());
            Main.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    public boolean isInputEnabled() {
        return inputEnabled;
    }

    public void setInputEnabled(boolean inputEnabled) {
        this.inputEnabled = inputEnabled;
    }


}
