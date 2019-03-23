package com.iandavis.runecraft.commands;

import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapabilityProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class CheckXPCommand extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "checkXP";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "checkXP {skill_name}";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        ISkillCapability skillCapability = (sender.getCommandSenderEntity()).getCapability(SkillCapabilityProvider.skill, null);
        sender.getCommandSenderEntity().sendMessage(new TextComponentString(String.valueOf(skillCapability.getXP(args[0]))));
    }
}
