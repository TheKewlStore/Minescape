package com.iandavis.minescape.skills;

import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.capability.skill.CapabilitySkills;
import com.iandavis.minescape.events.LevelUpEvent;
import com.iandavis.minescape.events.XPGainEvent;
import com.iandavis.minescape.api.capability.ISkillContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Simple Implementation of the ISkill interface which provides all the basic functionality required.
 * For an example of a complete skill based off of this implementation, view the DiggingSkill class.
 */
public abstract class BasicSkill implements ISkill {
    protected int currentXP;

    protected int[] xpLevels = new int[]{
            0, 50, 101, 203, 356, 561, 818, 1127, 1487, 1900, 2365,
            2883, 3454, 4078, 4756, 5487, 6273, 7114, 8010, 8961, 9969,
            11033, 12154, 13333, 14571, 15868, 17225, 18644, 20124, 21667, 23274,
            24946, 26685, 28491, 30367, 32313, 34332, 36425, 38594, 40842, 43170,
            45581, 48078, 50663, 53339, 56110, 58978, 61949, 65025, 68211, 71512,
            74932, 78477, 82152, 85963, 89917, 94022, 98283, 102710, 107311, 112094,
            117071, 122251, 127646, 133268, 139130, 145245, 151630, 158298, 165268, 172558,
            180187, 188177, 196549, 205327, 214537, 224207, 234366, 245044, 256276, 268098,
            280547, 293665, 307496, 322086, 337485, 353748, 370931, 389095, 408306, 428634,
            450153, 472943, 497090, 522685, 549824, 578613, 609161, 641588, 676020
    };

    public BasicSkill() {
        currentXP = 0;
    }

    @Override
    public void gainXP(int amount, EntityPlayer player) {
        int xpToNext = xpToNextLevel();
        int currentLevel = getLevel();

        this.currentXP += amount;

        XPGainEvent xpEvent = new XPGainEvent(this, player, amount);
        MinecraftForge.EVENT_BUS.post(xpEvent);

        if (amount >= xpToNext && currentLevel < getMaxLevel()) {
            LevelUpEvent levelEvent = new LevelUpEvent(this, player);
            MinecraftForge.EVENT_BUS.post(levelEvent);
        }
    }

    @Override
    public void setXP(int xp) {
        currentXP = xp;
    }

    @Override
    public int getXP() {
        return currentXP;
    }

    @Override
    public int getLevel() {
        for (int level = 1; level < xpLevels.length; level++) {
            if (xpLevels[level] > currentXP) {
                return level;
            }
        }

        return 99;
    }

    @Override
    public int xpForLevel(int level) {
        if (level >= getMaxLevel()) {
            return 0;
        }

        return xpLevels[level - 1];
    }

    @Override
    public int xpToNextLevel() {
        int currentXP = getXP();

        if (getLevel() >= getMaxLevel()) {
            return 0;
        }

        for (int level = 1; level < xpLevels.length; level++) {
            if (xpLevels[level] > currentXP) {
                return xpLevels[level] - currentXP;
            }
        }

        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 99;
    }

    @Override
    public void setLevel(int newLevel) {
        currentXP = xpLevels[newLevel - 1];
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("xp", currentXP);
        return data;
    }

    @Override
    public void deserializeNBT(NBTTagCompound data) {
        currentXP = data.getInteger("xp");
    }

    @Override
    public void serializePacket(ByteBuf buf) {
        buf.writeInt(this.getXP());
    }

    @Override
    public void deserializePacket(ByteBuf buf) {
        currentXP = buf.readInt();
    }

    public static ISkillContainer getCapabilityFromPlayer(EntityPlayer player) {
        return player.getCapability(CapabilitySkills.getSkillCapability(), null);
    }

    public static ISkillContainer getCapabilityFromEvent(BlockEvent.BreakEvent event) {
        return getCapabilityFromPlayer(event.getPlayer());
    }

    public static ISkillContainer getCapabilityFromEvent(PlayerEvent event) {
        return getCapabilityFromPlayer(event.getEntityPlayer());
    }

    public static ISkillContainer getCapabilityFromEvent(net.minecraftforge.fml.common.gameevent.PlayerEvent event) {
        return getCapabilityFromPlayer(event.player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        ISkillContainer oldCapability = getCapabilityFromPlayer(event.getOriginal());
        ISkillContainer newCapability = getCapabilityFromEvent(event);
        newCapability.setAllSkills(oldCapability.getAllSkills());
    }
}
