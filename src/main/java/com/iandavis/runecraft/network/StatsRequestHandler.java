package com.iandavis.runecraft.network;

import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapabilityProvider;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StatsRequestHandler implements IMessageHandler<StatsRequestMessage, StatsResponseMessage> {
    @Override
    public StatsResponseMessage onMessage(StatsRequestMessage message, MessageContext ctx) {
        ISkillCapability skillCapability = ctx.getServerHandler().player.getCapability(SkillCapabilityProvider.skill, null);
        return new StatsResponseMessage(skillCapability.getAllSkills());
    }
}
