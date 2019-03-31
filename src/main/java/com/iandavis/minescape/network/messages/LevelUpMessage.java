package com.iandavis.minescape.network.messages;

import com.iandavis.minescape.network.MessageID;
import com.iandavis.minescape.network.NetworkUtils;
import com.iandavis.minescape.network.handlers.LevelUpHandler;
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

    public String getSkillName() {
        return skillName;
    }

    public int getNewLevel() {
        return level;
    }
}
