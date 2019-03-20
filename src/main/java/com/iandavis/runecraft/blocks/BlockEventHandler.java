package com.iandavis.runecraft.blocks;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class BlockEventHandler {

    private final Logger logger;

    public BlockEventHandler(Logger logger) {
        this.logger = logger;
    }

    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {
        logger.info("Got a block break event");
        event.getPlayer().sendMessage(new TextComponentString(String.format("%s Gained 5 XP in digging!", event.getPlayer().getDisplayNameString())));
    }
}
