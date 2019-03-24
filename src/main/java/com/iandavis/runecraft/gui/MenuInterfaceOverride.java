package com.iandavis.runecraft.gui;

import com.iandavis.runecraft.RunecraftMain;
import com.iandavis.runecraft.network.StatsRequestMessage;
import com.iandavis.runecraft.network.StatsResponseHandler;
import com.iandavis.runecraft.network.StatsResponseMessage;
import com.iandavis.runecraft.proxy.CommonProxy;
import com.iandavis.runecraft.skills.ISkillCapability;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.util.Map;

public class MenuInterfaceOverride extends GuiInventory {
    private final ResourceLocation SKILLS_TEXTURE = new ResourceLocation(RunecraftMain.MODID, "textures/gui/skills.png");
    private GuiButton skillsButton;
    private boolean skillsTabActive = false;
    private ISkillCapability skillCapability = null;

    private enum ButtonIDs {
        SkillsButton
    }

    public MenuInterfaceOverride(EntityPlayer player) {
        super(player);
        skillsButton = new GuiButton(
                ButtonIDs.SkillsButton.ordinal(),
                this.guiLeft + 175,
                this.height / 2 - 46,
                50,
                20,
                "Skills");
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        super.initGui();

        skillsButton.x = this.guiLeft + 175;
        skillsButton.y = this.guiTop;
        skillsButton.width = 50;
        skillsButton.height = 20;

        this.buttonList.add(skillsButton);

        StatsResponseHandler.registerSingleShotListener(this::loadSkillCapability);
        CommonProxy.networkWrapper.sendToServer(new StatsRequestMessage());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (skillsTabActive) {
            skillsButton.displayString = "Back";
            drawSkillsScreen(mouseX, mouseY, partialTicks);
        } else {
            skillsButton.displayString = "Skills";
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    private void drawSkillsScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        skillsButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(SKILLS_TEXTURE);
        super.drawTexturedModalRect(
                this.guiLeft,
                this.guiTop,
                0,
                0,
                175,
                164);

        Color color = new Color(255, 144, 76, 255);

        if (skillCapability == null) {
            super.drawString(this.mc.fontRenderer,
                    "Loading Player Stats from server...",
                    this.guiLeft + 30,
                    this.guiTop + 8,
                    color.getIntValue());
            return;
        }

        for (Map.Entry<String, Integer> entry: skillCapability.getAllSkillXP().entrySet()) {
            drawSkillLevel(entry.getKey(), skillCapability.getLevel(entry.getKey()));
        }
    }

    private Position getLevelPosition(String skillName) {
        switch (skillName) {
            case "Digging":
                return new Position(this.guiLeft + 144.0f, this.guiTop + 6.0f);
            case "Attack":
                return new Position(this.guiLeft + 30.0f, this.guiTop + 6.0f);
                default:
                    return new Position(this.guiLeft, this.guiTop);
        }
    }

    private void drawSkillLevel(String skillName, Integer level) {
        Position position = getLevelPosition(skillName);
        Color color = new Color(255, 144, 76, 255);

        this.mc.fontRenderer.drawString(
                String.valueOf(level),
                position.getX(),
                position.getY(),
                color.getIntValue(),
                false);
    }

    private void loadSkillCapability(StatsResponseMessage message, MessageContext context) {
        if (message == null || context == null) {
            return;
        }

        skillCapability = message.getSkillCapability();
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        if (skillsButton != null && button != skillsButton) {
            super.actionPerformed(button);
            return;
        }

        skillsTabActive = !skillsTabActive;
    }
}
