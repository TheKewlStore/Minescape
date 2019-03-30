package com.iandavis.minescape.gui;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.proxy.ClientProxy;
import com.iandavis.minescape.skills.ISkill;
import com.iandavis.minescape.skills.ISkillCapability;
import com.iandavis.minescape.skills.SkillIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class SkillsTabScreen extends GuiScreen {
    private final ResourceLocation SKILLS_TEXTURE = new ResourceLocation(MinescapeMain.MODID, "textures/gui/skills.png");
    private List<String> skillNameIndices = new ArrayList<>();
    private GuiButton backButton;
    private ISkill activeSkillDisplay = null;
    private GuiScreen previousScreen;

    protected int guiLeft;
    protected int guiTop;

    protected final int xSize = 176;
    protected final int ySize = 166;

    public enum ButtonIDs {
        SkillsButton,
        BackButton
    }

    public SkillsTabScreen(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.backButton = new GuiButtonImage(
                ButtonIDs.SkillsButton.ordinal(),
                this.guiLeft + 155,
                this.guiTop + 3,
                14, // width
                12, // height
                1, // tex x
                168, // tex y
                0, // hover tex y
                new ResourceLocation(MinescapeMain.MODID, "textures/gui/skills.png"));

        buttonList.add(backButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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

        Color white = new Color(255, 255, 255, 255);

        ISkillCapability skillCapability = ClientProxy.getSkillCapability();

        if (ClientProxy.getSkillCapability() == null) {
            ClientProxy.updateSkillCapability();
            super.drawCenteredString(this.mc.fontRenderer,
                    "Loading Player Stats",
                    this.guiLeft + 87,
                    this.guiTop + 130,
                    white.getIntValue());
            super.drawCenteredString(this.mc.fontRenderer,
                    "from server...",
                    this.guiLeft + 87,
                    this.guiTop + 141,
                    white.getIntValue());
            GlStateManager.popMatrix();
            return;
        }

        int skillIndex = 1;
        skillNameIndices = new ArrayList<>();

        Color orange = new Color(217, 146, 24, 255);

        for (String skillName: skillCapability.getAllSkillXP().keySet()) {
            skillNameIndices.add(skillName);
            ISkill thisSkill = skillCapability.getSkill(skillName);

            if (thisSkill == activeSkillDisplay) {
                drawSkill(skillIndex++, thisSkill, orange);
                // Draw the extra stuff in the bottom display window if this is the actively clicked skill icon.
                drawSkillIcon(
                        activeSkillDisplay.getSkillIcon(),
                        new Position(this.guiLeft + 10, this.guiTop + 132));
                mc.fontRenderer.drawString(
                        String.format("Current XP: %d", activeSkillDisplay.getXP()),
                        this.guiLeft + 25,
                        this.guiTop + 130,
                        white.getIntValue());
                mc.fontRenderer.drawString(
                        String.format("XP to next level: %d", activeSkillDisplay.xpToNextLevel()),
                        this.guiLeft + 25,
                        this.guiTop + 141,
                        white.getIntValue());
            } else {
                drawSkill(skillIndex++, thisSkill, white);
            }
        }

        GlStateManager.popMatrix();

        backButton.drawButton(mc, mouseX, mouseY, partialTicks);
    }

    private void drawSkill(int skillIndex, ISkill skill, Color color) {
        Position levelPosition = getLevelPosition(skillIndex);
        Position iconPosition = getIconPosition(skillIndex);
        SkillIcon skillIcon = skill.getSkillIcon();

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        drawSkillIcon(skillIcon, iconPosition);

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

    private void drawSkillIcon(SkillIcon skillIcon, Position location) {
        mc.getTextureManager().bindTexture(skillIcon.getTextureLocation());
        drawScaledCustomSizeModalRect(
                (int) location.getX(),
                (int) location.getY(),
                0.0f,
                0.0f,
                skillIcon.getTexWidth(),
                skillIcon.getTexHeight(),
                12,
                12,
                skillIcon.getTexWidth(),
                skillIcon.getTexHeight());
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        if (button != backButton) {
            super.actionPerformed(button);
            return;
        }

        this.onGuiClosed();
        Minecraft.getMinecraft().displayGuiScreen(previousScreen);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0) {
            return;
        }

        if (mouseX > this.guiLeft + 7 &&
                mouseX < this.guiLeft + 167 &&
                mouseY > this.guiTop + 17 &&
                mouseY < this.guiTop + 121) {
            int row = (int) ((mouseY - this.guiTop - 17) / 14.0f);
            int column = (int) ((mouseX - this.guiLeft - 7) / 53.0f);
            int skillIndex = (row * 3) + column;
            ISkillCapability skillCapability = ClientProxy.getSkillCapability();

            if (skillIndex > skillNameIndices.size() - 1) {
                logger.warn(String.format("No Skill definition exists for skills page index %d", skillIndex));
                return;
            } else if (skillIndex < 0) {
                logger.warn(String.format("The skill index calculation returned a negative value for mouse coordinates: %d, %d - value of %d", mouseX, mouseY, skillIndex));
                return;
            }

            String skillName = skillNameIndices.get(skillIndex);
            activeSkillDisplay = skillCapability.getSkill(skillName);
        } else {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
