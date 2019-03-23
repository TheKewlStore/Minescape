package com.iandavis.runecraft.skills;

public interface ISkill {
    void gainXP(int amount);
    void setXP(int xp);
    int getXP();
    int getLevel();
    int xpToNextLevel();
    String getName();
}
