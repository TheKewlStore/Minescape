package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.gui.toast.LevelUpToast;
import com.iandavis.minescape.gui.toast.SimpleToastInterface;
import com.iandavis.minescape.network.messages.LevelUpMessage;
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

        ResourceLocation location = new ResourceLocation("minecraft", "entity.player.levelup");

        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().player.playSound(
                new SoundEvent(location), 100, 100));
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().getToastGui().add(
                new LevelUpToast(message.getSkillIcon(), message.getSkillName(), message.getNewLevel())));

        return null;
    }
}
