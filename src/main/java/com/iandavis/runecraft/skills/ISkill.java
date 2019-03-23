package com.iandavis.runecraft.skills;

public interface ISkill {
    public void gainXP(int amount);
    public void setXP(int xp);
    public int getXP();
    public int getLevel();
}
