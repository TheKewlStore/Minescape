package com.iandavis.minescape.skills.capability;

import com.iandavis.minescape.skills.ISkill;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class SkillCapability implements ISkillCapability {
    private final static Set<Class<? extends ISkill>> skillClasses = new HashSet<>();
    private final Map<String, ISkill> skills;

    public SkillCapability() {
        skills = new HashMap<>();

        for (Class<? extends ISkill> skillClass: skillClasses) {
            ISkill skill;

            try {
                skill = skillClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Failed to initialize skill instance from class descriptor: " + skillClass.getName(), e);
                continue;
            }

            skills.put(skill.getName(), skill);
        }
    }

    @Override
    public ISkill getSkill(String name) {
        return this.skills.get(name);
    }

    @Override
    public void setAllSkills(Map<String, ISkill> newSkills) {
        this.skills.putAll(newSkills);
    }

    @Override
    public void serializePacket(ByteBuf buf) {
        buf.writeInt(skills.size());

        for (ISkill skill: skills.values()) {
            buf.writeInt(skill.getName().length());
            buf.writeCharSequence(skill.getName(), Charset.defaultCharset());
            skill.serializePacket(buf);
        }
    }

    @Override
    public void deserializePacket(ByteBuf buf) {
        int numberOfSkills = buf.readInt();

        for (int i=0; i < numberOfSkills; i++) {
            int lengthOfSkillName = buf.readInt();
            String skillName = buf.readCharSequence(lengthOfSkillName, Charset.defaultCharset()).toString();
            getSkill(skillName).deserializePacket(buf);
        }
    }

    @Override
    public Map<String, ISkill> getAllSkills() {
        return skills;
    }

    @Override
    public Map<String, Integer> getAllSkillXP() {
        Map<String, Integer> skillXP = new HashMap<>();

        for (Map.Entry<String, ISkill> entry: skills.entrySet()) {
            skillXP.put(entry.getKey(), entry.getValue().getXP());
        }

        return skillXP;
    }

    /**
     * Static registration method used to register a new skill instance with the skill system.
     * @param skillClass the class of the skill instances to create.
     */
    public static void registerNewSkill(Class<? extends ISkill> skillClass) {
        skillClasses.add(skillClass);
    }
}
