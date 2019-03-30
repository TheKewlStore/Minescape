package com.iandavis.minescape.skills;

import com.iandavis.minescape.events.LevelUpEvent;
import com.iandavis.minescape.events.XPGainEvent;
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

    private final int[] xpLevels = new int[]{
            0, 50, 125, 225, 450, 675, 1050, 1430, 2090, 2987, // 10
            3545, 5690, 7835, 9980, 12125, 14270, 16415, 18560, 20705, 22850, // 20
            24995, 25000, 26000, 27000, 28000, 29000, 30000, 31000, 32000, 33000, 34000, // 30
            35000, 36000, 37000, 38000, 39000, 40000, 41000, 42000, 43000, 44000, 45000, // 40
            46000, 47000, 48000, 49000, 50000, 51000, 52000, 53000, 54000, 55000, 56000, // 50
            57000, 58000, 59000, 60000, 61000, 62000, 63000, 64000, 65000, 66000, 67000, // 60
            68000, 69000, 70000, 71000, 72000, 73000, 74000, 75000, 76000, 77000, 78000, // 70
            79000, 80000, 81000, 82000, 83000, 84000, 85000, 86000, 87000, 88000, 89000, // 80
            90000, 90000, 90000, 90000, 90000, 90000, 90000, 90000, 90000, 90000, 90000, // 90
            90000, 90000, 90000, 90000, 90000, 90000, 90000, 90000, 90000, 90000
    };

    public BasicSkill() {
        this.currentXP = 0;
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
        this.currentXP = xp;
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
    public int xpToNextLevel() {
        int currentXP = getXP();

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

    public static ISkillCapability getCapabilityFromPlayer(EntityPlayer player) {
        return player.getCapability(SkillCapabilityProvider.skill, null);
    }

    public static ISkillCapability getCapabilityFromEvent(BlockEvent.BreakEvent event) {
        return getCapabilityFromPlayer(event.getPlayer());
    }

    public static ISkillCapability getCapabilityFromEvent(PlayerEvent event) {
        return getCapabilityFromPlayer(event.getEntityPlayer());
    }

    public static ISkillCapability getCapabilityFromEvent(net.minecraftforge.fml.common.gameevent.PlayerEvent event) {
        return getCapabilityFromPlayer(event.player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        ISkillCapability oldCapability = getCapabilityFromPlayer(event.getOriginal());
        ISkillCapability newCapability = getCapabilityFromEvent(event);
        newCapability.setAllSkills(oldCapability.getAllSkills());
    }
}
