package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.network.messages.LevelSetMessage;
import com.iandavis.minescape.proxy.ClientProxy;
import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.api.capability.ISkillContainer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LevelSetHandler implements IMessageHandler<LevelSetMessage, IMessage> {
    @Override
    public IMessage onMessage(LevelSetMessage message, MessageContext ctx) {
        ISkillContainer skillCapability = ClientProxy.getSkillCapability();
        ISkill skill = skillCapability.getSkill(message.getSkillName());

        skill.setLevel(message.getNewLevel());
        return null;
    }
}
