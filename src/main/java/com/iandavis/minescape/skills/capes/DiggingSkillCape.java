package com.iandavis.minescape.skills.capes;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.api.skills.capes.SkillCapeBauble;
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
    public ResourceLocation getCapeTexture() {
        return new ResourceLocation(MinescapeMain.MODID, "textures/entity/baubles/digging_skill_cape.png");
    }
}
