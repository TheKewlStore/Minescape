package com.iandavis.runecraft.skills;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;


public class SkillStorage implements Capability.IStorage<ISkillCapability> {

    private final Logger logger;

    public SkillStorage(Logger logger) {
        this.logger = logger;
    }

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side) {
        NBTTagCompound skillData = new NBTTagCompound();

        for (Map.Entry<SkillEnum, Integer> entry: instance.getAllSkills().entrySet()) {
            skillData.setInteger(entry.getKey().name(), entry.getValue());
        }

        return skillData;
    }

    @Override
    public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound)) {
            return;
        }

        NBTTagCompound skillData = (NBTTagCompound) nbt;
        Map<SkillEnum, Integer> skills = new HashMap<>();

        for (String key: skillData.getKeySet()) {
            SkillEnum skill;

            try {
                skill = SkillEnum.valueOf(key);
            } catch (IllegalArgumentException ex) {
                logger.error(String.format("Failed to map skill named %s to SkillEnum!", key));
                continue;
            }

            Integer value = skillData.getInteger(key);
            skills.put(skill, value);
        }

        instance.setAllSkills(skills);
    }
}
