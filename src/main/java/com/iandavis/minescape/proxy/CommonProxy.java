package com.iandavis.minescape.proxy;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.capability.attackspeed.CapabilityAttackSpeed;
import com.iandavis.minescape.capability.skill.CapabilitySkills;
import com.iandavis.minescape.capability.skill.SkillContainer;
import com.iandavis.minescape.commands.CheckXPCommand;
import com.iandavis.minescape.commands.SetLevelCommand;
import com.iandavis.minescape.events.SkillEventHandler;
import com.iandavis.minescape.items.RareDropTable;
import com.iandavis.minescape.network.messages.*;
import com.iandavis.minescape.skills.AttackSkill;
import com.iandavis.minescape.api.skills.BasicSkill;
import com.iandavis.minescape.skills.DiggingSkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
public class CommonProxy implements Proxy {
    public static Logger logger;
    public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MinescapeMain.MODID);
    private static boolean registeredServer = false;
    private static RareDropTable rareDropTable;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        rareDropTable = new RareDropTable();
        rareDropTable.registerDefaultDrops();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        SkillContainer.registerNewSkill(AttackSkill.class);
        SkillContainer.registerNewSkill(DiggingSkill.class);

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
        NewWorldLoadedMessage.registerServerSide();
        RareDropTableMessage.registerServerSide();

        CapabilitySkills.register();
        CapabilityAttackSpeed.register();

        MinecraftForge.EVENT_BUS.register(AttackSkill.class);
        MinecraftForge.EVENT_BUS.register(DiggingSkill.class);
        MinecraftForge.EVENT_BUS.register(SkillEventHandler.class);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER) {
            return;
        }

        CommonProxy.networkWrapper.sendTo(new NewWorldLoadedMessage(), (EntityPlayerMP) event.player);
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }

    public static RareDropTable getRareDropTable() {
        return rareDropTable;
    }
}
