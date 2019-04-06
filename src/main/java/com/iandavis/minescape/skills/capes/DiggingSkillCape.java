package com.iandavis.minescape.skills.capes;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.api.capability.ISkillContainer;
import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.api.skills.capes.SkillCapeBauble;
import com.iandavis.minescape.proxy.ClientProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DiggingSkillCape extends SkillCapeBauble {
    public DiggingSkillCape() {
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName("digging_skill_cape");
        setRegistryName("digging_skill_cape");
        setMaxStackSize(1);
    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase player) {
        if (!(player instanceof EntityPlayer) || ClientProxy.getSkillCapability() == null) {
            return false;
        }

        ISkillContainer container = ClientProxy.getSkillCapability();
        ISkill digging = container.getSkill("Digging");
        return digging.getLevel() >= digging.getMaxLevel();
    }

    @Override
    public ResourceLocation getCapeTexture() {
        return new ResourceLocation(MinescapeMain.MODID, "textures/entity/baubles/digging_skill_cape.png");
    }
}
