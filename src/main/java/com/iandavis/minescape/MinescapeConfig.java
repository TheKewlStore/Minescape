package com.iandavis.minescape;

import net.minecraftforge.common.config.Config;

@Config(modid=MinescapeMain.MODID)
public class MinescapeConfig {
    @Config.Comment("Set this to false to disable rare drop tables across all skills")
    @Config.Name("AllowGlobalRareDropTables")
    public static boolean allowGlobalRareDropTables = true;

    @Config.Comment("Configuration related to the Digging Skill")
    @Config.Name("Digging")
    public static final DiggingSubCategory diggingSkillCategory = new DiggingSubCategory();

    private static class DiggingSubCategory {
        @Config.Comment("Enable the Digging skill for players")
        @Config.Name("DiggingSkillEnabled")
        public boolean enabled;
        @Config.Comment("The base amount of xp to gain for breaking a block of dirt (before modifiers)")
        @Config.Name("XPDirtBlock")
        public int xpDirt;
    }
}
