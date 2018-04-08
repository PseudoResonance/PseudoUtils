package io.github.pseudoresonance.pseudoutils.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class ResetSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoutils.reset")) {
				try {
					File conf = new File(PseudoUtils.plugin.getDataFolder(), "config.yml");
					conf.delete();
					PseudoUtils.plugin.saveDefaultConfig();
					PseudoUtils.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoUtils.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoUtils.getConfigOptions().reloadConfig();
				PseudoUtils.message.sendPluginMessage(sender, "Plugin config reset!");
				return true;
			} else {
				PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "reset the config!");
				return false;
			}
		} else {
			try {
				File conf = new File(PseudoUtils.plugin.getDataFolder(), "config.yml");
				conf.delete();
				PseudoUtils.plugin.saveDefaultConfig();
				PseudoUtils.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoUtils.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoUtils.getConfigOptions().reloadConfig();
			PseudoUtils.message.sendPluginMessage(sender, "Plugin config reset!");
			return true;
		}
	}

}
