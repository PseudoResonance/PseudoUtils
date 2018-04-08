package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoutils.reload")) {
				try {
					PseudoUtils.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoUtils.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoUtils.getConfigOptions().reloadConfig();
				PseudoUtils.message.sendPluginMessage(sender, "Plugin config reloaded!");
				return true;
			} else {
				PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "reload the config!");
				return false;
			}
		} else {
			try {
				PseudoUtils.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoUtils.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoUtils.getConfigOptions().reloadConfig();
			PseudoUtils.message.sendPluginMessage(sender, "Plugin config reloaded!");
			return true;
		}
	}

}
