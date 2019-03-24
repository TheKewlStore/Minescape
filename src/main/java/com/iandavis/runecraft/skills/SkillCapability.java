package com.iandavis.runecraft.skills;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.iandavis.runecraft.proxy.CommonProxy.logger;

public class SkillCapability implements ISkillCapability {
    private final static Set<Class<? extends ISkill>> skillClasses = new HashSet<>();
    private final Map<String, ISkill> skills;

    public SkillCapability() {
        skills = new HashMap<>();

        for (Class<? extends ISkill> skillClass: skillClasses) {
            ISkill skill;

            try {
                skill = skillClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Failed to initialize skill instance from class descriptor: " + skillClass.getName(), e);
                continue;
            }

            skills.put(skill.getName(), skill);
        }
    }

    @Override
    public ISkill getSkill(String name) {
        return this.skills.get(name);
    }

    @Override
    public void setAllSkills(Map<String, ISkill> skills) {
        this.skills.putAll(skills);
    }

    @Override
    public void setAllSkillXP(Map<String, Integer> skillXP) {
        for (Map.Entry<String, Integer> entry: skillXP.entrySet()) {
            setXP(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void setXP(String skill, int xp) {
        this.skills.get(skill).setXP(xp);
    }

    @Override
    public void gainXP(String skill, int amount) {
        ISkill skillInstance = this.skills.get(skill);
        skillInstance.gainXP(amount);
    }

    @Override
    public int getXP(String skill) {
        return this.skills.get(skill).getXP();
    }

    @Override
    public int getLevel(String skill) {
        return skills.get(skill).getLevel();
    }

    @Override
    public int xpToNextLevel(String skill) {
        return skills.get(skill).xpToNextLevel();
    }

    @Override
    public Map<String, ISkill> getAllSkills() {
        return skills;
    }

    @Override
    public Map<String, Integer> getAllSkillXP() {
        Map<String, Integer> skillXP = new HashMap<>();

        for (Map.Entry<String, ISkill> entry: skills.entrySet()) {
            skillXP.put(entry.getKey(), entry.getValue().getXP());
        }

        return skillXP;
    }

    /**
     * Static registration method used to register a new skill instance with the skill system.
     * @param skillClass the class of the skill instances to create.
     */
    public static void registerNewSkill(Class<? extends ISkill> skillClass) {
        skillClasses.add(skillClass);
    }
}
