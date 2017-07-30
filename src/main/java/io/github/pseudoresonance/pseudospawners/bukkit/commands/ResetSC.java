package io.github.pseudoresonance.pseudospawners.bukkit.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudospawners.bukkit.PseudoSpawners;
import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;

public class ResetSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudospawners.reset")) {
				try {
					File conf = new File(PseudoSpawners.plugin.getDataFolder(), "config.yml");
					conf.delete();
					PseudoSpawners.plugin.saveDefaultConfig();
					PseudoSpawners.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoSpawners.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoSpawners.getConfigOptions().reloadConfig();
				PseudoSpawners.message.sendPluginMessage(sender, "Plugin config reset!");
				return true;
			} else {
				PseudoSpawners.message.sendPluginError(sender, Errors.NO_PERMISSION, "reset the config!");
				return false;
			}
		} else {
			try {
				File conf = new File(PseudoSpawners.plugin.getDataFolder(), "config.yml");
				conf.delete();
				PseudoSpawners.plugin.saveDefaultConfig();
				PseudoSpawners.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoSpawners.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoSpawners.getConfigOptions().reloadConfig();
			PseudoSpawners.message.sendPluginMessage(sender, "Plugin config reset!");
			return true;
		}
	}

}
