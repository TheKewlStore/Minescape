package com.iandavis.runecraft.skills;

import com.iandavis.runecraft.network.LevelUpMessage;
import com.iandavis.runecraft.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

public class SkillEventHandler {

    private final Logger logger;

    public SkillEventHandler(Logger logger) {
        this.logger = logger;
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;

        if (SkillCapabilityProvider.skill == null) {
            logger.warn("SkillCapabilityProvider didn't have a proper capability injection");
            return;
        }

        ISkillCapability skill = player.getCapability(SkillCapabilityProvider.skill, null);

        if (skill == null) {
            logger.warn(String.format("Player %s did not have runecraft skill capability, ignoring!", player.getDisplayNameString()));
            return;
        }

        CommonProxy.networkWrapper.sendTo(
                new LevelUpMessage(SkillEnum.Digging, skill.getLevel(SkillEnum.Digging)),
                (EntityPlayerMP) player);
    }

    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {
        logger.info("Got a block break event");
        int xpEarned = 0;
        SkillEnum skillToApply = null;

        if (event.getState().getBlock() == Blocks.DIRT || event.getState().getBlock() == Blocks.GRASS) {
            xpEarned = 10;
            skillToApply = SkillEnum.Digging;
            logger.info("Dirt or Grass broken!");
        } else if (event.getState().getBlock() == Blocks.STONE || event.getState().getBlock() == Blocks.COBBLESTONE) {
            xpEarned = 10;
            skillToApply = SkillEnum.Mining;
        }

        if (SkillCapabilityProvider.skill == null) {
            logger.warn("SkillCapabilityProvider didn't have a proper capability injection");
            return;
        }

        ISkillCapability skillCapability = event.getPlayer().getCapability(SkillCapabilityProvider.skill, null);

        if (skillToApply != null && skillCapability != null) {
            boolean leveledUP = skillCapability.gainXP(skillToApply, xpEarned);

            if (leveledUP) {
                CommonProxy.networkWrapper.sendTo(
                        new LevelUpMessage(skillToApply, skillCapability.getLevel(skillToApply)),
                        (EntityPlayerMP) event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public void determineBreakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        logger.info(String.format("Determining break speed for block: %s", event.getState().getBlock().getLocalizedName()));
        if (event.getState().getBlock() == Blocks.DIRT || event.getState().getBlock() == Blocks.GRASS) {
            ISkillCapability skill = event.getEntityPlayer().getCapability(SkillCapabilityProvider.skill, null);
            logger.info(String.format("Setting new break speed to: %f", event.getOriginalSpeed() * skill.getDiggingSpeedModifier()));
            event.setNewSpeed(event.getOriginalSpeed() * skill.getDiggingSpeedModifier());
        }
    }
}
