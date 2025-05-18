package com.example.Controllers;

import com.badlogic.gdx.graphics.Texture;
import com.example.Main;

public class WorldController {
    private PlayerController playerController;
    private Texture background;
    private float backGroundX = 0;
    private float backGroundY = 0;

    public WorldController(PlayerController playerController) {
        this.playerController = playerController;
        this.background = new Texture("background.png");
    }

    public void update() {
        backGroundX = playerController.getPlayer().getPosX();
        backGroundY = playerController.getPlayer().getPosY();
        Main.getBatch().draw(background, backGroundX, backGroundY);
    }
}
