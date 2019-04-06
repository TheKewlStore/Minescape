package com.iandavis.minescape.network.messages;

import com.iandavis.minescape.network.MessageID;
import com.iandavis.minescape.network.handlers.NewWorldLoadedHandler;
import com.iandavis.minescape.proxy.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class NewWorldLoadedMessage implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage((
                        (message, ctx) -> null),
                NewWorldLoadedMessage.class,
                MessageID.NewWorldLoadedMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                NewWorldLoadedHandler.class,
                NewWorldLoadedMessage.class,
                MessageID.NewWorldLoadedMessage.ordinal(),
                Side.CLIENT);
    }
}
