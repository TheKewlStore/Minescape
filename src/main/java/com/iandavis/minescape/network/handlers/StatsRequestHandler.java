package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.capability.skill.CapabilitySkills;
import com.iandavis.minescape.network.messages.StatsRequestMessage;
import com.iandavis.minescape.network.messages.StatsResponseMessage;
import com.iandavis.minescape.api.capability.ISkillContainer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class StatsRequestHandler implements IMessageHandler<StatsRequestMessage, StatsResponseMessage> {
    @Override
    public StatsResponseMessage onMessage(StatsRequestMessage message, MessageContext ctx) {
        logger.info("Received a StatsRequest message!");

        ISkillContainer skillCapability = ctx.getServerHandler().player.getCapability(CapabilitySkills.getSkillCapability(), null);
        return new StatsResponseMessage(skillCapability);
    }
}
