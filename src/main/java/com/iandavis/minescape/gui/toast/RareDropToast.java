package com.iandavis.minescape.gui.toast;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class RareDropToast extends TimedToastMessage {
    private ResourceLocation itemTexture;
    private int quantity;

    public RareDropToast(ResourceLocation itemTexture, int quantity) {
        this.itemTexture = itemTexture;
        this.quantity = quantity;
    }

    public RareDropToast(int timeToLive, ResourceLocation itemTexture, int quantity) {
        super(timeToLive);
        this.itemTexture = itemTexture;
        this.quantity = quantity;
    }

    protected void drawToast(GuiToast toastGui, long delta) {
        String title = "Rare Drop Table Drop!";
        String message = String.format("Quantity: %d", quantity);

        if (isFirstDraw()) {
            logger.info(String.format("Drawing item texture using item id: %s", itemTexture.toString()));
        }

        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        toastGui.getMinecraft().fontRenderer.drawString(title, 30, 7, -11534256);
        toastGui.getMinecraft().fontRenderer.drawString(message, 30, 18, -16777216);

        RenderHelper.enableGUIStandardItemLighting();
        toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(Item.getByNameOrId(itemTexture.toString()).getDefaultInstance(), 8, 8);

    }
}
