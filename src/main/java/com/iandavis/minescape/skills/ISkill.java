package com.iandavis.minescape.skills;

import com.iandavis.minescape.gui.MenuInterfaceOverride;
import com.iandavis.minescape.gui.Position;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Define the basic behaviors that all skills in the skill system will expose.
 * The basic approach to utilizing this is to define a class implementing this interface,
 * then register that class with the SkillCapability using the static function registerNewSkill.
 * You'll also want to leverage the LevelUpEvent, XPGainEvent and, if client side interaction is necessary,
 * the LevelUpMessage and XPGainMessage.
 *
 * An important note about custom skill instances, everything must be serializable around the currentXP set via
 * setXP(). This is due to how skill data is serialized via the SkillCapability instance,
 * which stores solely the xp values.
 */
public interface ISkill {
    /**
     * Gain the given experience for the given player.
     * @param amount The amount of experience.
     * @param player The player that gained the experience.
     */
    void gainXP(int amount, EntityPlayer player);

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


    int getMaxLevel();


    void setLevel(int newLevel);


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

    /**
     * Serialize the skill into nbt tag format.
     * @return An NBTTagCompound instance representing this skill.
     */
    NBTTagCompound serializeNBT();

    /**
     * Deserialize the skill from nbt tag format.
     * @param data The NBTTagCompound representing this skill.
     */
    void deserializeNBT(NBTTagCompound data);

    /**
     * Serialize the skill into a packet-based byte buffer.
     */
    void serializePacket(ByteBuf buf);

    /**
     * Deserialize the skill from a packet-based byte buffer.
     * @param buf The buffer with packet data.
     */
    void deserializePacket(ByteBuf buf);

    SkillIcon getSkillIcon();
}
