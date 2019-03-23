package com.iandavis.runecraft.network;

import com.iandavis.runecraft.proxy.CommonProxy;
import com.iandavis.runecraft.skills.ISkill;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.Charset;

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
        int skillNameLength = buf.readInt();
        skillName = buf.readCharSequence(skillNameLength, Charset.defaultCharset()).toString();
        level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(skillName.length());
        buf.writeCharSequence(skillName, Charset.defaultCharset());
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
