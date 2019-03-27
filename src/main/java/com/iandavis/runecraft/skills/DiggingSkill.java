package com.iandavis.runecraft.skills;

import com.iandavis.runecraft.events.LevelUpEvent;
import com.iandavis.runecraft.events.XPGainEvent;
import com.iandavis.runecraft.gui.Position;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DiggingSkill extends BasicSkill {
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
        if (getLevel() > 99) {
            return 9.0f;
        } else if (getLevel() > 90) {
            return 8.5f;
        } else if (getLevel() > 85) {
            return 8.0f;
        } else if (getLevel() > 80) {
            return 7.5f;
        } else if (getLevel() > 70) {
            return 7.0f;
        } else if (getLevel() > 60) {
            return 6.5f;
        } else if (getLevel() > 50) {
            return 6.0f;
        } else if (getLevel() > 45) {
            return 5.5f;
        } else if (getLevel() > 40) {
            return 5.0f;
        } else if (getLevel() > 35) {
            return 4.5f;
        } else if (getLevel() > 30) {
            return 4.0f;
        } else if (getLevel() > 25) {
            return 3.5f;
        } else if (getLevel() > 20) {
            return 3.0f;
        } else if (getLevel() > 15) {
            return 2.5f;
        } else if (getLevel() > 10) {
            return 2.0f;
        } else if (getLevel() > 5) {
            return 1.5f;
        } else {
            return 1.0f;
        }
    }

    @SubscribeEvent
    public static void determineBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getState().getBlock() != Blocks.DIRT && event.getState().getBlock() != Blocks.GRASS) {
            return;
        }

        DiggingSkill skill = (DiggingSkill) getCapabilityFromEvent(event).getSkill("Digging");

        if (event.getState().getBlock() == Blocks.GRASS) {
            event.setNewSpeed(event.getOriginalSpeed() * skill.getDiggingSpeedModifier() * 5);
        } else {
            event.setNewSpeed(event.getOriginalSpeed() * skill.getDiggingSpeedModifier());
        }

    }

    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == Blocks.DIRT || event.getState().getBlock() == Blocks.GRASS) {
            ISkill skill = getCapabilityFromEvent(event).getSkill("Digging");

            int xpEarned = 10;
            int xpToNextLevel = skill.xpToNextLevel();

            skill.gainXP(xpEarned);

            XPGainEvent xpEvent = new XPGainEvent(skill, event.getPlayer(), xpEarned);
            MinecraftForge.EVENT_BUS.post(xpEvent);

            if (xpEarned >= xpToNextLevel) {
                LevelUpEvent levelEvent = new LevelUpEvent(skill, event.getPlayer());
                MinecraftForge.EVENT_BUS.post(levelEvent);
            }
        }
    }
}
