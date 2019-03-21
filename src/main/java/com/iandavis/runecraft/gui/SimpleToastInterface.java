package com.iandavis.runecraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;

import javax.annotation.Nonnull;

public class SimpleToastInterface implements IToast {
    private String message;
    private int timeToLive = 2500;

    public SimpleToastInterface(String message) {
        this.message = message;
    }

    public SimpleToastInterface(String message, int timeToLive) {
        this(message);
        this.timeToLive = timeToLive;
    }

    @Override
    @Nonnull
    public Visibility draw(@Nonnull GuiToast toastGui, long delta) {
        toastGui.drawCenteredString(Minecraft.getMinecraft().fontRenderer, message, 0, 0, 255);

        if (delta < timeToLive) {
            return Visibility.SHOW;
        } else {
            return Visibility.HIDE;
        }
    }
}
