package com.iandavis.runecraft.skills;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.iandavis.runecraft.proxy.CommonProxy.logger;

public class SkillStorage implements Capability.IStorage<ISkillCapability> {
    public SkillStorage() {
    }

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side) {
        NBTTagCompound skillData = new NBTTagCompound();

        for (Map.Entry<String, ISkill> entry : instance.getAllSkills().entrySet()) {
            skillData.setInteger(entry.getKey(), entry.getValue().getXP());
        }

        return skillData;
    }

    @Override
    public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound)) {
            return;
        }

        NBTTagCompound skillData = (NBTTagCompound) nbt;
        Map<String, ISkill> skills = new HashMap<>();

        for (String key : skillData.getKeySet()) {
            ISkill skill;

            try {
                Class genericClass = Class.forName(key);
                if (ISkill.class.isAssignableFrom(genericClass)) {
                    skill = (ISkill) genericClass.getDeclaredConstructor().newInstance();
                } else {
                    logger.error(String.format("Skill class descriptor %s was not found to implement the ISkill interface", key));
                    continue;
                }
            } catch (ClassNotFoundException e) {
                logger.error("Failed to find skill class matching descriptor: " + key, e);
                continue;
            }  catch (NoSuchMethodException e) {
                logger.error("No default constructor defined for skill class matching descriptor: " + key, e);
                continue;
            }  catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                logger.error("Exception occurred creating new instance of skill class matching descriptor: " + key, e);
                continue;
            }

            skill.setXP(skillData.getInteger(key));

            skills.put(key, skill);
        }

        instance.setAllSkills(skills);
    }
}
