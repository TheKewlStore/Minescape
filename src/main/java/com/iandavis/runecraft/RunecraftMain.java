package com.iandavis.runecraft;

import com.iandavis.runecraft.proxy.Proxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = RunecraftMain.MODID, name = RunecraftMain.NAME, version = RunecraftMain.VERSION)
public class RunecraftMain
{
    static final String MODID = "runecraft";
    static final String NAME = "Runecraft";
    static final String VERSION = "0.1";

    @SidedProxy(
            clientSide="com.iandavis.runecraft.proxy.ClientProxy",
            serverSide="com.iandavis.runecraft.proxy.ServerProxy")
    private static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        proxy.serverStarting(event);
    }
}
