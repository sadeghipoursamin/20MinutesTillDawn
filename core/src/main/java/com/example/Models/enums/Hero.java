package com.example.Models.enums;

public enum Hero {
    SHANA("SHANA", 4, 4),
    DIAMOND("DIAMOND", 1, 7),
    SCARLET("SCARLET", 3, 5),
    LILITH("LILITH", 5, 3),
    DASHER("DASHER", 2, 10);

    private final String name;
    private final int HP;
    private final int speed;

    Hero(String name, int HP, int speed) {
        this.name = name;
        this.HP = HP;
        this.speed = speed;
    }

    public static Hero getHeroByName(String name) {
        for (Hero hero : Hero.values()) {
            if (hero.getName().equals(name)) {
                return hero;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getHP() {
        return HP;
    }

    public int getSpeed() {
        return speed;
    }
}
