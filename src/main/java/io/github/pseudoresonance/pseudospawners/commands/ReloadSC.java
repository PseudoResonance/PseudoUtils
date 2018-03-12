package io.github.pseudoresonance.pseudospawners.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudospawners.PseudoSpawners;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudospawners.reload")) {
				try {
					PseudoSpawners.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoSpawners.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoSpawners.getConfigOptions().reloadConfig();
				PseudoSpawners.message.sendPluginMessage(sender, "Plugin config reloaded!");
				return true;
			} else {
				PseudoSpawners.message.sendPluginError(sender, Errors.NO_PERMISSION, "reload the config!");
				return false;
			}
		} else {
			try {
				PseudoSpawners.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoSpawners.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoSpawners.getConfigOptions().reloadConfig();
			PseudoSpawners.message.sendPluginMessage(sender, "Plugin config reloaded!");
			return true;
		}
	}

}
