package com.iandavis.minescape.network;

import com.iandavis.minescape.skills.ISkillCapability;
import com.iandavis.minescape.skills.SkillCapabilityProvider;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class StatsRequestHandler implements IMessageHandler<StatsRequestMessage, StatsResponseMessage> {
    @Override
    public StatsResponseMessage onMessage(StatsRequestMessage message, MessageContext ctx) {
        logger.info("Received a StatsRequest message!");

        ISkillCapability skillCapability = ctx.getServerHandler().player.getCapability(SkillCapabilityProvider.skill, null);
        return new StatsResponseMessage(skillCapability);
    }
}
