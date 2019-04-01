package com.iandavis.minescape.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RareDropTableEvent extends Event {
    private ResourceLocation itemLocation;
    private int quantity;
    private EntityPlayer player;

    public RareDropTableEvent(ResourceLocation itemLocation, int quantity, EntityPlayer player) {
        this.itemLocation = itemLocation;
        this.quantity = quantity;
        this.player = player;
    }

    public ResourceLocation getItemLocation() {
        return itemLocation;
    }

    public int getQuantity() {
        return quantity;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
