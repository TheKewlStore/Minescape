package com.iandavis.minescape.skills;

import com.iandavis.minescape.MinescapeMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class CapabilityHandler {

    private static final ResourceLocation SKILLS = new ResourceLocation(MinescapeMain.MODID);

    public CapabilityHandler() {
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer)) {
            return;
        }

        logger.info("Adding minescape skill capability to player");
        event.addCapability(SKILLS, new SkillCapabilityProvider());
    }
}
