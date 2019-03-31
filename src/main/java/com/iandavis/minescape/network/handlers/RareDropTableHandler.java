package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.gui.SimpleToastInterface;
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
        String title = "Rare Drop Table Gain!";
        String body = String.format(
                "Item: %s, Quantity: %d",
                message.getItemName(),
                message.getQuantity());
        ResourceLocation location = new ResourceLocation("minecraft", "block.enchantment_table.use");

        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().player.playSound(
                new SoundEvent(location), 100, 100));
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().getToastGui().add(
                new SimpleToastInterface(title, body)));

        return null;
    }
}
