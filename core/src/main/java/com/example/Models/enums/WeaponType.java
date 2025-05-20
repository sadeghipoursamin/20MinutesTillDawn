package com.example.Models.enums;

public enum WeaponType {
    REVOLVER("Revolver", 20, 1, 1, 6),
    SHOTGUN("Shotgun", 10, 4, 1, 2),
    SMGS_DUAL("SMG", 8, 1, 2, 24);

    private final String name;
    private final int damage;
    private final int projectile;
    private final int timeReload;
    private final int ammoMax;

    WeaponType(String name, int damage, int projectile, int timeReload, int ammoMax) {
        this.name = name;
        this.damage = damage;
        this.projectile = projectile;
        this.timeReload = timeReload;
        this.ammoMax = ammoMax;
    }

    public static WeaponType getWeaponTypeByName(String name) {
        for (WeaponType weaponType : WeaponType.values()) {
            if (weaponType.getName().equals(name)) {
                return weaponType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getProjectile() {
        return projectile;
    }

    public int getTimeReload() {
        return timeReload;
    }

    public int getAmmoMax() {
        return ammoMax;
    }
}
