package com.iandavis.runecraft.skills;

import java.util.HashMap;
import java.util.Map;


public class SkillCapability implements ISkillCapability {
    private final Map<SkillEnum, Integer> skills;

    private final int[] xpLevels = new int[]{
            0,
            100,
            225,
            400,
            675,
            1025,
            1385,
            1795,
            2345,
            2987,
            3545,
            5690,
            7835,
            9980,
            12125,
            14270,
            16415,
            18560,
            20705,
            22850,
            24995
    };

    public SkillCapability() {
        skills = new HashMap<>();
        skills.put(SkillEnum.Agility, 0);
        skills.put(SkillEnum.Attack, 0);
        skills.put(SkillEnum.Alchemy, 0);
        skills.put(SkillEnum.Constitution, 0);
        skills.put(SkillEnum.Construction, 0);
        skills.put(SkillEnum.Cooking, 0);
        skills.put(SkillEnum.Defense, 0);
        skills.put(SkillEnum.Digging, 0);
        skills.put(SkillEnum.Farming, 0);
        skills.put(SkillEnum.Fishing, 0);
        skills.put(SkillEnum.Magic, 0);
        skills.put(SkillEnum.Mining, 0);
        skills.put(SkillEnum.Ranged, 0);
    }

    public SkillCapability(Map<SkillEnum, Integer> newSkills) {
        skills = newSkills;
    }

    @Override
    public void setAllSkills(Map<SkillEnum, Integer> skills) {
        this.skills.putAll(skills);
    }

    @Override
    public void setXP(SkillEnum skill, int xp) {
        this.skills.put(skill, xp);
    }

    @Override
    public boolean gainXP(SkillEnum skill, int amount) {
        int xpToNextLevel = xpToNextLevel(skill);
        this.skills.put(skill, this.skills.get(skill) + amount);

        return amount >= xpToNextLevel;
    }

    @Override
    public int getXP(SkillEnum skill) {
        return this.skills.get(skill);
    }

    @Override
    public int getLevel(SkillEnum skill) {
        int currentXP = getXP(skill);

        for (int level = 1; level < xpLevels.length; level++) {
            if (xpLevels[level] > currentXP) {
                return level;
            }
        }

        return 1;
    }

    @Override
    public int xpToNextLevel(SkillEnum skill) {
        int currentLevel = getLevel(skill);

        if (currentLevel >= (xpLevels.length - 1)) {
            return 0;
        } else {
            return xpLevels[currentLevel] - getXP(skill);
        }
    }

    @Override
    public float getDiggingSpeedModifier() {
        if (getLevel(SkillEnum.Digging) >= 5) {
            return 15.0f;
        }

        return 1.0f;
    }

    @Override
    public Map<SkillEnum, Integer> getAllSkills() {
        return skills;
    }
}
