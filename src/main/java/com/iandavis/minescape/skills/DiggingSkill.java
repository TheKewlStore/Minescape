package com.iandavis.minescape.skills;

import com.iandavis.minescape.api.config.MinescapeConfig;
import com.iandavis.minescape.api.skills.BasicSkill;
import com.iandavis.minescape.api.skills.SkillIcon;
import com.iandavis.minescape.api.skills.capes.SkillCapeBauble;
import com.iandavis.minescape.api.utils.BlockUtils;
import com.iandavis.minescape.api.utils.CapabilityUtils;
import com.iandavis.minescape.api.utils.ItemUtils;
import com.iandavis.minescape.api.utils.Position;
import com.iandavis.minescape.capability.skill.CapabilitySkills;
import com.iandavis.minescape.items.Drop;
import com.iandavis.minescape.items.MinescapeItems;
import com.iandavis.minescape.items.RareDropTable;
import com.iandavis.minescape.proxy.ClientProxy;
import com.iandavis.minescape.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class DiggingSkill extends BasicSkill {
    private Set<BlockPos> blocksVisited = new HashSet<>();
    private Set<BlockPos> blockChainToBreak = new HashSet<>();
    private boolean ignoreBreakEvents = false;
    private final Random randomGenerator = new Random(System.currentTimeMillis());

    private Set<ItemStack> getRelatedTools() {
        Set<ItemStack> relatedTools = new HashSet<>();

        relatedTools.add(Items.WOODEN_SHOVEL.getDefaultInstance());
        relatedTools.add(Items.STONE_SHOVEL.getDefaultInstance());
        relatedTools.add(Items.IRON_SHOVEL.getDefaultInstance());
        relatedTools.add(Items.GOLDEN_SHOVEL.getDefaultInstance());
        relatedTools.add(Items.DIAMOND_SHOVEL.getDefaultInstance());

        return relatedTools;
    }

    private Set<ItemStack> getUsableTools() {
        Set<ItemStack> usableTools = new HashSet<>();

        if (getLevel() >= 1) {
            usableTools.add(Items.WOODEN_SHOVEL.getDefaultInstance());
        }

        if (getLevel() >= 15) {
            usableTools.add(Items.STONE_SHOVEL.getDefaultInstance());
        }

        if (getLevel() >= 30) {
            usableTools.add(Items.IRON_SHOVEL.getDefaultInstance());
        }

        if (getLevel() >= 60) {
            usableTools.add(Items.GOLDEN_SHOVEL.getDefaultInstance());
        }

        if (getLevel() >= 80) {
            usableTools.add(Items.DIAMOND_SHOVEL.getDefaultInstance());
        }

        return usableTools;
    }

    @SubscribeEvent
    public static void determineBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getEntityPlayer() == null || !MinescapeConfig.diggingSkillCategory.enabled) {
            return;
        }

        DiggingSkill skill;

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            skill = (DiggingSkill) CapabilityUtils.getCapability(event.getEntityPlayer(), CapabilitySkills.getSkillCapability()).getSkill("Digging");
        } else {
            if (ClientProxy.getSkillCapability() == null) {
                return;
            } else {
                skill = (DiggingSkill) ClientProxy.getSkillCapability().getSkill("Digging");
            }
        }

        if (!skill.isRelatedBlock(event.getState().getBlock())) {
            return;
        }

        Set<ItemStack> relatedTools = skill.getRelatedTools();
        Set<ItemStack> usableTools = skill.getUsableTools();

        ItemStack mainHand = event.getEntityPlayer().getHeldItemMainhand();
        ItemStack offHand = event.getEntityPlayer().getHeldItemOffhand();

        if (ItemUtils.setContainsItemStack(mainHand, relatedTools) || ItemUtils.setContainsItemStack(offHand, relatedTools)) {
            if (!ItemUtils.setContainsItemStack(mainHand, usableTools) && !ItemUtils.setContainsItemStack(offHand, usableTools)) {
                event.setCanceled(true);
                return;
            }
        }

        float newBreakSpeed = event.getOriginalSpeed() * skill.getDiggingSpeedModifier();

        if (skill.getSkillCape().isEquipped(event.getEntityPlayer())) {
            logger.info("Player that broke a block has the digging cape equipped!");
            newBreakSpeed *= 10;
        }

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
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT
                || !MinescapeConfig.diggingSkillCategory.enabled) {
            return;
        }

        DiggingSkill skill = (DiggingSkill) CapabilityUtils.getCapability(
                event.getPlayer(),
                CapabilitySkills.getSkillCapability()).getSkill("Digging");

        if (skill.isRelatedBlock(event.getState().getBlock())) {
            int xpEarned = skill.xpForBlock(event.getState().getBlock());
            skill.gainXP(xpEarned, event.getPlayer());
            skill.breakBlockChain(event);

            Set<ItemStack> usableTools = skill.getUsableTools();

            ItemStack mainHand = event.getPlayer().getHeldItemMainhand();
            ItemStack offHand = event.getPlayer().getHeldItemOffhand();

            if (ItemUtils.setContainsItemStack(mainHand, usableTools)) {
                skill.handleDurabilitySave(mainHand);
            } else if (ItemUtils.setContainsItemStack(offHand, usableTools)) {
                skill.handleDurabilitySave(offHand);
            }
        }
    }

    private void handleDurabilitySave(ItemStack tool) {
        float probabilityRoll = randomGenerator.nextFloat();
        logger.info(String.format("Probability roll of saving tool durability: %f, required minimum: %f",
                probabilityRoll, (1 - getDurabilitySavedChance())));

        int toolDurability = tool.getItemDamage();

        if (probabilityRoll > (1 - getDurabilitySavedChance())) {
            logger.info("Saving durability on tool");
            tool.setItemDamage(tool.getItemDamage() - 1);
        }

        logger.info(String.format("Tool durability before: %d and after: %d", toolDurability, tool.getItemDamage()));
    }

    @SubscribeEvent
    public static void onHarvestEvent(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() == null || !MinescapeConfig.allowGlobalRareDropTables || !MinescapeConfig.diggingSkillCategory.enabled) {
            return;
        }

        DiggingSkill skill = (DiggingSkill) CapabilityUtils.getCapability(
                event.getHarvester(),
                CapabilitySkills.getSkillCapability()).getSkill("Digging");
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

    @Override
    public SkillCapeBauble getSkillCape() {
        return MinescapeItems.diggingSkillCape;
    }

    private float getDurabilitySavedChance() {
        return 0.5f * ((float) getLevel() / getMaxLevel());
    }

    private float getRareDropChance(Item heldItem) {
        if (!MinescapeConfig.allowGlobalRareDropTables || !MinescapeConfig.diggingSkillCategory.enabled) {
            return 0.0f;
        }

        float toolBonus = 0.0f;
        float levelModifier = 0.05f * ((float) getLevel() / getMaxLevel());

        if (heldItem == Items.WOODEN_SHOVEL) {
            toolBonus = 0.0001f;
        } else if (heldItem == Items.STONE_SHOVEL) {
            toolBonus = 0.005f;
        } else if (heldItem == Items.IRON_SHOVEL) {
            toolBonus = 0.01f;
        } else if (heldItem == Items.GOLDEN_SHOVEL) {
            toolBonus = 0.05f;
        } else if (heldItem == Items.DIAMOND_SHOVEL) {
            toolBonus = 0.025f;
        }

        return toolBonus + levelModifier;
    }

    private boolean canBreakGravelColumns() {
        return this.getLevel() > 90;
    }

    private boolean canBreakSandColumns() {
        return this.getLevel() > 75;
    }

    private void checkBlockForChain(BlockPos blockPos, IBlockState blockState) {
        Block block = blockState.getBlock();
        blocksVisited.add(blockPos);

        if (block != Blocks.SAND && block != Blocks.GRAVEL) {
            return;
        }

        if (canBreakSandColumns() && block == Blocks.SAND) {
            blockChainToBreak.add(blockPos);
        }

        if (canBreakGravelColumns() && block == Blocks.GRAVEL) {
            blockChainToBreak.add(blockPos);
        }

        World world = Minecraft.getMinecraft().world;

        if (!blocksVisited.contains(blockPos.up())) {
            checkBlockForChain(blockPos.up(), world.getBlockState(blockPos.up()));
        }

        if (!blocksVisited.contains(blockPos.down())) {
            checkBlockForChain(blockPos.down(), world.getBlockState(blockPos.down()));
        }

        if (!blocksVisited.contains(blockPos.east())) {
            checkBlockForChain(blockPos.east(), world.getBlockState(blockPos.east()));
        }

        if (!blocksVisited.contains(blockPos.west())) {
            checkBlockForChain(blockPos.west(), world.getBlockState(blockPos.west()));
        }

        if (!blocksVisited.contains(blockPos.north())) {
            checkBlockForChain(blockPos.north(), world.getBlockState(blockPos.north()));
        }

        if (!blocksVisited.contains(blockPos.south())) {
            checkBlockForChain(blockPos.south(), world.getBlockState(blockPos.south()));
        }
    }

    private void breakBlockChain(BlockEvent.BreakEvent event) {
        Block block = event.getState().getBlock();

        if ((block != Blocks.SAND && block != Blocks.GRAVEL) || blockChainToBreak == null || ignoreBreakEvents) {
            return;
        }

        checkBlockForChain(event.getPos(), event.getState());

        ignoreBreakEvents = true;

        for (BlockPos blockPos : blockChainToBreak) {
            BlockUtils.simulateBlockBreak(blockPos, (EntityPlayerMP) event.getPlayer());
        }

        ignoreBreakEvents = false;
        blocksVisited = new HashSet<>();
        blockChainToBreak = new HashSet<>();
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
}
