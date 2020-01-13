package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class MoonPhaseSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoutils.moonphase")) {
				Player p = (Player) sender;
				World w = p.getWorld();
				if (w.getEnvironment() == Environment.NORMAL) {
					long day = w.getFullTime()/24000;
					int phase = (int) (day % 8);
					String phaseName = LanguageManager.getLanguage(sender).getMessage("pseudoutils.moonphase_" + phase);
					PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.current_moonphase", phaseName));
					return true;
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_not_in_overworld"));
					return false;
				}
			} else {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_moonphase"));
				return false;
			}
		} else {
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_players_only_moonphase"));
			return false;
		}
	}

}
