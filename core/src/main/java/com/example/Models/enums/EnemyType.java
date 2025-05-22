package com.example.Models.enums;

public enum EnemyType {
    TREE("TreeMonster", 500),
    TENTACLE_MONSTER("TentacleMonster", 25),
    EYEBAT("EyeBat", 50),
    ELDER("Elder", 400);

    private final String name;
    private final int HP;

    EnemyType(String name, int HP) {
        this.name = name;
        this.HP = HP;
    }

    public int getHP() {
        return HP;
    }

    public String getName() {
        return name;
    }
}
