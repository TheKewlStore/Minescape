package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.network.messages.NewWorldLoadedMessage;
import com.iandavis.minescape.proxy.ClientProxy;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NewWorldLoadedHandler implements IMessageHandler<NewWorldLoadedMessage, IMessage> {
    @Override
    public IMessage onMessage(NewWorldLoadedMessage message, MessageContext ctx) {
        ClientProxy.handleNewWorldStarted();
        return null;
    }
}
