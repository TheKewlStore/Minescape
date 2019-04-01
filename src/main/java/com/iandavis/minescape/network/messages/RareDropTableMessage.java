package com.iandavis.minescape.network.messages;

import com.iandavis.minescape.network.MessageID;
import com.iandavis.minescape.network.NetworkUtils;
import com.iandavis.minescape.network.handlers.RareDropTableHandler;
import com.iandavis.minescape.proxy.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class RareDropTableMessage implements IMessage {
    private ResourceLocation location;
    private int quantity;

    public RareDropTableMessage() {
    }

    public RareDropTableMessage(ResourceLocation location, int quantity) {
        this.location = location;
        this.quantity = quantity;
    }

    public static void registerServerSide() {
        CommonProxy.networkWrapper.registerMessage(
                (message, ctx) -> null,
                RareDropTableMessage.class,
                MessageID.RareDropTableMessage.ordinal(),
                Side.SERVER);
    }

    public static void registerClientSide() {
        CommonProxy.networkWrapper.registerMessage(
                RareDropTableHandler.class,
                RareDropTableMessage.class,
                MessageID.RareDropTableMessage.ordinal(),
                Side.CLIENT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location = NetworkUtils.readResourceFromBuffer(buf);
        quantity = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtils.writeResourceToBuffer(buf, location);
        buf.writeInt(quantity);
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public int getQuantity() {
        return quantity;
    }
}
