package com.iandavis.minescape.commands;

import com.iandavis.minescape.events.LevelUpEvent;
import com.iandavis.minescape.network.messages.LevelSetMessage;
import com.iandavis.minescape.proxy.CommonProxy;
import com.iandavis.minescape.skills.ISkill;
import com.iandavis.minescape.skills.capability.ISkillCapability;
import com.iandavis.minescape.skills.capability.SkillCapabilityProvider;
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
