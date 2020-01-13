package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class ReloadLocalizationSC implements SubCommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || sender.hasPermission("pseudoutils.reloadlocalization")) {
			try {
				LanguageManager.copyDefaultPluginLanguageFiles(PseudoUtils.plugin, false);
			} catch (Exception e) {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoapi.localization_reloaded"));
			return true;
		} else {
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoapi.permission_reload_localization"));
			return false;
		}
	}

}
