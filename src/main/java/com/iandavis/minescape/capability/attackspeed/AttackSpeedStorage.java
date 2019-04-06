package com.iandavis.minescape.capability.attackspeed;

import com.iandavis.minescape.api.capability.IAttackSpeed;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AttackSpeedStorage implements Capability.IStorage<IAttackSpeed> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IAttackSpeed> capability, IAttackSpeed instance, EnumFacing side) {
        return new NBTTagFloat(instance.getBonusAttackSpeed());
    }

    @Override
    public void readNBT(Capability<IAttackSpeed> capability, IAttackSpeed instance, EnumFacing side, NBTBase nbt) {
        instance.setBonusAttackSpeed(((NBTTagFloat) nbt).getFloat());
    }
}
