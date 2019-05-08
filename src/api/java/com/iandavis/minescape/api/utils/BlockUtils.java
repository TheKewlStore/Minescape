package com.iandavis.minescape.api.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class BlockUtils {
    public static void simulateBlockBreak(BlockPos pos, EntityPlayerMP entityPlayerMP) {
        entityPlayerMP.interactionManager.tryHarvestBlock(pos);
    }
}
