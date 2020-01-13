package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoutils.reload")) {
				try {
					PseudoUtils.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Chat.Errors.GENERIC);
					return false;
				}
				PseudoUtils.getConfigOptions().reloadConfig();
				PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoapi.config_reloaded"));
				return true;
			} else {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Chat.Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoapi.permission_reload_config"));
				return false;
			}
		} else {
			try {
				PseudoUtils.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Chat.Errors.GENERIC);
				return false;
			}
			PseudoUtils.getConfigOptions().reloadConfig();
			PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoapi.config_reloaded"));
			return true;
		}
	}

}
