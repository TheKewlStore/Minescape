package com.iandavis.minescape;

import com.iandavis.minescape.api.config.MinescapeConfig;
import com.iandavis.minescape.api.utils.Constants;
import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.proxy.Proxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid=Constants.MOD_ID, name=Constants.MOD_NAME, version=Constants.MOD_VERSION, guiFactory="com.iandavis.minescape.api.config.GuiConfigFactory")
public class MinescapeMain
{
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
        MinecraftForge.EVENT_BUS.register(new MinescapeConfig());
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
