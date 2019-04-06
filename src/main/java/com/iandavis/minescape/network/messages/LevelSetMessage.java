package com.iandavis.minescape.network.messages;

import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.network.MessageID;
import com.iandavis.minescape.api.network.NetworkUtils;
import com.iandavis.minescape.network.handlers.LevelSetHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class LevelSetMessage implements IMessage {
    private int newLevel;
    private String skillName;

    public LevelSetMessage() {

    }

    public LevelSetMessage(String skillName, int newLevel) {
        this.skillName = skillName;
        this.newLevel = newLevel;
    }

    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage((
                (message, ctx) -> null),
                LevelSetMessage.class,
                MessageID.LevelSetMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                LevelSetHandler.class,
                LevelSetMessage.class,
                MessageID.LevelSetMessage.ordinal(),
                Side.CLIENT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.skillName = NetworkUtils.readStringFromBuffer(buf);
        this.newLevel = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtils.writeStringToBuffer(buf, skillName);
        buf.writeInt(newLevel);
    }

    public int getNewLevel() {
        return newLevel;
    }

    public String getSkillName() {
        return skillName;
    }
}
