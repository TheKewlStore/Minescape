package com.iandavis.runecraft.network;

import com.iandavis.runecraft.proxy.CommonProxy;
import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapability;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class StatsResponseMessage implements IMessage {
    private Map<String, Integer> skills;

    public StatsResponseMessage() {
        skills = new HashMap<>();
    }

    public StatsResponseMessage(Map<String, Integer> newSkills) {
        skills = newSkills;
    }

    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage((
                        (message, ctx) -> null),
                StatsResponseMessage.class,
                MessageID.StatsResponseMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                StatsResponseHandler.class,
                StatsResponseMessage.class,
                MessageID.StatsResponseMessage.ordinal(),
                Side.CLIENT);
    }

    public ISkillCapability getSkillCapability() {
        ISkillCapability capability = new SkillCapability();
        capability.setAllSkillXP(skills);
        return capability;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int numberOfSkills = buf.readInt();

        ISkillCapability skillCapability = new SkillCapability();

        for (int i=0; i < numberOfSkills; i++) {
            int lengthOfSkillName = buf.readInt();
            String skillName = buf.readCharSequence(lengthOfSkillName, Charset.defaultCharset()).toString();
            int experience = buf.readInt();
            skillCapability.setXP(skillName, experience);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(skills.size());

        for (Map.Entry<String, Integer> entry: skills.entrySet()) {
            buf.writeInt(entry.getKey().length());
            buf.writeCharSequence(entry.getKey(), Charset.defaultCharset());
            buf.writeInt(entry.getValue());
        }
    }
}
