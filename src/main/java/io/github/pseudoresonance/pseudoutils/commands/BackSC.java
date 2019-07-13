package io.github.pseudoresonance.pseudoutils.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class BackSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.back"))) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					Object backLocationO = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "backLocation");
					if (backLocationO != null) {
						if (backLocationO instanceof String) {
							String s = (String) backLocationO;
							String[] split = s.split(",");
							if (split.length >= 4) {
								p.teleport(new Location(PseudoUtils.plugin.getServer().getWorld(UUID.fromString(split[0])), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5])));
								PseudoUtils.message.sendPluginMessage(sender, "Returned to previous location");
								return true;
							}
						}
					}
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No previous location!");
					return false;
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a player!");
					return false;
				}
			} else {
				p = Bukkit.getServer().getPlayer(args[0]);
				if (p == null) {
					PseudoUtils.message.sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
					return false;
				} else {
					if (sender instanceof Player) {
						if (!(p.getName().equals(((Player) sender).getName()) || sender.hasPermission("pseudoutils.back.others"))) {
							PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use back on other players!");
							return false;
						}
					}
					Object backLocationO = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "backLocation");
					if (backLocationO != null) {
						if (backLocationO instanceof String) {
							String s = (String) backLocationO;
							String[] split = s.split(",");
							if (split.length >= 6) {
								p.teleport(new Location(PseudoUtils.plugin.getServer().getWorld(UUID.fromString(split[0])), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5])));
								PseudoUtils.message.sendPluginMessage(sender, "Returned " + p.getDisplayName() + Config.textColor + " to previous location ");
								if (sender instanceof Player)
									PseudoUtils.message.sendPluginMessage(p, "Returned to previous location by " + ((Player) sender).getDisplayName());
								else
									PseudoUtils.message.sendPluginMessage(p, "Returned to previous location by Console");
								return true;
							}
						}
					}
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No previous location!");
					return false;
				}
			}
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "return to previous location!");
		return false;
	}

}
