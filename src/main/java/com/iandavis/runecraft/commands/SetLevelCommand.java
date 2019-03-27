package com.iandavis.runecraft.commands;

import com.iandavis.runecraft.skills.ISkill;
import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapabilityProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
        ISkillCapability skillCapability = (sender.getCommandSenderEntity()).getCapability(SkillCapabilityProvider.skill, null);
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
        }
    }
}