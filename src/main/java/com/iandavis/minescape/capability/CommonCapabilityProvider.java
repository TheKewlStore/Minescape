package com.iandavis.minescape.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommonCapabilityProvider<HANDLER> implements ICapabilitySerializable<NBTBase> {
    protected final Capability<HANDLER> capability;
    protected final EnumFacing facing;
    protected final HANDLER instance;

    public CommonCapabilityProvider(Capability<HANDLER> capability, EnumFacing facing, HANDLER instance) {
        this.capability = capability;
        this.facing = facing;
        this.instance = instance;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == getCapability();
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == getCapability()) {
            return getCapability().cast(getInstance());
        }

        return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return getCapability().writeNBT(getInstance(), getFacing());
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        getCapability().readNBT(getInstance(), getFacing(), nbt);
    }

    public final Capability<HANDLER> getCapability() {
        return capability;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public HANDLER getInstance() {
        return instance;
    }
}
