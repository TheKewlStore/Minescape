package com.iandavis.runecraft.skills;

import com.iandavis.runecraft.events.LevelUpEvent;
import com.iandavis.runecraft.events.XPGainEvent;
import com.iandavis.runecraft.gui.Position;
import com.iandavis.runecraft.proxy.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.iandavis.runecraft.proxy.CommonProxy.logger;

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

    private float getDiggingSpeedModifier(float blockHardness, float playerRelativeBlockHardness) {
        float multiplier = ((float) getLevel() / getMaxLevel()) * 30.0f;
        return (multiplier * blockHardness) / playerRelativeBlockHardness;
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

        float blockHardness = event.getState().getBlockHardness(event.getEntityPlayer().world, event.getPos());
        float newBreakSpeed = event.getNewSpeed();
        float skillLevelRatio = ((float) skill.getLevel() / skill.getMaxLevel()) + 0.05f;

        if (!event.getEntityPlayer().canHarvestBlock(event.getState())) {
            newBreakSpeed *= skillLevelRatio * 100.0f;
        } else {
            newBreakSpeed *= skillLevelRatio * (30.0f * blockHardness);
        }

        if (event.getEntityPlayer().isInsideOfMaterial(Material.WATER)) {
            newBreakSpeed *= skillLevelRatio * 5.0f;
        }

        logger.debug(String.format(
                "Original breaking speed is: %f",
                event.getNewSpeed()));
        logger.debug(String.format(
                "Original block hardness is: %f",
                blockHardness));
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
            int currentLevel = skill.getLevel();
            int xpToNextLevel = skill.xpToNextLevel();

            skill.gainXP(xpEarned);

            XPGainEvent xpEvent = new XPGainEvent(skill, event.getPlayer(), xpEarned);
            MinecraftForge.EVENT_BUS.post(xpEvent);

            if (xpEarned >= xpToNextLevel && currentLevel < skill.getMaxLevel()) {
                LevelUpEvent levelEvent = new LevelUpEvent(skill, event.getPlayer());
                MinecraftForge.EVENT_BUS.post(levelEvent);
            }
        }
    }
}
