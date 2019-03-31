package com.iandavis.minescape.skills;

import com.iandavis.minescape.gui.Position;
import com.iandavis.minescape.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
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
            texturePath = "minecraft:textures/items/gold_shovel.png";
        } else {
            texturePath = "minecraft:textures/items/diamond_shovel.png";
        }

        return new SkillIcon(
                new ResourceLocation(texturePath),
                new Position(0, 0),
                16,
                16);
    }

    private float getRareDropChance(Item heldItem) {
        float toolBonus = 0.0f;
        float levelModifier = 0.10f * ((float) getLevel() / getMaxLevel());

        if (heldItem == Items.WOODEN_SHOVEL) {
            toolBonus = 0.015f;
        } else if (heldItem == Items.STONE_SHOVEL) {
            toolBonus = 0.025f;
        } else if (heldItem == Items.IRON_SHOVEL) {
            toolBonus = 0.05f;
        } else if (heldItem == Items.GOLDEN_SHOVEL) {
            toolBonus = 0.3f;
        } else if (heldItem == Items.DIAMOND_SHOVEL) {
            toolBonus = 0.15f;
        }

        return toolBonus + levelModifier;
    }

    private boolean isRelatedBlock(Block block) {
        return block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.SAND || block == Blocks.GRAVEL;
    }

    private int xpForBlock(Block block) {
        if (block == Blocks.DIRT || block == Blocks.GRASS) {
            return 10;
        } else if (block == Blocks.GRAVEL) {
            return 15;
        } else if (block == Blocks.SAND) {
            return 7;
        }

        return 0;
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

        DiggingSkill skill;

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            skill = (DiggingSkill) getCapabilityFromEvent(event).getSkill("Digging");
        } else {
            if (CommonProxy.getSkillCapability() == null) {
                return;
            } else {
                skill = (DiggingSkill) CommonProxy.getSkillCapability().getSkill("Digging");
            }
        }

        if (!skill.isRelatedBlock(event.getState().getBlock())) {
            return;
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

        if (event.getState().getBlock() == Blocks.DIRT || event.getState().getBlock() == Blocks.GRASS) {
            DiggingSkill skill = (DiggingSkill) getCapabilityFromEvent(event).getSkill("Digging");

            int xpEarned = skill.xpForBlock(event.getState().getBlock());

            skill.gainXP(xpEarned, event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onHarvestEvent(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() == null) {
            return;
        }

        DiggingSkill skill = (DiggingSkill) getCapabilityFromPlayer(event.getHarvester()).getSkill("Digging");
        RareDropTable table = CommonProxy.getRareDropTable();

        if (table.shouldGetReward(skill.getRareDropChance(event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).getItem()))) {
            Drop drop = table.getReward(event.getHarvester());
            int quantity = drop.getQuantity();
            int maxStackSize = drop.getItem().getDefaultInstance().getMaxStackSize();

            while (quantity >= maxStackSize) {
                event.getDrops().add(new ItemStack(drop.getItem(), maxStackSize));
                quantity -= maxStackSize;
            }

            if (quantity > 0) {
                event.getDrops().add(new ItemStack(drop.getItem(), quantity));
            }
        }
    }
}
