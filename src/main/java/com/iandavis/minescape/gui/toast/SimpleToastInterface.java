package com.iandavis.minescape.gui.toast;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.renderer.RenderHelper;

public class SimpleToastInterface extends TimedToastMessage {
    private final String title;
    private final String message;

    public SimpleToastInterface(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public SimpleToastInterface(int timeToLive, String title, String message) {
        super(timeToLive);
        this.title = title;
        this.message = message;
    }

    @Override
    protected void drawToast(GuiToast toastGui, long delta) {
        ToastUtils.drawToastBackground(toastGui);
        ToastUtils.renderTitleAndBody(toastGui, title, message);
        RenderHelper.enableGUIStandardItemLighting();
    }
}
