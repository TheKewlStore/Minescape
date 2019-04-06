package com.iandavis.minescape.capability.skill;

import com.iandavis.minescape.api.capability.ISkillContainer;
import com.iandavis.minescape.api.skills.ISkill;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Map;

public class SkillStorage implements Capability.IStorage<ISkillContainer> {
    public SkillStorage() {
    }

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISkillContainer> capability, ISkillContainer instance, EnumFacing side) {
        NBTTagCompound skillData = new NBTTagCompound();

        for (Map.Entry<String, ISkill> entry : instance.getAllSkills().entrySet()) {
            skillData.setTag(entry.getKey(), entry.getValue().serializeNBT());
        }

        return skillData;
    }

    @Override
    public void readNBT(Capability<ISkillContainer> capability, ISkillContainer instance, EnumFacing side, NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound)) {
            return;
        }

        NBTTagCompound skillData = (NBTTagCompound) nbt;

        for (String key: skillData.getKeySet()) {
            instance.getSkill(key).deserializeNBT(skillData.getCompoundTag(key));
        }
    }
}
