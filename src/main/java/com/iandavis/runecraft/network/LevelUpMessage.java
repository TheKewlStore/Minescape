package com.iandavis.runecraft.network;

import com.iandavis.runecraft.proxy.CommonProxy;
import com.iandavis.runecraft.skills.SkillEnum;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.Charset;

public class LevelUpMessage implements IMessage {
    private int skillNameLength;
    private SkillEnum skillName;
    private int newLevel;

    public LevelUpMessage() {

    }

    public LevelUpMessage(SkillEnum skill, int level) {
        skillNameLength = skill.name().length();
        skillName = skill;
        newLevel = level;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        skillNameLength = buf.readInt();
        skillName = SkillEnum.valueOf(buf.readCharSequence(skillNameLength, Charset.defaultCharset()).toString());
        newLevel = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(skillNameLength);
        buf.writeCharSequence(skillName.name(), Charset.defaultCharset());
        buf.writeInt(newLevel);

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

    SkillEnum getSkillName() {
        return skillName;
    }

    int getNewLevel() {
        return newLevel;
    }
}
