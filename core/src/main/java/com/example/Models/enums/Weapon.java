package com.example.Models.enums;

public enum Weapon {
    REVOLVER("Revolver",20,1,1,6),
    SHOTGUN("Shotgun",10,4,1,2),
    SMGS_DUAL("SMG's Dual", 8,1,2,24);

    private final String name;
    private final int damage;
    private final int projectile;
    private final int timeReload;
    private final int ammoMax;

    Weapon(String name, int damage, int projectile, int timeReload, int ammoMax) {
        this.name = name;
        this.damage = damage;
        this.projectile = projectile;
        this.timeReload = timeReload;
        this.ammoMax = ammoMax;
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
