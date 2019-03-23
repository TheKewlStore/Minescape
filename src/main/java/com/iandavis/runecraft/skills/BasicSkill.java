package com.iandavis.runecraft.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

public abstract class BasicSkill implements ISkill {
    protected int currentXP;

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

    public BasicSkill() {
        this.currentXP = 0;
    }

    public BasicSkill(int currentXP) {
        this.currentXP = currentXP;
    }

    @Override
    public void gainXP(int amount) {
        this.currentXP += amount;
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

        return 1;
    }

    @Override
    public int xpToNextLevel() {
        int currentXP = getXP();

        for (int level = 1; level < xpLevels.length; level++) {
            if (xpLevels[level] > currentXP) {
                return level;
            }
        }

        return 1;
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
}
