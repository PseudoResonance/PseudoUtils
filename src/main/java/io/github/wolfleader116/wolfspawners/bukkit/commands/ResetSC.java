package io.github.wolfleader116.wolfspawners.bukkit.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfspawners.bukkit.WolfSpawners;

public class ResetSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("wolfspawners.reset")) {
				try {
					File conf = new File(WolfSpawners.plugin.getDataFolder(), "config.yml");
					conf.delete();
					WolfSpawners.plugin.saveDefaultConfig();
					WolfSpawners.plugin.reloadConfig();
				} catch (Exception e) {
					WolfSpawners.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				WolfSpawners.getConfigOptions().reloadConfig();
				WolfSpawners.message.sendPluginMessage(sender, "Plugin config reset!");
				return true;
			} else {
				WolfSpawners.message.sendPluginError(sender, Errors.NO_PERMISSION, "reset the config!");
				return false;
			}
		} else {
			try {
				File conf = new File(WolfSpawners.plugin.getDataFolder(), "config.yml");
				conf.delete();
				WolfSpawners.plugin.saveDefaultConfig();
				WolfSpawners.plugin.reloadConfig();
			} catch (Exception e) {
				WolfSpawners.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			WolfSpawners.getConfigOptions().reloadConfig();
			WolfSpawners.message.sendPluginMessage(sender, "Plugin config reset!");
			return true;
		}
	}

}
