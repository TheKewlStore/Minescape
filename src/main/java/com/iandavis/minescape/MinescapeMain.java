package com.iandavis.minescape;

import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.proxy.Proxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MinescapeMain.MODID, name = MinescapeMain.NAME, version = MinescapeMain.VERSION)
public class MinescapeMain
{
    public static final String MODID = "minescape";
    public static final String NAME = "minescape";
    public static final String VERSION = "0.1";

    @SidedProxy(
            clientSide="com.iandavis.minescape.proxy.CommonProxy",
            serverSide="com.iandavis.minescape.proxy.CommonProxy")
    private static CommonProxy commonProxy;

    @SidedProxy(
            clientSide="com.iandavis.minescape.proxy.ClientProxy",
            serverSide="com.iandavis.minescape.proxy.ServerProxy")
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
