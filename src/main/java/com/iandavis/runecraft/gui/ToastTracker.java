package com.iandavis.runecraft.gui;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class ToastTracker {
    private final static Deque<SimpleToastInterface> toasts = new LinkedBlockingDeque<>();

    public static void add(SimpleToastInterface toast) {
        toasts.add(toast);
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        for (SimpleToastInterface toast: toasts) {
//            toast.tick();
        }
    }
}
