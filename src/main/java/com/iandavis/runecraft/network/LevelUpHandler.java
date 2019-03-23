package com.iandavis.runecraft.network;

import com.iandavis.runecraft.gui.SimpleToastInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LevelUpHandler implements IMessageHandler<LevelUpMessage, IMessage> {
    public LevelUpHandler() {
    }

    @Override
    public IMessage onMessage(LevelUpMessage message, MessageContext ctx) {
        String displayMessage = String.format(
                "Gained %s level! Current level is now: %d",
                message.getSkillName(),
                message.getNewLevel());
        ResourceLocation location = new ResourceLocation("minecraft", "entity.player.levelup");

        if (location != null) {
            SoundEvent event = new SoundEvent(location);

            if (event != null) {
                Minecraft.getMinecraft().player.playSound(new SoundEvent(location), 100, 100);
            }
        }

        Minecraft.getMinecraft().getToastGui().add(new SimpleToastInterface(displayMessage));
        return null;
    }
}
