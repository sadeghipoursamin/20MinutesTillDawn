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
    private float reloadDuration = 2f; // Duration of reload animation in seconds
    private Texture reloadBarEmpty;
    private Texture reloadBarIndicator; // Changed from reloadBarFill to reloadBarIndicator
    private Sprite reloadBarEmptySprite;
    private Sprite reloadBarIndicatorSprite; // Changed from reloadBarFillSprite
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

            // Make the indicator smaller - it will move along the bar
            float indicatorWidth = 10f; // Smaller width for the moving indicator
            reloadBarIndicatorSprite.setSize(indicatorWidth, barHeight);

        } catch (Exception e) {
            System.err.println("Error loading reload bar textures: " + e.getMessage());
            // Create fallback colored rectangles if textures can't be loaded
            createFallbackReloadBars();
        }
    }

    private void createFallbackReloadBars() {
        // Create simple colored textures as fallback
        // This is a simplified version - you might want to implement proper texture creation
        System.out.println("Using fallback reload bars");
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

        // Handle reload input
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && !isReloading && timeSinceLastReload >= reloadCooldown) {
            startReload();
        }

        // Update reload animation
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
            return; // Don't reload if already at max ammo
        }

        isReloading = true;
        reloadProgress = 0f;
        reloadBarVisible = true;
        timeSinceLastReload = 0f;

        // Play reload sound
        GameAssetManager.getGameAssetManager().reloadSound();

        System.out.println("Started reloading...");
    }

    private void completeReload() {
        isReloading = false;
        reloadProgress = 0f;
        reloadBarVisible = false;

        // Actually reload the weapon
        weapon.setAmmo(weapon.getWeaponType().getAmmoMax());

        System.out.println("Reload complete!");
    }

    private void updateReloadBarPosition() {
        if (playerController != null && reloadBarVisible) {
            float playerX = playerController.getPlayer().getPosX();
            float playerY = playerController.getPlayer().getPosY();

            // Position the reload bar above the player
            float barX = playerX - reloadBarEmptySprite.getWidth() / 2;
            float barY = playerY + 80; // 80 pixels above the player

            reloadBarEmptySprite.setPosition(barX, barY);

            // Calculate the indicator position based on progress
            float progressRatio = Math.min(reloadProgress / reloadDuration, 1.0f);

            // Calculate how far along the bar the indicator should be
            float barTravelDistance = reloadBarEmptySprite.getWidth() - reloadBarIndicatorSprite.getWidth();
            float indicatorX = barX + (barTravelDistance * progressRatio);

            reloadBarIndicatorSprite.setPosition(indicatorX, barY);
        }
    }

    public void renderReloadBar(SpriteBatch batch) {
        if (reloadBarVisible && reloadBarEmptySprite != null && reloadBarIndicatorSprite != null) {
            // Draw the empty bar first
            reloadBarEmptySprite.draw(batch);

            // Draw the moving indicator on top
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
