package com.iandavis.minescape.events;

import com.iandavis.minescape.api.events.LevelUpEvent;
import com.iandavis.minescape.api.events.RareDropTableEvent;
import com.iandavis.minescape.api.events.XPGainEvent;
import com.iandavis.minescape.items.MinescapeItems;
import com.iandavis.minescape.network.messages.LevelUpMessage;
import com.iandavis.minescape.network.messages.RareDropTableMessage;
import com.iandavis.minescape.network.messages.XPGainMessage;
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
        if (event.getSkill().getLevel() >= event.getSkill().getMaxLevel()) {
            event.getPlayer().inventory.addItemStackToInventory(MinescapeItems.diggingSkillCape.getDefaultInstance());
        }

        CommonProxy.networkWrapper.sendTo(
                new LevelUpMessage(
                        event.getSkill().getName(),
                        event.getSkill().getSkillIcon(),
                        event.getSkill().getLevel()),
                (EntityPlayerMP) event.getPlayer());
    }

    @SubscribeEvent
    public static void onRareDropTable(RareDropTableEvent event) {
        CommonProxy.networkWrapper.sendTo(
                new RareDropTableMessage(event.getItemLocation(), event.getQuantity()),
                (EntityPlayerMP) event.getPlayer());
    }
}
