package com.iandavis.minescape.proxy;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.commands.CheckXPCommand;
import com.iandavis.minescape.commands.SetLevelCommand;
import com.iandavis.minescape.events.SkillEventHandler;
import com.iandavis.minescape.network.LevelUpMessage;
import com.iandavis.minescape.network.StatsRequestMessage;
import com.iandavis.minescape.network.StatsResponseMessage;
import com.iandavis.minescape.network.XPGainMessage;
import com.iandavis.minescape.skills.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

public class CommonProxy implements Proxy {
    private SkillStorage skillStorage;
    public static Logger logger;
    public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MinescapeMain.MODID);
    private static boolean registeredServer = false;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        skillStorage = new SkillStorage();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        SkillCapability.registerNewSkill(AttackSkill.class);
        SkillCapability.registerNewSkill(DiggingSkill.class);

        MinecraftForge.EVENT_BUS.register(BasicSkill.class);
        MinecraftForge.EVENT_BUS.register(AttackSkill.class);
        MinecraftForge.EVENT_BUS.register(DiggingSkill.class);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        if (registeredServer) {
            ClientProxy.updateSkillCapability();
            return;
        }

        registeredServer = true;
        event.registerServerCommand(new CheckXPCommand());
        event.registerServerCommand(new SetLevelCommand());

        LevelUpMessage.registerServerSide();
        XPGainMessage.registerServerSide();
        StatsResponseMessage.registerServerSide();
        StatsRequestMessage.registerServerSide();

        CapabilityManager.INSTANCE.register(ISkillCapability.class, skillStorage, SkillCapability::new);

        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(AttackSkill.class);
        MinecraftForge.EVENT_BUS.register(DiggingSkill.class);
        MinecraftForge.EVENT_BUS.register(SkillEventHandler.class);

        ClientProxy.updateSkillCapability();
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }
}