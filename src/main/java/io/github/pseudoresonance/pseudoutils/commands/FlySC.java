package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class FlySC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.fly"))) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					boolean b = p.getAllowFlight();
					p.setAllowFlight(!b);
					if (!b) {
						PseudoUtils.message.sendPluginMessage(sender, "Enabled fly mode!");
					} else
						PseudoUtils.message.sendPluginMessage(sender, "Disabled fly mode!");
					return true;
				}
				else {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a player!");
					return false;
				}
			} else {
				p = Bukkit.getServer().getPlayer(args[0]);
				if (p == null) {
					PseudoUtils.message.sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
					return false;
				}
			}
			if (sender instanceof Player) {
				if (!(p.getName().equals(((Player) sender).getName()) || sender.hasPermission("pseudoutils.fly.others"))) {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use fly mode on other players!");
					return false;
				}
			}
			if (p != null) {
				boolean b = p.getAllowFlight();
				p.setAllowFlight(!b);
				if (!b) {
					PseudoUtils.message.sendPluginMessage(sender, "Enabled fly mode for " + p.getName() + "!");
					if (sender instanceof Player)
						PseudoUtils.message.sendPluginMessage(p, "Fly mode enabled by " + ((Player) sender).getName() + "!");
					else
						PseudoUtils.message.sendPluginMessage(p, "Fly mode enabled by Console!");
				} else {
					PseudoUtils.message.sendPluginMessage(sender, "Disabled fly mode for " + p.getName() + "!");
					if (sender instanceof Player)
						PseudoUtils.message.sendPluginMessage(p, "Fly mode disabled by " + ((Player) sender).getName() + "!");
					else
						PseudoUtils.message.sendPluginMessage(p, "Fly mode disabled by Console!");
				}
			}
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use fly mode!");
		return false;
	}

}
