package io.github.pseudoresonance.pseudoutils.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;

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
					PseudoUtils.plugin.getChat().sendPluginError(sender, Chat.Errors.GENERIC);
					return false;
				}
				PseudoUtils.getConfigOptions().reloadConfig();
				PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoapi.config_reset"));
				return true;
			} else {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Chat.Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoapi.permission_reset_config"));
				return false;
			}
		} else {
			try {
				File conf = new File(PseudoUtils.plugin.getDataFolder(), "config.yml");
				conf.delete();
				PseudoUtils.plugin.saveDefaultConfig();
				PseudoUtils.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Chat.Errors.GENERIC);
				return false;
			}
			PseudoUtils.getConfigOptions().reloadConfig();
			PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoapi.config_reset"));
			return true;
		}
	}

}
