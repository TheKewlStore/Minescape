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
        String title = String.format(
                "Gained %s level!",
                message.getSkillName());
        String body = String.format(
                "Current level: %d",
                message.getNewLevel());
        ResourceLocation location = new ResourceLocation("minecraft", "entity.player.levelup");

        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().player.playSound(
                new SoundEvent(location), 100, 100));
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().getToastGui().add(
                new SimpleToastInterface(title, body)));

        return null;
    }
}
