package com.iandavis.minescape.gui;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.api.utils.Color;
import com.iandavis.minescape.proxy.ClientProxy;
import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.api.skills.ISkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SkillBarHUD extends Gui {
    private final ResourceLocation texture = new ResourceLocation(MinescapeMain.MODID, "textures/gui/skills.png");

    public SkillBarHUD() {
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        ScaledResolution resolution = event.getResolution();
        int xPos = resolution.getScaledWidth() / 2 - 91;
        int yPos = resolution.getScaledHeight() - GuiIngameForge.left_height;
        GuiIngameForge.left_height += 16;

        ISkill activeSkill = ClientProxy.getActivelyTrainedSkill();

        if (activeSkill == null) {
            return;
        }

        if (!mc.playerController.shouldDrawHUD()) {
            return;
        }

        mc.getTextureManager().bindTexture(texture);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        drawTexturedModalRect(xPos, yPos, 177, 0, 54, 6);

        int xpStartValue = activeSkill.xpForLevel(activeSkill.getLevel());
        int xpForNextLevel = activeSkill.getXP() + activeSkill.xpToNextLevel();
        float levelUpProgress = ((float) activeSkill.getXP() - xpStartValue) / (xpForNextLevel - xpStartValue);
        int xpBarWidth = (int) (levelUpProgress * 48.0f);

        drawTexturedModalRect(xPos + 3, yPos + 2, 180, 7, xpBarWidth, 3);
        String s = String.format("XP: %d / %d", activeSkill.getXP(), xpForNextLevel);

        yPos += 10;

        Color white = new Color(255, 255, 255, 255);
        mc.fontRenderer.drawString(s, xPos, yPos, white.getIntValue());

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
