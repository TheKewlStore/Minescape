package com.iandavis.minescape.proxy;

import com.iandavis.minescape.api.capability.ISkillContainer;
import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.gui.CustomCreativeInventoryScreen;
import com.iandavis.minescape.gui.CustomInventoryScreen;
import com.iandavis.minescape.gui.SkillBarHUD;
import com.iandavis.minescape.network.handlers.StatsResponseHandler;
import com.iandavis.minescape.network.messages.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

@Mod.EventBusSubscriber
public class ClientProxy implements Proxy {
    private static ISkillContainer skillCapability = null;
    private static ISkill activelyTrainedSkill = null;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void init(FMLInitializationEvent event) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        LevelUpMessage.registerClientSide();
        XPGainMessage.registerClientSide();
        LevelSetMessage.registerClientSide();
        StatsRequestMessage.registerClientSide();
        StatsResponseMessage.registerClientSide();
        RareDropTableMessage.registerClientSide();
        NewWorldLoadedMessage.registerClientSide();

        StatsResponseHandler.registerListener(ClientProxy::loadSkillCapability);

        MinecraftForge.EVENT_BUS.register(new SkillBarHUD());
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }

    public static void handleNewWorldStarted() {
        setActivelyTrainedSkill(null);
    }

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiInventory) {
            if (Minecraft.getMinecraft() == null) {
                logger.warn("mc was null!");
                return;
            } else if (Minecraft.getMinecraft().player == null) {
                logger.warn("mc.player was null!");
                return;
            }

            event.setGui(new CustomInventoryScreen(Minecraft.getMinecraft().player));
        } else if (event.getGui() instanceof GuiContainerCreative) {
            if (Minecraft.getMinecraft() == null) {
                logger.warn("mc was null!");
                return;
            } else if (Minecraft.getMinecraft().player == null) {
                logger.warn("mc.player was null!");
                return;
            }

            event.setGui(new CustomCreativeInventoryScreen(Minecraft.getMinecraft().player));
        }
    }

    public static void loadSkillCapability(StatsResponseMessage message, MessageContext context) {
        if (message == null || context == null) {
            return;
        }

        skillCapability = message.getSkillCapability();
    }

    public static void setActivelyTrainedSkill(ISkill newActiveSkill) {
        activelyTrainedSkill = newActiveSkill;
    }

    public static ISkill getActivelyTrainedSkill() {
        return activelyTrainedSkill;
    }

    public static ISkillContainer getSkillCapability() {
        return skillCapability;
    }


}
