package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PlayerBrand;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import net.md_5.bungee.api.ChatColor;

public class BrandSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.brand"))) {
			if (args.length > 0) {
				Player p = Bukkit.getServer().getPlayer(args[0]);
				if (p != null) {
					PseudoUtils.message.sendPluginMessage(sender, "Player: " + p.getName() + "'s Client Brand: " + ChatColor.RED + PlayerBrand.getBrand(p.getName()));
					return true;
				} else
					PseudoUtils.message.sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
			} else
				PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a player!");
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "view user brand!");
		return false;
	}

}
