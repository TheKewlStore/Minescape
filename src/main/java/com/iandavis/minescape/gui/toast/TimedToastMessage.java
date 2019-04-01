package com.iandavis.minescape.gui.toast;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;

public abstract class TimedToastMessage implements IToast {
    private int timeToLive;
    private long startTime;
    private boolean newToast = true;

    public TimedToastMessage() {
        this.timeToLive = 2500;
    }

    public TimedToastMessage(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public Visibility draw(GuiToast toastGui, long delta) {
        if (newToast) {
            startTime = delta;
            newToast = false;
        }

        drawToast(toastGui, delta);

        if ((startTime + timeToLive) > delta) {
            return Visibility.SHOW;
        } else {
            return Visibility.HIDE;
        }
    }

    protected abstract void drawToast(GuiToast toastGui, long delta);

    public boolean isFirstDraw() {
        return newToast;
    }
}
