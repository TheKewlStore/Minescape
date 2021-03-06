package com.iandavis.minescape.commands;

import com.iandavis.minescape.capability.skill.CapabilitySkills;
import com.iandavis.minescape.api.events.LevelUpEvent;
import com.iandavis.minescape.network.messages.LevelSetMessage;
import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.api.capability.ISkillContainer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

import static com.iandavis.minescape.proxy.CommonProxy.logger;

public class SetLevelCommand extends CommandBase {
    @Override
    public String getName() {
        return "setLevel";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "setLevel {skill_name} {level}";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        ISkillContainer skillCapability = (sender.getCommandSenderEntity()).getCapability(
                CapabilitySkills.getSkillCapability(),
                null);
        ISkill skill = skillCapability.getSkill(args[0]);

        if (skill == null) {
            sender.getCommandSenderEntity().sendMessage(new TextComponentString(String.format("No known skill named %s", args[0])));
        } else {
            int newLevel = Integer.parseInt(args[1]);

            if (newLevel > skill.getMaxLevel()) {
                newLevel = skill.getMaxLevel();
            }

            skillCapability.getSkill(args[0]).setLevel(newLevel);
            sender.getCommandSenderEntity().sendMessage(
                    new TextComponentString(
                            String.format(
                                    "Set level in %s to %d", args[0], newLevel)));

            try {
                EntityPlayerMP player = getCommandSenderAsPlayer(sender);
                CommonProxy.networkWrapper.sendTo(new LevelSetMessage(args[0], newLevel), player);
                LevelUpEvent levelUpEvent = new LevelUpEvent(skillCapability.getSkill(args[0]), player);
                MinecraftForge.EVENT_BUS.post(levelUpEvent);
            } catch (PlayerNotFoundException e) {
                logger.error("Failed to find player attached to sender entity: " + sender.getName(), e);
            }
        }
    }
}
