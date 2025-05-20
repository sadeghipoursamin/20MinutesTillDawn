package com.example.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.example.Models.enums.WeaponType;
import com.example.Models.utilities.GameAssetManager;

public class Weapon {
    private final Texture smgTexture = GameAssetManager.getGameAssetManager().getSmgTexture();
    private final Texture revolverTexture = GameAssetManager.getGameAssetManager().getRevolverTexture();
    private final Texture shotgunTexture = GameAssetManager.getGameAssetManager().getShotGunTexture();
    private final Sprite smgSprite = new Sprite(smgTexture);
    private final Sprite revolverSprite = new Sprite(revolverTexture);
    private final Sprite shotgunSprite = new Sprite(shotgunTexture);
    private WeaponType weaponType;
    private int ammo;
    private int damage;
    private int timeReload;
    private int projectile;

    public Weapon(WeaponType weaponType) {
        switch (weaponType.getName()) {
            case "Revolver":
                revolverSprite.setX((float) Gdx.graphics.getWidth() / 2);
                revolverSprite.setY((float) Gdx.graphics.getHeight() / 2);
                revolverSprite.setSize(50, 50);
                break;
            case "Shotgun":
                shotgunSprite.setX((float) Gdx.graphics.getWidth() / 2);
                shotgunSprite.setY((float) Gdx.graphics.getHeight() / 2);
                shotgunSprite.setSize(50, 50);
                break;
            case "SMG":
                smgSprite.setX((float) Gdx.graphics.getWidth() / 2);
                smgSprite.setY((float) Gdx.graphics.getHeight() / 2);
                smgSprite.setSize(50, 50);
                break;
            default:
                revolverSprite.setX((float) Gdx.graphics.getWidth() / 2);
                revolverSprite.setY((float) Gdx.graphics.getHeight() / 2);
                revolverSprite.setSize(50, 50);
                break;
        }
        this.ammo = weaponType.getAmmoMax();
        this.damage = weaponType.getDamage();
        this.timeReload = weaponType.getTimeReload();
        this.projectile = weaponType.getProjectile();
    }

    public Sprite getSmgSprite() {
        return smgSprite;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public Texture getSmgTexture() {
        return smgTexture;
    }

    public Texture getRevolverTexture() {
        return revolverTexture;
    }

    public Texture getShotgunTexture() {
        return shotgunTexture;
    }

    public Sprite getRevolverSprite() {
        return revolverSprite;
    }

    public Sprite getShotgunSprite() {
        return shotgunSprite;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getTimeReload() {
        return timeReload;
    }

    public void setTimeReload(int timeReload) {
        this.timeReload = timeReload;
    }

    public int getProjectile() {
        return projectile;
    }

    public void setProjectile(int projectile) {
        this.projectile = projectile;
    }
}
