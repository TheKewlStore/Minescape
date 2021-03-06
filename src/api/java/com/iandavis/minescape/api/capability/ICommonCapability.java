package com.iandavis.minescape.api.capability;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;

public interface ICommonCapability {
    void serializePacket(ByteBuf buf);
    void deserializePacket(ByteBuf buf);
    ResourceLocation getCapabilityID();
}
