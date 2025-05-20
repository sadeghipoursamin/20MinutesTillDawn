package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.example.Main;

public class WorldController {
    private PlayerController playerController;
    private Texture background;


    public WorldController(PlayerController playerController) {
        this.playerController = playerController;
        this.background = new Texture("MapDetails/map.png");
    }

    public void update() {

        float backWidth = background.getWidth();
        float backHeight = background.getHeight();

        int tilesX = (int) Math.ceil(Gdx.graphics.getWidth() / backWidth) + 2;
        int tilesY = (int) Math.ceil(Gdx.graphics.getHeight() / backHeight) + 2;

        float cameraX = 0;
        float cameraY = 0;

        float offsetX = 0;
        float offsetY = 0;

        float startX = cameraX - offsetX - backWidth;
        float startY = cameraY - offsetY - backHeight;

        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                float tileX = startX + (x * backWidth);
                float tileY = startY + (y * backHeight);
                Main.getBatch().draw(background, tileX, tileY);
            }
        }
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public Texture getBackground() {
        return background;
    }

}
