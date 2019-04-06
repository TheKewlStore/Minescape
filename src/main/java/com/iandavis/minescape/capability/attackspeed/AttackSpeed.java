package com.iandavis.minescape.capability.attackspeed;

import com.iandavis.minescape.MinescapeMain;
import com.iandavis.minescape.api.capability.IAttackSpeed;
import com.iandavis.minescape.api.utils.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class AttackSpeed implements IAttackSpeed {
    private float attackSpeedBonus = 0.0f;

    private final EntityLivingBase entity;

    private static final UUID MODIFIER_ID = UUID.nameUUIDFromBytes("minescape:attack_speed".getBytes(StandardCharsets.UTF_8));
    private static final String MODIFIER_NAME = "Bonus Attack Speed";

    public AttackSpeed() {
        entity = null;
    }

    public AttackSpeed(@Nullable final EntityLivingBase entity) {
        this.entity = entity;
    }

    @Override
    public float getBonusAttackSpeed() {
        return attackSpeedBonus;
    }

    @Override
    public void setBonusAttackSpeed(float bonusAttackSpeed) {
        attackSpeedBonus = bonusAttackSpeed;
        onAttackSpeedBonusChanged();
    }

    @Override
    public void addBonusAttackSpeed(float bonusAttackSpeed) {
        setBonusAttackSpeed(getBonusAttackSpeed() + bonusAttackSpeed);
    }

    @Override
    public void synchronize() {
        if (entity == null) {
            logger.warn("Cannot synchronize attack speed capability to client as there is no client!");
            return;
        }

        if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER) {
            final IAttributeInstance attackSpeedAttribute = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
            final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(attackSpeedAttribute));
            ((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
        }
    }

    protected AttributeModifier createModifier() {
        return new AttributeModifier(
                MODIFIER_ID,
                MODIFIER_NAME,
                getBonusAttackSpeed(),
                Constants.ATTRIBUTE_MODIFIER_OPERATION_ADD);
    }

    protected void onAttackSpeedBonusChanged() {
        if (entity == null) {
            return;
        }

        final IAttributeInstance attackSpeedAttribute = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        final AttributeModifier modifier = createModifier();
        final float newAmount = (float) modifier.getAmount();

        if (attackSpeedAttribute.getModifier(MODIFIER_ID) != null) {
            attackSpeedAttribute.removeModifier(MODIFIER_ID);
        }

        logger.debug(String.format("Attack Speed Added! Entity: %s - Speed: %f", entity, newAmount));
        attackSpeedAttribute.applyModifier(modifier);
    }

    @Override
    public void serializePacket(ByteBuf buf) {

    }

    @Override
    public void deserializePacket(ByteBuf buf) {

    }

    @Override
    public ResourceLocation getCapabilityID() {
        return new ResourceLocation(MinescapeMain.MODID, "capability");
    }
}
