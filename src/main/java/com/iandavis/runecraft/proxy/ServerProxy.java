package com.iandavis.runecraft.proxy;

import com.iandavis.runecraft.events.SkillEventHandler;
import com.iandavis.runecraft.network.StatsRequestMessage;
import com.iandavis.runecraft.network.StatsResponseMessage;
import com.iandavis.runecraft.commands.CheckXPCommand;
import com.iandavis.runecraft.network.LevelUpMessage;
import com.iandavis.runecraft.skills.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy implements Proxy {
    private SkillStorage skillStorage;

    public ServerProxy() {
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        skillStorage = new SkillStorage();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        LevelUpMessage.registerServerSide();
        StatsResponseMessage.registerServerSide();
        StatsRequestMessage.registerServerSide();

        CapabilityManager.INSTANCE.register(ISkillCapability.class, skillStorage, SkillCapability::new);

        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(DiggingSkill.class);
        MinecraftForge.EVENT_BUS.register(SkillEventHandler.class);
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
