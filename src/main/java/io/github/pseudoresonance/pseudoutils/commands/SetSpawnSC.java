package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class SetSpawnSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.setspawn"))) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a spawnpoint and world!");
					return false;
				}
			} else if (args.length == 3) {
				
			} else {
				
			}
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "set the spawnpoint!");
		return false;
	}

}
