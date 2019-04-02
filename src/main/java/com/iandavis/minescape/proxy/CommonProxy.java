package com.iandavis.minescape.proxy;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.commands.CheckXPCommand;
import com.iandavis.minescape.commands.SetLevelCommand;
import com.iandavis.minescape.events.SkillEventHandler;
import com.iandavis.minescape.network.handlers.StatsResponseHandler;
import com.iandavis.minescape.network.messages.*;
import com.iandavis.minescape.skills.*;
import com.iandavis.minescape.skills.capability.CapabilityHandler;
import com.iandavis.minescape.items.RareDropTable;
import com.iandavis.minescape.skills.capability.ISkillCapability;
import com.iandavis.minescape.skills.capability.SkillCapability;
import com.iandavis.minescape.skills.capability.SkillStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

public class CommonProxy implements Proxy {
    private SkillStorage skillStorage;
    public static Logger logger;
    public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MinescapeMain.MODID);
    private static boolean registeredServer = false;
    private static ISkillCapability skillCapability = null;
    private static ISkill activelyTrainedSkill = null;
    private static RareDropTable rareDropTable;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        skillStorage = new SkillStorage();
        rareDropTable = new RareDropTable();
        rareDropTable.registerDefaultDrops();
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
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                updateSkillCapability();
            }
            return;
        }

        registeredServer = true;
        event.registerServerCommand(new CheckXPCommand());
        event.registerServerCommand(new SetLevelCommand());

        LevelUpMessage.registerServerSide();
        XPGainMessage.registerServerSide();
        LevelSetMessage.registerServerSide();
        StatsResponseMessage.registerServerSide();
        StatsRequestMessage.registerServerSide();
        RareDropTableMessage.registerServerSide();

        CapabilityManager.INSTANCE.register(ISkillCapability.class, skillStorage, SkillCapability::new);

        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(AttackSkill.class);
        MinecraftForge.EVENT_BUS.register(DiggingSkill.class);
        MinecraftForge.EVENT_BUS.register(SkillEventHandler.class);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            updateSkillCapability();
        }
    }

    public static void loadSkillCapability(StatsResponseMessage message, MessageContext context) {
        if (message == null || context == null) {
            return;
        }

        skillCapability = message.getSkillCapability();
    }

    public static void updateSkillCapability() {
        StatsResponseHandler.registerSingleShotListener(CommonProxy::loadSkillCapability);
        CommonProxy.networkWrapper.sendToServer(new StatsRequestMessage());
    }

    public static void setActivelyTrainedSkill(ISkill newActiveSkill) {
        activelyTrainedSkill = newActiveSkill;
    }

    public static ISkill getActivelyTrainedSkill() {
        return activelyTrainedSkill;
    }

    public static ISkillCapability getSkillCapability() {
        return skillCapability;
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }

    public static RareDropTable getRareDropTable() {
        return rareDropTable;
    }
}
