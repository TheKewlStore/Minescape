package com.iandavis.minescape.capability.skill;

import com.iandavis.minescape.api.capability.ISkillContainer;
import com.iandavis.minescape.api.utils.CapabilityUtils;
import com.iandavis.minescape.capability.CommonCapabilityProvider;
import com.iandavis.minescape.network.messages.StatsResponseMessage;
import com.iandavis.minescape.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class CapabilitySkills {
    @CapabilityInject(ISkillContainer.class)
    private static Capability<ISkillContainer> skillContainerCapability = null;
    private static final SkillStorage skillStorage = new SkillStorage();

    public static void register() {
        CapabilityManager.INSTANCE.register(ISkillContainer.class, skillStorage, SkillContainer::new);
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer)) {
            return;
        }

        SkillContainer newContainer = new SkillContainer();

        event.addCapability(
                newContainer.getCapabilityID(),
                new CommonCapabilityProvider<>(
                        skillContainerCapability,
                        null,
                        newContainer));
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return;
        }

        CommonProxy.networkWrapper.sendTo(
                new StatsResponseMessage(getSkillContainer(event.player)),
                (EntityPlayerMP) event.player);
    }

    public static ISkillContainer getSkillContainer(ICapabilityProvider provider) {
        return CapabilityUtils.getCapability(provider, skillContainerCapability, null);
    }

    public static Capability<ISkillContainer> getSkillCapability() {
        return skillContainerCapability;
    }
}
