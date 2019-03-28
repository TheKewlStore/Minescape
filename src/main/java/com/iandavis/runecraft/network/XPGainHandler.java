package com.iandavis.runecraft.network;

import com.iandavis.runecraft.proxy.ClientProxy;
import com.iandavis.runecraft.skills.ISkillCapability;
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

        ISkillCapability skillCapability = ClientProxy.getSkillCapability();
        skillCapability.getSkill(message.getSkillName()).setXP(message.getXpGained());

        return null;
    }
}
