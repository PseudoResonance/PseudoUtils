package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class WalkSpeedSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.speed"))) {
			Player p = null;
			double speed = 1;
			if (args.length == 0) {
				if (sender instanceof Player) {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a speed!");
					return false;
				}
				else {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a speed and a player!");
					return false;
				}
			} else if (args.length == 1) {
				if (sender instanceof Player) {
					try {
						speed = Double.valueOf(args[0]);
						if (speed > 10)
							speed = 10;
						else if (speed < -10)
							speed = -10;
						p = (Player) sender;
						if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
							speed = io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
						else if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
							speed = -io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
						p.setWalkSpeed((float) (speed / 10));
						PseudoUtils.message.sendPluginMessage(sender, "Set your walk speed to " + speed + "!");
						return true;
					} catch (NumberFormatException e) {
						PseudoUtils.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[0]);
						return false;
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a player!");
					return false;
				}
			} else {
				if (sender.hasPermission("pseudoutils.speed.others")) {
					p = Bukkit.getServer().getPlayer(args[0]);
					if (p == null) {
						PseudoUtils.message.sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
						return false;
					} else {
						try {
							String senderName = "";
							speed = Double.valueOf(args[1]);
							if (speed > 10)
								speed = 10;
							else if (speed < -10)
								speed = -10;
							if (sender instanceof Player)
								senderName = ((Player) sender).getDisplayName();
							else
								senderName = "Console";
							if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
								speed = io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
							else if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
								speed = -io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
							p.setWalkSpeed((float) (speed / 10));
							PseudoUtils.message.sendPluginMessage(sender, "Set " + p.getDisplayName() + Config.textColor + "'s walk speed to " + speed + "!");
							PseudoUtils.message.sendPluginMessage(p, senderName + Config.textColor + " set your walk speed to " + speed + "!");
							return true;
						} catch (NumberFormatException e) {
							PseudoUtils.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
							return false;
						}
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "set another player's speed!");
					return false;
				}
			}
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "set your speed!");
		return false;
	}

}
