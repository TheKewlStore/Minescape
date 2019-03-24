package com.iandavis.runecraft.skills;

/**
 * Define the basic behaviors that all skills in the skill system will expose.
 * The basic approach to utilizing this is to define a class implementing this interface,
 * then register that class with the SkillCapability using the static function registerNewSkill.
 * You'll also want to leverage the LevelUpEvent, XPGainEvent and, if client side interaction is necessary,
 * the LevelUpMessage and XPGainMessage.
 */
public interface ISkill {
    /**
     * Gain the given experience.
     * @param amount The amount of experience to gain.
     */
    void gainXP(int amount);

    /**
     * Set the experience value to the given amount.
     * @param xp The next xp value.
     */
    void setXP(int xp);

    /**
     * Get the current experience value.
     * @return The current experience value.
     */
    int getXP();

    /**
     *
     * @return The current level.
     */
    int getLevel();

    /**
     *
     * @return The experience required to get to the next level.
     */
    int xpToNextLevel();

    /**
     *
     * @return The name of this skill.
     */
    String getName();
}
