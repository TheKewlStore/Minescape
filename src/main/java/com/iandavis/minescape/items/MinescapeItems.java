package com.iandavis.minescape.items;

import com.iandavis.minescape.skills.capes.DiggingSkillCape;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class MinescapeItems {
    @GameRegistry.ObjectHolder("minescape:digging_skill_cape")
    public static final DiggingSkillCape diggingSkillCape = new DiggingSkillCape();

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(diggingSkillCape, 0,
                new ModelResourceLocation(diggingSkillCape.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(diggingSkillCape);
    }
}
