package com.iandavis.minescape.events;

import com.iandavis.minescape.network.LevelUpMessage;
import com.iandavis.minescape.network.XPGainMessage;
import com.iandavis.minescape.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkillEventHandler {
    @SubscribeEvent
    public static void onXPGain(XPGainEvent event) {
        CommonProxy.networkWrapper.sendTo(
                new XPGainMessage(event.getSkill().getName(), event.getXpGained()),
                (EntityPlayerMP) event.getPlayer());
    }

    @SubscribeEvent
    public static void onLevelUp(LevelUpEvent event) {
        CommonProxy.networkWrapper.sendTo(
                new LevelUpMessage(event.getSkill().getName(), event.getSkill().getLevel()),
                (EntityPlayerMP) event.getPlayer());
    }
}