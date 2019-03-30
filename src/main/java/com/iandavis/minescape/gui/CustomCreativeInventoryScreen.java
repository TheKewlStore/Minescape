package com.iandavis.minescape.gui;

import com.iandavis.minescape.MinescapeMain;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class CustomCreativeInventoryScreen extends GuiContainerCreative {
    private final SkillsTabScreen skillsTabScreen;
    private GuiButton skillsButton;

    public CustomCreativeInventoryScreen(EntityPlayer player) {
        super(player);
        skillsTabScreen = new SkillsTabScreen(this);
    }

    @Override
    public void initGui() {
        super.initGui();
        skillsButton = new GuiButtonImage(
                SkillsTabScreen.ButtonIDs.SkillsButton.ordinal(),
                this.guiLeft + 175,
                this.guiTop + 3,
                14, // width
                12, // height
                1, // tex x
                168, // tex y
                0, // hover tex y
                new ResourceLocation(MinescapeMain.MODID, "textures/gui/skills.png"));
        buttonList.add(skillsButton);
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        if (button != skillsButton) {
            super.actionPerformed(button);
        } else {
            this.onGuiClosed();
            mc.displayGuiScreen(skillsTabScreen);
        }
    }
}
