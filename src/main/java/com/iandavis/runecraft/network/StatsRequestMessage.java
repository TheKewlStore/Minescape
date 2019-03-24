package com.iandavis.runecraft.network;

import com.iandavis.runecraft.proxy.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class StatsRequestMessage implements IMessage {
    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage(
                StatsRequestHandler.class,
                StatsRequestMessage.class,
                MessageID.StatsRequestMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                (message, ctx) -> null,
                StatsRequestMessage.class,
                MessageID.StatsRequestMessage.ordinal(),
                Side.CLIENT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }
}
