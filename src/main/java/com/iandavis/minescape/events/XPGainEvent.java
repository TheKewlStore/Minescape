package com.iandavis.minescape.events;

import com.iandavis.minescape.skills.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

public class XPGainEvent extends Event {
    private ISkill skill;
    private EntityPlayer player;
    private int xpGained;

    public XPGainEvent(ISkill skill, EntityPlayer player, int xpGained) {
        this.skill = skill;
        this.player = player;
        this.xpGained = xpGained;
    }

    @Nonnull
    public ISkill getSkill() {
        return skill;
    }

    @Nonnull
    public EntityPlayer getPlayer() {
        return player;
    }

    public int getXpGained() {
        return xpGained;
    }
}
