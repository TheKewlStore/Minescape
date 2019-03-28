package com.iandavis.runecraft.gui;

import com.iandavis.runecraft.RunecraftMain;
import com.iandavis.runecraft.proxy.ClientProxy;
import com.iandavis.runecraft.skills.ISkill;
import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillIcon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class MenuInterfaceOverride extends GuiInventory {
    private final ResourceLocation SKILLS_TEXTURE = new ResourceLocation(RunecraftMain.MODID, "textures/gui/skills.png");
    private GuiButton skillsButton;
    private boolean skillsTabActive = false;

    private enum ButtonIDs {
        SkillsButton
    }

    public MenuInterfaceOverride(EntityPlayer player) {
        super(player);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        super.initGui();

        skillsButton = new GuiButtonImage(
                ButtonIDs.SkillsButton.ordinal(),
                this.guiLeft + 155,
                this.guiTop + 3,
                14, // width
                12, // height
                1, // tex x
                168, // tex y
                0, // hover tex y
                new ResourceLocation(RunecraftMain.MODID, "textures/gui/skills.png"));

        this.buttonList.add(skillsButton);
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
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(SKILLS_TEXTURE);
        super.drawTexturedModalRect(
                this.guiLeft,
                this.guiTop,
                0,
                0,
                176,
                166);

        Color color = new Color(255, 255, 255, 255);

        ISkillCapability skillCapability = ClientProxy.getSkillCapability();

        if (ClientProxy.getSkillCapability() == null) {
            ClientProxy.updateSkillCapability();
            super.drawCenteredString(this.mc.fontRenderer,
                    "Loading Player Stats",
                    this.guiLeft + 87,
                    this.guiTop + 130,
                    color.getIntValue());
            super.drawCenteredString(this.mc.fontRenderer,
                    "from server...",
                    this.guiLeft + 87,
                    this.guiTop + 141,
                    color.getIntValue());
            GlStateManager.popMatrix();
            return;
        }

        int skillIndex = 1;

        for (String skillName: skillCapability.getAllSkillXP().keySet()) {
            drawSkill(skillIndex++, skillCapability.getSkill(skillName));
        }

        skillsButton.drawButton(this.mc, mouseX, mouseY, partialTicks);

        GlStateManager.popMatrix();
    }

    private void drawSkill(int skillIndex, ISkill skill) {
        Position levelPosition = getLevelPosition(skillIndex);
        Position iconPosition = getIconPosition(skillIndex);
        SkillIcon skillIcon = skill.getSkillIcon();

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        mc.getTextureManager().bindTexture(skillIcon.getTextureLocation());
        drawScaledCustomSizeModalRect(
                (int) iconPosition.getX(),
                (int) iconPosition.getY(),
                0.0f,
                0.0f,
                skillIcon.getTexWidth(),
                skillIcon.getTexHeight(),
                12,
                12,
                skillIcon.getTexWidth(),
                skillIcon.getTexHeight());

        Color color = new Color(255, 255, 255, 255);

        mc.fontRenderer.drawString(
                String.valueOf(skill.getLevel()),
                levelPosition.getX(),
                levelPosition.getY(),
                color.getIntValue(),
                true);
        mc.fontRenderer.drawString(
                "99",
                levelPosition.getX() + 20,
                levelPosition.getY(),
                color.getIntValue(),
                true);

        GlStateManager.popMatrix();
    }

    private Position getIconPosition(int skillIndex) {
        Position position = getLevelPosition(skillIndex);
        position.setX(position.getX() - 20);
        position.setY(position.getY() - 2);
        return position;
    }

    private Position getLevelPosition(int skillIndex) {
        int row = (skillIndex / 3) + 1;
        int column = skillIndex % 3;
        int left = (column - 1) * 53;
        int right = column * 53;
        int columnOffset = (int) ((right - left) / 2.0f) + left;

        float x = this.guiLeft + 2 + columnOffset;
        float y = this.guiTop + 17 + (3.0f * row);

        return new Position(x, y);
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        if (skillsButton != null && button != skillsButton) {
            super.actionPerformed(button);
            return;
        }

        skillsTabActive = !skillsTabActive;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (skillsTabActive &&
                mouseX > this.guiLeft + 7 &&
                mouseX < this.guiLeft + 167 &&
                mouseY > this.guiTop + 17 &&
                mouseY < this.guiTop + 121) {
        } else {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
