package com.example.Controllers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.example.Main;
import com.example.Models.Bullet;
import com.example.Models.Weapon;

import java.util.ArrayList;
import java.util.Iterator;

public class WeaponController {
    private Weapon weapon;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private PlayerController playerController;

    public WeaponController(Weapon weapon) {
        this.weapon = weapon;
    }

    public void update() {
        updateBullets();

        if (playerController != null) {
            float playerX = playerController.getPlayer().getPosX();
            float playerY = playerController.getPlayer().getPosY();

            weapon.getSmgSprite().setPosition(
                playerX, playerY
            );

            weapon.getSmgSprite().draw(Main.getBatch());
        }
    }

    public void handleWeaponRotation(int x, int y) {
        Sprite weaponSprite = weapon.getSmgSprite();

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

        weapon.setAmmo(weapon.getAmmo() - 1);
    }

    public void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            bullet.getSprite().draw(Main.getBatch());

            float speed = 10.0f;

            bullet.getSprite().setX(bullet.getSprite().getX() + bullet.getDirection().x * speed);
            bullet.getSprite().setY(bullet.getSprite().getY() + bullet.getDirection().y * speed);

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
}
