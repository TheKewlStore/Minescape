package com.iandavis.minescape.skills.capes;

import com.iandavis.minescape.MinescapeMain;
import net.minecraft.creativetab.CreativeTabs;
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
    ResourceLocation getCapeTexture() {
        return new ResourceLocation(MinescapeMain.MODID, "textures/entity/baubles/digging_skill_cape.png");
    }
}
