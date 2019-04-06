package com.iandavis.minescape.api.utils;

import com.iandavis.minescape.api.skills.SkillIcon;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;

import java.nio.charset.Charset;

public class NetworkUtils {
    public static String readStringFromBuffer(ByteBuf buf) {
        int length = buf.readInt();
        return buf.readCharSequence(length, Charset.defaultCharset()).toString();
    }

    public static void writeStringToBuffer(ByteBuf buf, String message) {
        buf.writeInt(message.length());
        buf.writeCharSequence(message, Charset.defaultCharset());
    }

    public static ResourceLocation readResourceFromBuffer(ByteBuf buf) {
        String domain = readStringFromBuffer(buf);
        String path = readStringFromBuffer(buf);
        return new ResourceLocation(domain, path);
    }

    public static void writeResourceToBuffer(ByteBuf buf, ResourceLocation location) {
        writeStringToBuffer(buf, location.getResourceDomain());
        writeStringToBuffer(buf, location.getResourcePath());
    }

    public static SkillIcon readSkillIconFromBuffer(ByteBuf buf) {
        ResourceLocation location = readResourceFromBuffer(buf);
        Position offset = readPositionFromBuffer(buf);
        int texWidth = buf.readInt();
        int texHeight = buf.readInt();
        return new SkillIcon(location, offset, texWidth, texHeight);
    }

    public static void writeSkillIconToBuffer(ByteBuf buf, SkillIcon icon) {
        writeResourceToBuffer(buf, icon.getTextureLocation());
        writePositionToBuffer(buf, icon.getTextureOffset());
        buf.writeInt(icon.getTexWidth());
        buf.writeInt(icon.getTexHeight());
    }

    public static Position readPositionFromBuffer(ByteBuf buf) {
        float x = buf.readFloat();
        float y = buf.readFloat();
        return new Position(x, y);
    }

    public static void writePositionToBuffer(ByteBuf buf, Position position) {
        buf.writeFloat(position.getX());
        buf.writeFloat(position.getY());
    }
}
