package com.iandavis.runecraft;

import com.iandavis.runecraft.proxy.CommonProxy;
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
    public static final String MODID = "runecraft";
    public static final String NAME = "Runecraft";
    public static final String VERSION = "0.1";

    @SidedProxy(
            clientSide="com.iandavis.runecraft.proxy.CommonProxy",
            serverSide="com.iandavis.runecraft.proxy.CommonProxy")
    private static CommonProxy commonProxy;

    @SidedProxy(
            clientSide="com.iandavis.runecraft.proxy.ClientProxy",
            serverSide="com.iandavis.runecraft.proxy.ServerProxy")
    private static Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        commonProxy.preInit(event);
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        commonProxy.init(event);
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        commonProxy.postInit(event);
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        commonProxy.serverStarting(event);
        proxy.serverStarting(event);
    }
}
