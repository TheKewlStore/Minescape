package com.iandavis.runecraft.skills;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

public interface ISkillCapability {
    void setAllSkills(Map<String, ISkill> skills);
    void setAllSkillXP(Map<String, Integer> skillXP);
    void setXP(String skill, int xp);
    void gainXP(String skill, int amount, EntityPlayer player);
    int getXP(String  skill);
    int getLevel(String skill);
    int xpToNextLevel(String skill);
    Map<String, ISkill> getAllSkills();
    Map<String, Integer> getAllSkillXP();
    ISkill getSkill(String name);
}
