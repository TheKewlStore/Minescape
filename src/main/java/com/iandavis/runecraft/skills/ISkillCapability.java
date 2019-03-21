package com.iandavis.runecraft.skills;

import java.util.Map;

public interface ISkillCapability {
    void setAllSkills(Map<SkillEnum, Integer> skills);
    void setXP(SkillEnum skill, int xp);
    boolean gainXP(SkillEnum skill, int amount);
    int getXP(SkillEnum skill);
    int getLevel(SkillEnum skill);
    int xpToNextLevel(SkillEnum skill);
    float getDiggingSpeedModifier();
    Map<SkillEnum, Integer> getAllSkills();
}
