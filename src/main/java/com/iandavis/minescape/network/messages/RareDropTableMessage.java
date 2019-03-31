package com.iandavis.minescape.network.messages;

import com.iandavis.minescape.network.MessageID;
import com.iandavis.minescape.network.NetworkUtils;
import com.iandavis.minescape.network.handlers.RareDropTableHandler;
import com.iandavis.minescape.proxy.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class RareDropTableMessage implements IMessage {
    private String itemName;
    private int quantity;

    public RareDropTableMessage() {
    }

    public RareDropTableMessage(String itemName, int quantity) {
        this();
        this.itemName = itemName;
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
        itemName = NetworkUtils.readStringFromBuffer(buf);
        quantity = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtils.writeStringToBuffer(buf, itemName);
        buf.writeInt(quantity);
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }
}
