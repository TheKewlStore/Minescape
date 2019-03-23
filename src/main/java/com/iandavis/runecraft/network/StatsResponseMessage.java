package com.iandavis.runecraft.network;

import com.iandavis.runecraft.proxy.CommonProxy;
import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapability;
import com.iandavis.runecraft.skills.SkillEnum;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class StatsResponseMessage implements IMessage {
    private Map<SkillEnum, Integer> skills;

    public StatsResponseMessage() {
        skills = new HashMap<>();
    }

    public StatsResponseMessage(Map<SkillEnum, Integer> newSkills) {
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
        capability.setAllSkills(skills);
        return capability;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int numberOfSkills = buf.readInt();

        for (int i=0; i < numberOfSkills; i++) {
            int lengthOfSkillName = buf.readInt();
            SkillEnum skillName = SkillEnum.valueOf(
                    buf.readCharSequence(lengthOfSkillName,
                            Charset.defaultCharset()).toString());
            Integer experience = buf.readInt();
            skills.put(skillName, experience);
        }

        ISkillCapability skillCapability = new SkillCapability();
        skillCapability.setAllSkills(skills);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(skills.size());

        for (Map.Entry<SkillEnum, Integer> entry: skills.entrySet()) {
            buf.writeInt(entry.getKey().name().length());
            buf.writeCharSequence(entry.getKey().name(), Charset.defaultCharset());
            buf.writeInt(entry.getValue());
        }
    }
}
