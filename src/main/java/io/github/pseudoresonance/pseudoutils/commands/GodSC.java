package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class GodSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.god"))) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					Object o = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "godMode").join();
					if (o == null) {
						o = false;
					}
					if (o instanceof Boolean) {
						boolean b = (Boolean) o;
						ServerPlayerDataController.setPlayerSetting(p.getUniqueId().toString(), "godMode", !b);
						if (!b) {
							p.setFoodLevel(20);
							p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
						}
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.god_mode", LanguageManager.getLanguage(sender).getMessage("pseudoutils." + (!b ? "enabled" : "disabled"))));
						return true;
					}
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
					return false;
				}
			} else {
				p = Bukkit.getServer().getPlayer(args[0]);
				if (p == null) {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
					return false;
				}
			}
			if (sender instanceof Player) {
				if (!(p.getName().equals(((Player) sender).getName()) || sender.hasPermission("pseudoutils.god.others"))) {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_god_others"));
					return false;
				}
			}
			if (p != null) {
				Object o = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "godMode").join();
				if (o == null) {
					o = false;
				}
				if (o instanceof Boolean) {
					boolean b = (Boolean) o;
					ServerPlayerDataController.setPlayerSetting(p.getUniqueId().toString(), "godMode", !b);
					if (!b) {
						p.setFoodLevel(20);
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					}
					PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.god_mode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils." + (!b ? "enabled" : "disabled"))));
					PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.god_mode_by", LanguageManager.getLanguage(p).getMessage("pseudoutils." + (!b ? "enabled" : "disabled")), sender instanceof Player ? ((Player) sender).getDisplayName() + Config.textColor : LanguageManager.getLanguage(p).getMessage("pseudoutils.console")));
				}
			}
		} else
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_god"));
		return false;
	}

}
