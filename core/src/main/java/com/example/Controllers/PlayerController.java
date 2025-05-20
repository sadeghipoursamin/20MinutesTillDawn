package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public PlayerController(Player player) {
        this.player = player;
        this.map = new Texture(Gdx.files.internal("MapDetails/map.png"));
        this.width = map.getWidth();
        this.height = map.getHeight();
        map.dispose();
    }

    public void update() {
        if (player.isPlayerIdle()) {
            idleAnimation();
        }
        handlePlayerInput();
        player.getPlayerSprite().setPosition(player.getPosX(), player.getPosY());

        player.getPlayerSprite().draw(Main.getBatch());
    }

    public void handlePlayerInput() {
        float newX = player.getPosX();
        float newY = player.getPosY();
        boolean movingLeft = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += player.getSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += player.getSpeed();
            if (player.getPlayerSprite().isFlipX()) {
                player.getPlayerSprite().setFlip(false, false);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= player.getSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= player.getSpeed();
            movingLeft = true;
        }

        if (movingLeft && !player.getPlayerSprite().isFlipX()) {
            player.getPlayerSprite().setFlip(true, false);
        }

        float halfWidth = player.getPlayerSprite().getWidth() / 2f;
        float halfHeight = player.getPlayerSprite().getHeight() / 2f;

        newX = MathUtils.clamp(newX, halfWidth, width - halfWidth);
        newY = MathUtils.clamp(newY, halfHeight, height - halfHeight);

        player.setPosX(newX);
        player.setPosY(newY);
    }

    public void idleAnimation() {
        Animation<Texture> animation = GameAssetManager.getGameAssetManager().Idle_animation("Shana");
        boolean wasFlipped = player.getPlayerSprite().isFlipX();

        player.getPlayerSprite().setRegion(animation.getKeyFrame(player.getTime()));

        if (wasFlipped) {
            player.getPlayerSprite().setFlip(true, false);
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

}
