package com.iandavis.minescape.capability.attackspeed;

import com.iandavis.minescape.api.capability.IAttackSpeed;
import com.iandavis.minescape.capability.CommonCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

@Mod.EventBusSubscriber
public class CapabilityAttackSpeed {
    @CapabilityInject(IAttackSpeed.class)
    private static Capability<IAttackSpeed> attackSpeedCapability = null;
    private static final AttackSpeedStorage attackSpeedStorage = new AttackSpeedStorage();

    public static void register() {
        CapabilityManager.INSTANCE.register(IAttackSpeed.class, attackSpeedStorage, AttackSpeed::new);
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer)) {
            return;
        }

        IAttackSpeed attackSpeed = new AttackSpeed((EntityPlayer) event.getObject());

        event.addCapability(attackSpeed.getCapabilityID(),
                new CommonCapabilityProvider<>(
                        attackSpeedCapability,
                        null,
                        attackSpeed));
    }

    @SubscribeEvent
    public static void onPlayerClone(final PlayerEvent.Clone event) {
        final IAttackSpeed oldAttackSpeed = getAttackSpeed(event.getOriginal());
        final IAttackSpeed newAttackSpeed = getAttackSpeed(event.getEntityPlayer());

        if (oldAttackSpeed != null && newAttackSpeed != null) {
            newAttackSpeed.setBonusAttackSpeed(oldAttackSpeed.getBonusAttackSpeed());
        }
    }

    @SubscribeEvent
    public static void playerChangeDimension(final PlayerChangedDimensionEvent event) {
        final IAttackSpeed attackSpeed = getAttackSpeed(event.player);

        if (attackSpeed != null) {
            attackSpeed.synchronize();
        }
    }

    public static IAttackSpeed getAttackSpeed(final EntityPlayer player) {
        return player != null && player.hasCapability(attackSpeedCapability, null) ? player.getCapability(attackSpeedCapability, null) : null;
    }

    public static Capability<IAttackSpeed> getAttackSpeedCapability() {
        return attackSpeedCapability;
    }
}
