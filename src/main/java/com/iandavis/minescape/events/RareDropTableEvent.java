package com.iandavis.minescape.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RareDropTableEvent extends Event {
    private String itemAwarded;
    private int quantity;
    private EntityPlayer player;

    public RareDropTableEvent(String itemAwarded, int quantity, EntityPlayer player) {
        this.itemAwarded = itemAwarded;
        this.quantity = quantity;
        this.player = player;
    }

    public String getItemAwarded() {
        return itemAwarded;
    }

    public int getQuantity() {
        return quantity;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
