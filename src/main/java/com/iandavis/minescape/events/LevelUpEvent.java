package com.iandavis.minescape.events;

import com.iandavis.minescape.api.skills.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LevelUpEvent extends Event {
    private final ISkill skill;
    private final EntityPlayer player;

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
