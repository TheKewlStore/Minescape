package com.iandavis.minescape.proxy;

import com.iandavis.minescape.gui.CustomCreativeInventoryScreen;
import com.iandavis.minescape.gui.CustomInventoryScreen;
import com.iandavis.minescape.gui.SkillBarHUD;
import com.iandavis.minescape.network.handlers.StatsResponseHandler;
import com.iandavis.minescape.network.messages.*;
import com.iandavis.minescape.skills.SkillCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class ClientProxy implements Proxy {

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

        StatsResponseHandler.registerSingleShotListener(CommonProxy::loadSkillCapability);

        MinecraftForge.EVENT_BUS.register(new SkillBarHUD());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            CommonProxy.networkWrapper.sendTo(
                    new StatsResponseMessage(event.player.getCapability(SkillCapabilityProvider.skill, null)),
                    (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
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


}
