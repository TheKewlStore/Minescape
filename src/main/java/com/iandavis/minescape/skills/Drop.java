package com.iandavis.minescape.skills;

import net.minecraft.item.Item;

public class Drop {
    private int quantity;
    private Item item;

    private float probabilityCutoff;

    public Drop(Item item, int quantity, float probabilityCutoff) {
        this.item = item;
        this.quantity = quantity;
        this.probabilityCutoff = probabilityCutoff;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item getItem() {
        return item;
    }

    public float getProbabilityCutoff() {
        return probabilityCutoff;
    }
}
