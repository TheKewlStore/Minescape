package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.gui.toast.RareDropToast;
import com.iandavis.minescape.network.messages.RareDropTableMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RareDropTableHandler implements IMessageHandler<RareDropTableMessage, IMessage> {
    @Override
    public IMessage onMessage(RareDropTableMessage message, MessageContext ctx) {
        ResourceLocation location = new ResourceLocation("minecraft", "block.enchantment_table.use");

        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().player.playSound(
                new SoundEvent(location), 100, 100));
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().getToastGui().add(
                new RareDropToast(message.getLocation(), message.getQuantity())));

        return null;
    }
}
