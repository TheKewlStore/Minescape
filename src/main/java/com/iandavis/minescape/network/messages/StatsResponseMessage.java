package com.iandavis.minescape.network.messages;

import com.iandavis.minescape.capability.skill.SkillContainer;
import com.iandavis.minescape.network.MessageID;
import com.iandavis.minescape.network.handlers.StatsResponseHandler;
import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.api.capability.ISkillContainer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class StatsResponseMessage implements IMessage {
    private final ISkillContainer capability;

    public StatsResponseMessage() {
        capability = new SkillContainer();
    }

    public StatsResponseMessage(ISkillContainer capability) {
        this.capability = capability;
    }

    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage((
                        (message, ctx) -> null),
                StatsResponseMessage.class,
                MessageID.StatsResponseMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                StatsResponseHandler.class,
                StatsResponseMessage.class,
                MessageID.StatsResponseMessage.ordinal(),
                Side.CLIENT);
    }

    public ISkillContainer getSkillCapability() {
        return capability;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        capability.deserializePacket(buf);

    }

    @Override
    public void toBytes(ByteBuf buf) {
        capability.serializePacket(buf);
    }
}
