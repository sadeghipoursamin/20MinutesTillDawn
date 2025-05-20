package com.example.Models.enums;

public enum EnemyType {
    TREE(500, 0),
    TENTACLE_MONSTER(25, 3),
    EYEBAT(50, 10),
    ELDER(400, 5);

    private final int HP;
    private final int spownRate;

    EnemyType(int HP, int spownRate) {
        this.HP = HP;
        this.spownRate = spownRate;
    }

    public int getHP() {
        return HP;
    }

    public int getSpownRate() {
        return spownRate;
    }
}
