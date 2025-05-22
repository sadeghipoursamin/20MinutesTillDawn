package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.Bullet;
import com.example.Models.Weapon;
import com.example.Models.utilities.GameAssetManager;

import java.util.ArrayList;
import java.util.Iterator;

public class WeaponController {
    private Weapon weapon;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private PlayerController playerController;
    private float reloadCooldown = 2f;
    private float timeSinceLastReload = 0f;

    // Reload animation variables
    private boolean isReloading = false;
    private float reloadProgress = 0f;
    private float reloadDuration = 2f;
    private Texture reloadBarEmpty;
    private Texture reloadBarIndicator;
    private Sprite reloadBarEmptySprite;
    private Sprite reloadBarIndicatorSprite;
    private boolean reloadBarVisible = false;

    public WeaponController(Weapon weapon) {
        this.weapon = weapon;
        initializeReloadBarTextures();
    }

    private void initializeReloadBarTextures() {
        try {
            reloadBarEmpty = new Texture(Gdx.files.internal("Weapons/ReloadBar_0.png"));
            reloadBarIndicator = new Texture(Gdx.files.internal("Weapons/ReloadBar_1.png"));

            reloadBarEmptySprite = new Sprite(reloadBarEmpty);
            reloadBarIndicatorSprite = new Sprite(reloadBarIndicator);

            float barWidth = 100f;
            float barHeight = 20f;
            reloadBarEmptySprite.setSize(barWidth, barHeight);

            float indicatorWidth = 10f;
            reloadBarIndicatorSprite.setSize(indicatorWidth, barHeight);

        } catch (Exception e) {
            System.err.println("Error loading reload bar textures: " + e.getMessage());

        }
    }


    public void update() {
        updateBullets();
        updateReload();

        if (playerController != null) {
            float playerX = playerController.getPlayer().getPosX();
            float playerY = playerController.getPlayer().getPosY();

            getWeaponSprite().setPosition(playerX, playerY);
            getWeaponSprite().draw(Main.getBatch());
        }
    }

    private void updateReload() {
        timeSinceLastReload += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && !isReloading && timeSinceLastReload >= reloadCooldown) {
            startReload();
        }

        if (isReloading) {
            reloadProgress += Gdx.graphics.getDeltaTime();

            if (reloadProgress >= reloadDuration) {
                completeReload();
            }

            updateReloadBarPosition();
        }
    }

    private void startReload() {
        if (weapon.getAmmo() >= weapon.getWeaponType().getAmmoMax()) {
            return;
        }

        isReloading = true;
        reloadProgress = 0f;
        reloadBarVisible = true;
        timeSinceLastReload = 0f;

        GameAssetManager.getGameAssetManager().reloadSound();

        System.out.println("Started reloading...");
    }

    private void completeReload() {
        isReloading = false;
        reloadProgress = 0f;
        reloadBarVisible = false;

        weapon.setAmmo(weapon.getWeaponType().getAmmoMax());

        System.out.println("Reload complete!");
    }

    private void updateReloadBarPosition() {
        if (playerController != null && reloadBarVisible) {
            float playerX = playerController.getPlayer().getPosX();
            float playerY = playerController.getPlayer().getPosY();

            float barX = playerX - 25;
            float barY = playerY + 80;

            reloadBarEmptySprite.setPosition(barX, barY);

            float progressRatio = Math.min(reloadProgress / reloadDuration, 1.0f);

            float barTravelDistance = reloadBarEmptySprite.getWidth() - reloadBarIndicatorSprite.getWidth();
            float indicatorX = barX + (barTravelDistance * progressRatio);

            reloadBarIndicatorSprite.setPosition(indicatorX, barY);
        }
    }

    public void renderReloadBar(SpriteBatch batch) {
        if (reloadBarVisible && reloadBarEmptySprite != null && reloadBarIndicatorSprite != null) {
            reloadBarEmptySprite.draw(batch);

            if (reloadProgress > 0) {
                reloadBarIndicatorSprite.draw(batch);
            }
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
        if (isReloading) return;

        Sprite weaponSprite = getWeaponSprite();

        float weaponCenterX = playerController.getPlayer().getPosX();
        float weaponCenterY = playerController.getPlayer().getPosY();

        float angle = (float) Math.atan2(y - weaponCenterY, x - weaponCenterX);
        float degree = (float) Math.toDegrees(angle);
        weaponSprite.setRotation((degree));
    }

    public void handleWeaponShoot(int x, int y) {
        if (isReloading) return;

        if (weapon.getAmmo() <= 0) {
            if (App.getSettings().isAutoReloadEnabled()) {
                startReload();
            }
            return;
        }

        float playerX = playerController.getPlayer().getPosX();
        float playerY = playerController.getPlayer().getPosY();

        int baseDamage = weapon.getWeaponType().getDamage();
        float damageMultiplier = playerController.getPlayer().getDamageMultiplier();
        int finalDamage = Math.round(baseDamage * damageMultiplier);

        Bullet bullet = new Bullet((int) playerX, (int) playerY, true);
        bullet.setDamage(finalDamage);

        Vector2 direction = new Vector2(x - playerX, y - playerY).nor();
        bullet.getSprite().setPosition(playerX, playerY);
        bullet.setDirection(direction);

        bullets.add(bullet);
        weapon.setAmmo(weapon.getAmmo() - 1);

        if (weapon.getAmmo() == 0 && App.getSettings().isAutoReloadEnabled() && !isReloading) {
            startReload();
        }
    }

    public void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            bullet.getSprite().draw(Main.getBatch());

            float speed = 10.0f;

            bullet.getSprite().setX(bullet.getSprite().getX() + bullet.getDirection().x * speed);
            bullet.getSprite().setY(bullet.getSprite().getY() + bullet.getDirection().y * speed);

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

    public boolean isReloading() {
        return isReloading;
    }

    public float getReloadProgress() {
        return reloadProgress;
    }

    public float getReloadDuration() {
        return reloadDuration;
    }

    public void setReloadDuration(float duration) {
        this.reloadDuration = duration;
    }

    public void dispose() {
        if (reloadBarEmpty != null) {
            reloadBarEmpty.dispose();
        }
        if (reloadBarIndicator != null) {
            reloadBarIndicator.dispose();
        }
    }
}
