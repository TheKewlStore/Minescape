package com.iandavis.runecraft.proxy;

import com.iandavis.runecraft.skills.SkillEventHandler;
import com.iandavis.runecraft.commands.CheckXPCommand;
import com.iandavis.runecraft.network.LevelUpMessage;
import com.iandavis.runecraft.skills.CapabilityHandler;
import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapability;
import com.iandavis.runecraft.skills.SkillStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Logger;

public class ServerProxy implements Proxy {
    private static Logger logger;
    private SkillStorage skillStorage;

    public ServerProxy() {
        skillStorage = new SkillStorage(logger);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        LevelUpMessage.registerServerSide();
        CapabilityManager.INSTANCE.register(ISkillCapability.class, skillStorage, SkillCapability::new);

        MinecraftForge.EVENT_BUS.register(new CapabilityHandler(logger));
        MinecraftForge.EVENT_BUS.register(new SkillEventHandler(logger));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CheckXPCommand());
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }
}
