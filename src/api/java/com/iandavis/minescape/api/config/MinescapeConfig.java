package com.iandavis.minescape.api.config;

import com.iandavis.minescape.api.skills.BasicSkill;
import com.iandavis.minescape.api.utils.Constants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber
@Config(modid=Constants.MOD_ID)
public class MinescapeConfig {
    @Config.LangKey("minescape.config.allow_global_drop_tables")
    @Config.Comment("Set this to false to disable rare drop tables across all skills")
    @Config.Name("AllowGlobalRareDropTables")
    public static boolean allowGlobalRareDropTables = true;

    @Config.LangKey("minescape.config.digging_subcategory")
    @Config.Comment("Configuration related to the Digging Skill")
    @Config.Name("Digging")
    public static final DiggingSubCategory diggingSkillCategory = new DiggingSubCategory();

    public static class DiggingSubCategory {
        @Config.LangKey("minescape.config.digging_subcategory.enabled")
        @Config.Comment("Enable the Digging skill for players")
        @Config.Name("DiggingSkillEnabled")
        public boolean enabled = true;

        @Config.LangKey("minescape.config.digging_subcategory.allow_rare_drop_tables")
        @Config.Comment("Set this to false to disable rare drop table drops for the Digging skill only")
        @Config.Name("AllowRareDropTablesDigging")
        public boolean enableRareDropTables = true;

        @Config.LangKey("minescape.config.digging_subcategory.xp_dirt")
        @Config.Comment("The base amount of xp to gain for breaking a block of dirt (before modifiers)")
        @Config.Name("XPDirtBlock")
        public int xpDirt = 10;

        @Config.LangKey("minescape.config.digging_subcategory.xp_levels")
        @Config.Comment("XP Requirements for each level of the digging skill (including level 1, additive)")
        @Config.Name("DiggingXPLevels")
        public String[] xpLevels = Arrays.stream(BasicSkill.getXpLevels()).boxed()
                .map(String::valueOf).toArray(String[]::new);

        public Integer[] getXpLevels() {
            return Arrays.stream(xpLevels).map(Integer::parseInt).toArray(Integer[]::new);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if (event.getModID().equals(Constants.MOD_ID)) {
            ConfigManager.sync(Constants.MOD_ID, Config.Type.INSTANCE);
        }
    }
}

