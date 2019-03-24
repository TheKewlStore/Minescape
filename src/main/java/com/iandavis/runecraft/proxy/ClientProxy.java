package com.iandavis.runecraft.proxy;

import com.iandavis.runecraft.gui.MenuInterfaceOverride;
import com.iandavis.runecraft.network.LevelUpMessage;
import com.iandavis.runecraft.network.StatsRequestMessage;
import com.iandavis.runecraft.network.StatsResponseMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Logger;

public class ClientProxy implements Proxy {
    private static Logger logger;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

    }

    @Override
    public void init(FMLInitializationEvent event) {
        LevelUpMessage.registerClientSide();
        StatsRequestMessage.registerClientSide();
        StatsResponseMessage.registerClientSide();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiInventory) {
            if (Minecraft.getMinecraft() == null) {
                logger.warn("mc was null!");
                return;
            } else if (Minecraft.getMinecraft().player == null) {
                logger.warn("mc.player was null!");
                return;
            }

            event.setGui(new MenuInterfaceOverride(Minecraft.getMinecraft().player));
        }
    }
}
