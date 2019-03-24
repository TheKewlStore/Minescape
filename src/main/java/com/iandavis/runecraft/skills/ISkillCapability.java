package com.iandavis.runecraft.skills;

import java.util.Map;

/**
 * Implement the ability to store skill xp data on minecraft players.
 */
public interface ISkillCapability {
    /**
     * Used for data persistence from the SkillStorage class, set all skill data to the given skill instances.
     * @param skills A map of the skills to their instances.
     */
    void setAllSkills(Map<String, ISkill> skills);

    /**
     * Used for network packet transmission when a client requests all skills from the server for a specific player.
     * This method is necessary because we don't want/need to reinstantiate all the ISkill instances on the client side.
     * @param skillXP A map of the skill names to their xp.
     */
    void setAllSkillXP(Map<String, Integer> skillXP);

    /**
     * Set the experience value for the given skill name.
     * @param skill The name of the skill.
     * @param xp The xp value to set for this skill.
     */
    void setXP(String skill, int xp);

    /**
     * Gain the given experience amount on the given player.
     * @param skill The skill to gain experience in.
     * @param amount The amount of experience to gain.
     */
    void gainXP(String skill, int amount);

    /**
     * Get the experience in the given skill.
     * @param skill The skill to retrieve experience from.
     * @return The experience in the skill given.
     */
    int getXP(String  skill);

    /**
     * Get the level of a given skill.
     * @param skill The skill to retrieve the level.
     * @return The level of the skill.
     */
    int getLevel(String skill);

    /**
     * Get the experience required to the next level of a given skill.
     * @param skill The skill to retrieve the level.
     * @return The level of the skill.
     */
    int xpToNextLevel(String skill);

    /**
     * Return all skill instances.
     * @return A map of the skill names to their instances.
     */
    Map<String, ISkill> getAllSkills();

    /**
     * Return all skill experiences.
     * @return A map of the skill names to their xp values.
     */
    Map<String, Integer> getAllSkillXP();

    /**
     * Return the given skill instance.
     * @param name The name of the skill.
     * @return The skill instance.
     */
    ISkill getSkill(String name);
}
