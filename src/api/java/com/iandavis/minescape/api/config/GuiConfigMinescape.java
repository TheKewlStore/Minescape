package com.iandavis.minescape.api.config;

import com.iandavis.minescape.api.utils.Constants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

public class GuiConfigMinescape extends GuiConfig {
    public GuiConfigMinescape(GuiScreen parentScreen, List<IConfigElement> elements) {
        super(
                parentScreen,
                elements,
                Constants.MOD_ID,
                Constants.MOD_ID,
                false,
                false,
                Constants.MOD_NAME);
    }
}
