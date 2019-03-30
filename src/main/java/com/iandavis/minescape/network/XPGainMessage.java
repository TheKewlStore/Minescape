package com.iandavis.minescape.network;

import com.iandavis.minescape.proxy.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.Charset;

public class XPGainMessage implements IMessage {
    private String skillName;
    private int xpGained;

    public XPGainMessage() {

    }

    public XPGainMessage(String newSkillName, int newXpGained) {
        skillName = newSkillName;
        xpGained = newXpGained;
    }

    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage((
                        (message, ctx) -> null),
                XPGainMessage.class,
                MessageID.XPGainMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                XPGainHandler.class,
                XPGainMessage.class,
                MessageID.XPGainMessage.ordinal(),
                Side.CLIENT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int skillNameLength = buf.readInt();
        skillName = buf.readCharSequence(skillNameLength, Charset.defaultCharset()).toString();
        xpGained = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(skillName.length());
        buf.writeCharSequence(skillName, Charset.defaultCharset());
        buf.writeInt(xpGained);
    }

    public String getSkillName() {
        return skillName;
    }

    public int getXpGained() {
        return xpGained;
    }
}
