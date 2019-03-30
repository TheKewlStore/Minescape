package com.iandavis.minescape.gui;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

import javax.annotation.Nonnull;

public class SimpleToastInterface implements IToast {
    private String title;
    private String message;
    private int timeToLive;
    private long startTime;
    private boolean newToast = true;

    public SimpleToastInterface(String title, String message) {
        this.title = title;
        this.message = message;
        this.timeToLive = 2500;
    }

    public SimpleToastInterface(String title, String message, int timeToLive) {
        this.title = title;
        this.message = message;
        this.timeToLive = timeToLive;
    }

    @Override
    @Nonnull
    public Visibility draw(@Nonnull GuiToast toastGui, long delta) {
        if (newToast) {
            startTime = delta;
            newToast = false;
        }

        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        toastGui.getMinecraft().fontRenderer.drawString(title, 30, 7, -11534256);
        toastGui.getMinecraft().fontRenderer.drawString(message, 30, 18, -16777216);
        RenderHelper.enableGUIStandardItemLighting();

        if ((startTime + timeToLive) > delta) {
            return Visibility.SHOW;
        } else {
            return Visibility.HIDE;
        }
    }
}
