package com.iandavis.runecraft.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class StatsResponseHandler implements IMessageHandler<StatsResponseMessage, StatsResponseMessage> {

    private final static Set<BiConsumer<StatsResponseMessage, MessageContext>> currentListeners = new HashSet<>();

    public static void registerSingleShotListener(BiConsumer<StatsResponseMessage, MessageContext> listener) {
        currentListeners.add(listener);
    }

    @Override
    public StatsResponseMessage onMessage(StatsResponseMessage message, MessageContext ctx) {
        for (BiConsumer<StatsResponseMessage, MessageContext> listener: currentListeners) {
            Minecraft.getMinecraft().addScheduledTask(() -> listener.accept(message, ctx));
        }

        currentListeners.clear();
        return null;
    }
}
