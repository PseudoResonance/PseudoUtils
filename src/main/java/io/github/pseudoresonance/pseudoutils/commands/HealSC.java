package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class HealSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.heal"))) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					p.setFoodLevel(20);
					p.setExhaustion(0);
					p.setSaturation(20);
					p.setRemainingAir(p.getMaximumAir());
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					PseudoUtils.message.sendPluginMessage(sender, "Healed");
					return true;
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
						if (!(p.getName().equals(((Player) sender).getName()) || sender.hasPermission("pseudoutils.heal.others"))) {
							PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use heal on other players!");
							return false;
						}
					}
					p.setFoodLevel(20);
					p.setExhaustion(0);
					p.setSaturation(20);
					p.setRemainingAir(p.getMaximumAir());
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					PseudoUtils.message.sendPluginMessage(sender, "Healed " + p.getDisplayName());
					if (sender instanceof Player)
						PseudoUtils.message.sendPluginMessage(p, "Healed by " + ((Player) sender).getDisplayName());
					else
						PseudoUtils.message.sendPluginMessage(p, "Healed by Console");
					return true;
				}
			}
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use heal!");
		return false;
	}

}
