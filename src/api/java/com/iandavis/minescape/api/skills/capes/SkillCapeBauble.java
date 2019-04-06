package com.iandavis.minescape.api.skills.capes;

import baubles.api.BaubleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true)
@Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = "baubles", striprefs = true)
public abstract class SkillCapeBauble extends Item implements baubles.api.IBauble, baubles.api.render.IRenderBauble {
    private final Set<EntityLivingBase> playersWithEquipped;

    public SkillCapeBauble() {
        playersWithEquipped = new HashSet<>();
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BODY;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        playersWithEquipped.add(player);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        playersWithEquipped.add(player);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        playersWithEquipped.remove(player);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return false;
    }

    public abstract ResourceLocation getCapeTexture();

    @Override
    @SideOnly(Side.CLIENT)
    public void onPlayerBaubleRender(ItemStack itemStack, EntityPlayer entityPlayer, RenderType renderType, float v) {
        if (renderType != RenderType.BODY) {
            return;
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(getCapeTexture());

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.125F);
        double d0 = entityPlayer.prevChasingPosX +
                (entityPlayer.chasingPosX - entityPlayer.prevChasingPosX) * (double) v -
                (entityPlayer.prevPosX + (entityPlayer.posX - entityPlayer.prevPosX)
                        * (double) v);
        double d1 = entityPlayer.prevChasingPosY +
                (entityPlayer.chasingPosY - entityPlayer.prevChasingPosY) * (double) v
                - (entityPlayer.prevPosY + (entityPlayer.posY - entityPlayer.prevPosY)
                * (double) v);
        double d2 = entityPlayer.prevChasingPosZ +
                (entityPlayer.chasingPosZ - entityPlayer.prevChasingPosZ) * (double) v -
                (entityPlayer.prevPosZ + (entityPlayer.posZ - entityPlayer.prevPosZ)
                        * (double) v);
        float f = entityPlayer.prevRenderYawOffset +
                (entityPlayer.renderYawOffset - entityPlayer.prevRenderYawOffset) * v;
        double d3 = (double) MathHelper.sin(f * 0.017453292F);
        double d4 = (double) (-MathHelper.cos(f * 0.017453292F));
        float f1 = (float) d1 * 10.0F;
        f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
        float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
        float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        float f4 = entityPlayer.prevCameraYaw + (entityPlayer.cameraYaw - entityPlayer.prevCameraYaw) * v;
        f1 = f1 + MathHelper.sin((entityPlayer.prevDistanceWalkedModified +
                (entityPlayer.distanceWalkedModified - entityPlayer.prevDistanceWalkedModified) * v) * 6.0F) * 32.0F * f4;

        if (entityPlayer.isSneaking()) {
            f1 += 25.0F;
        }

        GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        RenderPlayer renderPlayer = ReflectionHelper.getPrivateValue(
                RenderManager.class,
                Minecraft.getMinecraft().getRenderManager(),
                "playerRenderer");

        ModelRenderer capeRenderer = ReflectionHelper.getPrivateValue(ModelPlayer.class, renderPlayer.getMainModel(), "bipedCape");
        renderPlayer.getMainModel().renderCape(0.0625F);

        GlStateManager.popMatrix();
    }

    public boolean isEquipped(EntityPlayer player) {
        return playersWithEquipped.contains(player);
    }
}
