package com.iandavis.minescape.network;

import com.iandavis.minescape.proxy.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class LevelUpMessage implements IMessage {
    private String skillName;
    private int level;

    public LevelUpMessage() {
    }

    public LevelUpMessage(String newSkillName, int newLevel) {
        skillName = newSkillName;
        level = newLevel;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        skillName = NetworkUtils.readStringFromBuffer(buf);
        level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtils.writeStringToBuffer(buf, skillName);
        buf.writeInt(level);
    }

    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage((
                (message, ctx) -> null),
                LevelUpMessage.class,
                MessageID.LevelUpMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                LevelUpHandler.class,
                LevelUpMessage.class,
                MessageID.LevelUpMessage.ordinal(),
                Side.CLIENT);
    }

    String getSkillName() {
        return skillName;
    }

    int getNewLevel() {
        return level;
    }
}
