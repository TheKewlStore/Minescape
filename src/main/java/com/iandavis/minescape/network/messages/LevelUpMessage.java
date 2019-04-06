package com.iandavis.minescape.network.messages;

import com.iandavis.minescape.network.MessageID;
import com.iandavis.minescape.api.network.NetworkUtils;
import com.iandavis.minescape.network.handlers.LevelUpHandler;
import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.api.skills.SkillIcon;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class LevelUpMessage implements IMessage {
    private String skillName;
    private SkillIcon skillIcon;
    private int level;

    public LevelUpMessage() {
    }

    public LevelUpMessage(String skillName, SkillIcon skillIcon, int level) {
        this.skillName = skillName;
        this.skillIcon = skillIcon;
        this.level = level;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        skillName = NetworkUtils.readStringFromBuffer(buf);
        skillIcon = NetworkUtils.readSkillIconFromBuffer(buf);
        level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtils.writeStringToBuffer(buf, skillName);
        NetworkUtils.writeSkillIconToBuffer(buf, skillIcon);
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

    public SkillIcon getSkillIcon() {
        return skillIcon;
    }
}
