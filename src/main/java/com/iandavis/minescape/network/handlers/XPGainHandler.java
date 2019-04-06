package com.iandavis.minescape.network.handlers;

import com.iandavis.minescape.api.capability.ISkillContainer;
import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.network.messages.XPGainMessage;
import com.iandavis.minescape.proxy.ClientProxy;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class XPGainHandler implements IMessageHandler<XPGainMessage, IMessage> {
    @Override
    public IMessage onMessage(XPGainMessage message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return null;
        }

        ISkillContainer skillCapability = ClientProxy.getSkillCapability();
        ISkill skill = skillCapability.getSkill(message.getSkillName());

        skill.setXP(skill.getXP() + message.getXpGained());
        ClientProxy.setActivelyTrainedSkill(skill);

        return null;
    }
}
