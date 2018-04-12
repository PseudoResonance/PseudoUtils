package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class GodSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.god"))) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					Object o = PlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "godMode");
					if (o instanceof Boolean) {
						boolean b = (Boolean) o;
						PlayerDataController.setPlayerSetting(p.getUniqueId().toString(), "godMode", !b);
						if (!b) {
							p.setFoodLevel(20);
							p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
							PseudoUtils.message.sendPluginMessage(sender, "Enabled god mode!");
						} else
							PseudoUtils.message.sendPluginMessage(sender, "Disabled god mode!");
						return true;
					}
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
				if (!(p.getName().equals(((Player) sender).getName()) || sender.hasPermission("pseudoutils.god.others"))) {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use god mode on other players!");
					return false;
				}
			}
			if (p != null) {
				Object o = PlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "godMode");
				if (o instanceof Boolean) {
					boolean b = (Boolean) o;
					PlayerDataController.setPlayerSetting(p.getUniqueId().toString(), "godMode", !b);
					if (!b) {
						p.setFoodLevel(20);
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
						PseudoUtils.message.sendPluginMessage(sender, "Enabled god mode for " + p.getName() + "!");
					} else
						PseudoUtils.message.sendPluginMessage(sender, "Disabled god mode for " + p.getName() + "!");
				}
			}
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use god mode!");
		return false;
	}

}
