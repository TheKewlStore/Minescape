package com.iandavis.runecraft.skills;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SkillCapabilityProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(ISkillCapability.class)
    public static final Capability<ISkillCapability> skill = null;

    private ISkillCapability instance;

    public SkillCapabilityProvider() {
        instance = skill.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == skill;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (skill == null) {
            return null;
        }

        return skill == capability ? skill.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        if (skill == null) {
            return null;
        }

        return skill.getStorage().writeNBT(skill, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        if (skill == null) {
            return;
        }

        skill.getStorage().readNBT(skill, this.instance, null, nbt);
    }
}
