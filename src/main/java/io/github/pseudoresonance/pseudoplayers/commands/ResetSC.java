package io.github.pseudoresonance.pseudoplayers.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoplayers.PseudoPlayers;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class ResetSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoplayers.reset")) {
				try {
					File conf = new File(PseudoPlayers.plugin.getDataFolder(), "config.yml");
					conf.delete();
					PseudoPlayers.plugin.saveDefaultConfig();
					PseudoPlayers.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoPlayers.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoPlayers.getConfigOptions().reloadConfig();
				PseudoPlayers.message.sendPluginMessage(sender, "Plugin config reset!");
				return true;
			} else {
				PseudoPlayers.message.sendPluginError(sender, Errors.NO_PERMISSION, "reset the config!");
				return false;
			}
		} else {
			try {
				File conf = new File(PseudoPlayers.plugin.getDataFolder(), "config.yml");
				conf.delete();
				PseudoPlayers.plugin.saveDefaultConfig();
				PseudoPlayers.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoPlayers.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoPlayers.getConfigOptions().reloadConfig();
			PseudoPlayers.message.sendPluginMessage(sender, "Plugin config reset!");
			return true;
		}
	}

}
