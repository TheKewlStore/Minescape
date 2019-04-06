package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.network.messages.StatsResponseMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class StatsResponseHandler implements IMessageHandler<StatsResponseMessage, StatsResponseMessage> {
    private final static Set<BiConsumer<StatsResponseMessage, MessageContext>> currentListeners = new HashSet<>();

    public static void registerListener(BiConsumer<StatsResponseMessage, MessageContext> listener) {
        currentListeners.add(listener);
    }

    @Override
    public StatsResponseMessage onMessage(StatsResponseMessage message, MessageContext ctx) {
        logger.info("Received a StatsResponse message!");

        for (BiConsumer<StatsResponseMessage, MessageContext> listener: currentListeners) {
            Minecraft.getMinecraft().addScheduledTask(() -> listener.accept(message, ctx));
        }

        return null;
    }
}
