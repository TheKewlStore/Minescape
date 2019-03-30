package com.iandavis.minescape.network;

import io.netty.buffer.ByteBuf;

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
}
