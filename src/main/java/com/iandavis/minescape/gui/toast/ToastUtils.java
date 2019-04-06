package com.iandavis.minescape.gui.toast;

import com.iandavis.minescape.api.skills.SkillIcon;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

import static net.minecraft.client.gui.toasts.IToast.TEXTURE_TOASTS;

public class ToastUtils {
    public static void drawToastBackground(GuiToast toastGui) {
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
    }

    public static void renderItemTextureOnToast(GuiToast toastGui, ItemStack item) {
        toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, 8, 8);
    }

    public static void renderTitleAndBody(GuiToast toastGui, String title, String body) {
        toastGui.getMinecraft().fontRenderer.drawString(title, 30, 7, -11534256);
        toastGui.getMinecraft().fontRenderer.drawString(body, 30, 18, -16777216);
    }

    public static void renderSkillIcon(GuiToast toastGui, SkillIcon icon) {
        toastGui.getMinecraft().getTextureManager().bindTexture(icon.getTextureLocation());
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        drawCustomSizedTexture(toastGui,
                8,
                8,
                icon.getTexWidth(),
                icon.getTexHeight(),
                16,
                16);
    }

    public static void drawCustomSizedTexture(GuiToast toastGui, int x, int y, int textureWidth, int textureHeight, int width, int height) {
        float u = 0.0f;
        float v = 0.0f;
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)textureHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)textureWidth) * f), (double)((v + (float)textureHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)textureWidth) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }
}
