package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.Bullet;
import com.example.Models.Weapon;

import java.util.ArrayList;
import java.util.Iterator;

public class WeaponController {
    private Weapon weapon;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private PlayerController playerController;
    private float reloadCooldown = 2f; // 2 seconds delay
    private float timeSinceLastReload = 0f;

    public WeaponController(Weapon weapon) {
        this.weapon = weapon;
    }

    public void update() {
        updateBullets();

        timeSinceLastReload += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && timeSinceLastReload >= reloadCooldown) {
            weapon.reload();
            timeSinceLastReload = 0f;
        }

        if (playerController != null) {
            float playerX = playerController.getPlayer().getPosX();
            float playerY = playerController.getPlayer().getPosY();

            getWeaponSprite().setPosition(playerX, playerY);
            getWeaponSprite().draw(Main.getBatch());
        }
    }


    public Sprite getWeaponSprite() {
        switch (weapon.getWeaponType()) {
            case REVOLVER -> {
                return weapon.getRevolverSprite();
            }
            case SHOTGUN -> {
                return weapon.getShotgunSprite();
            }
            case SMGS_DUAL -> {
                return weapon.getSmgSprite();
            }
            default -> {
                return weapon.getRevolverSprite();
            }
        }
    }

    public void handleWeaponRotation(int x, int y) {
        Sprite weaponSprite = getWeaponSprite();

        float weaponCenterX = playerController.getPlayer().getPosX();
        float weaponCenterY = playerController.getPlayer().getPosY();

        float angle = (float) Math.atan2(y - weaponCenterY, x - weaponCenterX);
        float degree = (float) Math.toDegrees(angle);
        weaponSprite.setRotation((degree));
    }

    public void handleWeaponShoot(int x, int y) {
        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();

        Bullet bullet = new Bullet((int) playerX, (int) playerY);

        Vector2 direction = new Vector2(x - playerX, y - playerY).nor();
        bullet.getSprite().setPosition(playerX, playerY);
        bullet.setDirection(direction);

        bullets.add(bullet);
        if ((weapon.getAmmo() == 0 && App.getSettings().isAutoReloadEnabled())) {
            weapon.reload();
        }
        weapon.setAmmo(weapon.getAmmo() - 1);
    }

    public void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            bullet.getSprite().draw(Main.getBatch());

            float speed = 10.0f;

            // Move sprite
            bullet.getSprite().setX(bullet.getSprite().getX() + bullet.getDirection().x * speed);
            bullet.getSprite().setY(bullet.getSprite().getY() + bullet.getDirection().y * speed);

            // Sync logical position
            bullet.setX(bullet.getSprite().getX());
            bullet.setY(bullet.getSprite().getY());

            float playerX = playerController.getPlayer().getPosX();
            float playerY = playerController.getPlayer().getPosY();

            if (isBulletFar(bullet, playerX, playerY)) {
                bullet.dispose();
                iterator.remove();
            }
        }
    }

    private boolean isBulletFar(Bullet bullet, float x, float y) {
        float bulletX = bullet.getSprite().getX();
        float bulletY = bullet.getSprite().getY();

        float distanceSquared = (bulletX - x) * (bulletX - x) + (bulletY - y) * (bulletY - y);
        return distanceSquared > 1000 * 1000;
    }


    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
}
