package com.iandavis.minescape.skills;

import com.iandavis.minescape.api.skills.BasicSkill;
import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.api.skills.SkillIcon;
import com.iandavis.minescape.api.utils.CapabilityUtils;
import com.iandavis.minescape.api.utils.Position;
import com.iandavis.minescape.api.skills.capes.SkillCapeBauble;
import com.iandavis.minescape.capability.skill.CapabilitySkills;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttackSkill extends BasicSkill {
    @Override
    public String getName() {
        return "Attack";
    }

    @Override
    public SkillIcon getSkillIcon() {
        String texturePath;

        if (getLevel() < 10) {
            texturePath = "minecraft:textures/items/wood_sword.png";
        } else if (getLevel() < 30) {
            texturePath = "minecraft:textures/items/stone_sword.png";
        } else if (getLevel() < 50) {
            texturePath = "minecraft:textures/items/iron_sword.png";
        } else if (getLevel() < 70) {
            texturePath = "minecraft:textures/items/gold_sword.png";
        } else {
            texturePath = "minecraft:textures/items/diamond_sword.png";
        }

        return new SkillIcon(
                new ResourceLocation(texturePath),
                new Position(0,0),
                16,
                16);
    }

    @Override
    public SkillCapeBauble getSkillCape() {
        return null;
    }

    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent event) {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        ISkill attackSkill = CapabilityUtils.getCapability(player, CapabilitySkills.getSkillCapability(), null).getSkill("Attack");

        float skillLevelRatio = ((float) attackSkill.getLevel() / attackSkill.getMaxLevel());

        event.setAmount(
                event.getAmount() +
                event.getAmount() * skillLevelRatio +
                event.getEntityLiving().getHealth() * (0.1f * skillLevelRatio));

        int xpGained = (int) event.getAmount() * 10;
        attackSkill.gainXP(xpGained, player);
    }
}
