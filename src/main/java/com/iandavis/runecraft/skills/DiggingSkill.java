package com.iandavis.runecraft.skills;

import com.iandavis.runecraft.events.LevelUpEvent;
import com.iandavis.runecraft.events.XPGainEvent;
import com.iandavis.runecraft.network.LevelUpMessage;
import com.iandavis.runecraft.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.iandavis.runecraft.proxy.CommonProxy.logger;

public class DiggingSkill extends BasicSkill {
    @Override
    public String getName() {
        return "Digging";
    }

    private float getDiggingSpeedModifier() {
        if (getLevel() > 15) {
            return 15.0f;
        } else {
            return 1.0f;
        }
    }

    @SubscribeEvent
    public static void determineBreakSpeed(PlayerEvent.BreakSpeed event) {
        logger.info(String.format("Determining break speed for block: %s", event.getState().getBlock().getLocalizedName()));

        if (event.getState().getBlock() != Blocks.DIRT && event.getState().getBlock() != Blocks.GRASS) {
            return;
        }


        DiggingSkill skill = (DiggingSkill) getCapabilityFromEvent(event).getSkill("Digging");

        logger.info(String.format("Setting new break speed to: %f", event.getOriginalSpeed() * skill.getDiggingSpeedModifier()));
        event.setNewSpeed(event.getOriginalSpeed() * skill.getDiggingSpeedModifier());
    }

    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event) {
        logger.info("Got a block break event");
        int xpEarned = 0;
        if (event.getState().getBlock() == Blocks.DIRT || event.getState().getBlock() == Blocks.GRASS) {
            xpEarned = 10;

            ISkill skill = getCapabilityFromEvent(event).getSkill("Digging");

            XPGainEvent xpEvent = new XPGainEvent();
            MinecraftForge.EVENT_BUS.post(xpEvent);

            if (xpEarned > skill.xpToNextLevel()) {
                LevelUpEvent levelEvent = new LevelUpEvent(skill, event.getPlayer());
                MinecraftForge.EVENT_BUS.post(levelEvent);
                CommonProxy.networkWrapper.sendTo(
                        new LevelUpMessage(skill.getName(), skill.getLevel()),
                        (EntityPlayerMP) event.getPlayer());
            }
        }
    }
}
