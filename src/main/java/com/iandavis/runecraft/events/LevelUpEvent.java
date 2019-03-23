package com.iandavis.runecraft.events;

import com.iandavis.runecraft.skills.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LevelUpEvent extends Event {
    private ISkill skill;
    private EntityPlayer player;

    public LevelUpEvent(ISkill skill, EntityPlayer player) {
        this.skill = skill;
        this.player = player;
    }

    public ISkill getSkill() {
        return skill;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
