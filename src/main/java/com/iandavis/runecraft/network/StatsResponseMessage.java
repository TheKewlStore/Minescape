package com.iandavis.runecraft.network;

import com.iandavis.runecraft.proxy.CommonProxy;
import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapability;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class StatsResponseMessage implements IMessage {
    private ISkillCapability capability;

    public StatsResponseMessage() {
        capability = new SkillCapability();
    }

    public StatsResponseMessage(ISkillCapability capability) {
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

    public ISkillCapability getSkillCapability() {
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
