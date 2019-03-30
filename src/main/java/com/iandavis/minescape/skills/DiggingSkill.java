package com.iandavis.minescape.skills;

import com.iandavis.minescape.events.LevelUpEvent;
import com.iandavis.minescape.events.XPGainEvent;
import com.iandavis.minescape.gui.Position;
import com.iandavis.minescape.proxy.ClientProxy;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class DiggingSkill extends BasicSkill {
    private static boolean temporaryHardnessCheck = false;

    @Override
    public String getName() {
        return "Digging";
    }

    @Override
    public SkillIcon getSkillIcon() {
        String texturePath;

        if (getLevel() < 10) {
            texturePath = "minecraft:textures/items/wood_shovel.png";
        } else if (getLevel() < 30) {
            texturePath = "minecraft:textures/items/stone_shovel.png";
        } else if (getLevel() < 50) {
            texturePath = "minecraft:textures/items/iron_shovel.png";
        } else if (getLevel() < 70) {
            texturePath = "minecraft:textures/items/golden_shovel.png";
        } else {
            texturePath = "minecraft:textures/items/diamond_shovel.png";
        }

        return new SkillIcon(
                new ResourceLocation(texturePath),
                new Position(0, 0),
                16,
                16);
    }

    private float getDiggingSpeedModifier() {
        return ((((float) getLevel() / getMaxLevel()) * 30.0f) / 10.0f) + 1.0f;
    }

    @SubscribeEvent
    public static void determineBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (temporaryHardnessCheck) {
            logger.info("Ignoring hardness check for now");
            temporaryHardnessCheck = false;
            return;
        }

        if (event.getEntityPlayer() == null) {
            return;
        }

        if (event.getState().getBlock() != Blocks.DIRT && event.getState().getBlock() != Blocks.GRASS) {
            return;
        }

        DiggingSkill skill;

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            skill = (DiggingSkill) getCapabilityFromEvent(event).getSkill("Digging");
        } else {
            if (ClientProxy.getSkillCapability() == null) {
                return;
            } else {
                skill = (DiggingSkill) ClientProxy.getSkillCapability().getSkill("Digging");
            }
        }

        float newBreakSpeed = event.getOriginalSpeed() * skill.getDiggingSpeedModifier();

        logger.debug(String.format(
                "Original breaking speed is: %f",
                event.getNewSpeed()));
        logger.debug(String.format(
                "Break speed at level %d will be set to: %f",
                skill.getLevel(),
                newBreakSpeed));

        event.setNewSpeed(newBreakSpeed);
    }

    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return;
        }

        logger.info("Received onBreakEvent!");

        if (event.getState().getBlock() == Blocks.DIRT || event.getState().getBlock() == Blocks.GRASS) {
            ISkill skill = getCapabilityFromEvent(event).getSkill("Digging");

            int xpEarned = 10;

            skill.gainXP(xpEarned, event.getPlayer());
        }
    }
}
