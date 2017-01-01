package io.github.wolfleader116.wolfspawners.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.wolfapi.bukkit.ConfigOptions;
import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfspawners.bukkit.WolfSpawners;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("wolfmusic.reload")) {
				try {
					WolfSpawners.plugin.reloadConfig();
				} catch (Exception e) {
					WolfSpawners.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				WolfSpawners.message.sendPluginMessage(sender, "Plugin config reloaded!");
				return true;
			} else {
				WolfSpawners.message.sendPluginError(sender, Errors.NO_PERMISSION, "reload the config!");
				return false;
			}
		} else {
			try {
				WolfSpawners.plugin.reloadConfig();
			} catch (Exception e) {
				WolfSpawners.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			ConfigOptions.reloadConfig();
			WolfSpawners.message.sendPluginMessage(sender, "Plugin config reloaded!");
			return true;
		}
	}

}
