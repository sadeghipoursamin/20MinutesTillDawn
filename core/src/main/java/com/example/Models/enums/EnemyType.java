package com.example.Models.enums;

public enum EnemyType {
    TREE("TreeMonster", 500, 0),
    TENTACLE_MONSTER("TentacleMonster", 25, 3),
    EYEBAT("EyeBat", 50, 10),
    ELDER("Elder", 400, 5);

    private final String name;
    private final int HP;
    private final int spawnRate;

    EnemyType(String name, int HP, int spawnRate) {
        this.name = name;
        this.HP = HP;
        this.spawnRate = spawnRate;
    }

    public int getHP() {
        return HP;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public String getName() {
        return name;
    }
}
