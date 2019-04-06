package com.iandavis.minescape.items;

import com.iandavis.minescape.api.events.RareDropTableEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class RareDropTable {
    private final List<Drop> rareDropTable;
    private float currentProbabilityMax;
    private Random randomGenerator;

    public RareDropTable() {
        randomGenerator = new Random(System.currentTimeMillis());
        currentProbabilityMax = 0.0f;
        rareDropTable = new ArrayList<>();
    }

    public boolean shouldGetReward(float chance) {
        float probabilityNumber = randomGenerator.nextFloat();
        logger.info(String.format("Probability roll of getting into rare drop table: %f, required minimum: %f", probabilityNumber, (1 - chance)));
        return probabilityNumber > (1 - chance);
    }

    public Drop getReward(EntityPlayer player) {
        float probabilityNumber = randomGenerator.nextFloat() * currentProbabilityMax;
        Drop actualDrop = null;

        for (Drop drop: rareDropTable) {
            if (probabilityNumber < drop.getProbabilityCutoff()) {
                actualDrop = drop;
                break;
            }
        }

        if (actualDrop == null) {
            logger.warn(String.format("Failed to designate a drop with probability roll %f, assuming this means drop the last registered item!", probabilityNumber));
            actualDrop = rareDropTable.get(rareDropTable.size() - 1);
        } else {
            logger.info(String.format("Probability roll of %f means dropping item %s", probabilityNumber, actualDrop.getItem().getUnlocalizedName()));
        }

        RareDropTableEvent event = new RareDropTableEvent(actualDrop.getItem().delegate.name(), actualDrop.getQuantity(), player);
        MinecraftForge.EVENT_BUS.post(event);

        return actualDrop;
    }

    public void addReward(Item item, Float chance, int quantity) {
        currentProbabilityMax += chance;
        rareDropTable.add(new Drop(item, quantity, currentProbabilityMax));
        rareDropTable.sort(Comparator.comparingInt(Drop::getQuantity));
    }

    public void registerDefaultDrops() {
        addReward(Items.NETHER_STAR, 0.00025f, 1);
        addReward(Items.GOLDEN_APPLE, 0.0007f, 2);
        addReward(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 0.005f, 16);
        addReward(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 0.05f, 64);
        addReward(Item.getItemFromBlock(Blocks.TNT), 0.05f, 8);
        addReward(Item.getItemFromBlock(Blocks.NETHER_WART), 1.0f, 8);
        addReward(Item.getItemFromBlock(Blocks.LAPIS_BLOCK), 1.5f, 16);
        addReward(Item.getItemFromBlock(Blocks.COAL_ORE), 5.0f, 32);
        addReward(Item.getItemFromBlock(Blocks.IRON_ORE), 15.0f, 64);
        addReward(Item.getItemFromBlock(Blocks.PLANKS), 15.0f, 64);
        addReward(Item.getItemFromBlock(Blocks.GRASS), 15.0f, 64);
        addReward(Item.getItemFromBlock(Blocks.GRAVEL), 15.0f, 64);
        addReward(Item.getItemFromBlock(Blocks.STONE), 15.0f, 64);
    }
}
