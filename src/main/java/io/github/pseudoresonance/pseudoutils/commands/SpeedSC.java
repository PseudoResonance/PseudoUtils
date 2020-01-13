package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class SpeedSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean forceFly = false;
		if (label.equalsIgnoreCase("flyspeed"))
			forceFly = true;
		boolean forceWalk = false;
		if (label.equalsIgnoreCase("walkspeed"))
			forceWalk = true;
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.speed"))) {
			Player p = null;
			double speed = 1;
			if (args.length == 0) {
				if (sender instanceof Player) {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_speed"));
					return false;
				}
				else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_speed_player"));
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
						if (forceFly || !forceWalk && p.isFlying()) {
							if (speed > io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed && !sender.hasPermission("pseudoutils.speed.override"))
								speed = io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed;
							else if (speed > io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed && !sender.hasPermission("pseudoutils.speed.override"))
								speed = -io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed;
							p.setFlySpeed((float) (speed / 10));
							PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_your_fly_speed", speed));
							return true;
						} else {
							if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
								speed = io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
							else if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
								speed = -io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
							p.setWalkSpeed((float) (speed / 10));
							PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_your_walk_speed", speed));
							return true;
						}
					} catch (NumberFormatException e) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_A_NUMBER, args[0]);
						return false;
					}
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
					return false;
				}
			} else {
				if (sender.hasPermission("pseudoutils.speed.others")) {
					p = Bukkit.getServer().getPlayer(args[0]);
					if (p == null) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
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
							if (forceFly || !forceWalk && p.isFlying()) {
								if (speed > io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed && !sender.hasPermission("pseudoutils.speed.override"))
									speed = io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed;
								else if (speed > io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed && !sender.hasPermission("pseudoutils.speed.override"))
									speed = -io.github.pseudoresonance.pseudoutils.Config.maxFlySpeed;
								p.setFlySpeed((float) (speed / 10));
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_your_fly_speed_others", p.getDisplayName() + Config.textColor, speed));
								PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(sender).getMessage("pseudoutils.player_set_your_fly_speed", senderName + Config.textColor, speed));
								return true;
							} else {
								if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
									speed = io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
								else if (speed > io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed && !sender.hasPermission("pseudoutils.speed.override"))
									speed = -io.github.pseudoresonance.pseudoutils.Config.maxWalkSpeed;
								p.setWalkSpeed((float) (speed / 10));
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_your_walk_speed_others", p.getDisplayName() + Config.textColor, speed));
								PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(sender).getMessage("pseudoutils.player_set_your_walk_speed", senderName + Config.textColor, speed));
								return true;
							}
						} catch (NumberFormatException e) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
							return false;
						}
					}
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_setspeed_others"));
					return false;
				}
			}
		} else
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_setspeed"));
		return false;
	}

}
