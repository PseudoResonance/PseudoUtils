package io.github.pseudoresonance.pseudoutils.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class BackSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.back"))) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					Object backLocationO = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "backLocation").join();
					if (backLocationO != null) {
						if (backLocationO instanceof String) {
							String s = (String) backLocationO;
							String[] split = s.split(",");
							if (split.length >= 4) {
								p.teleport(new Location(PseudoUtils.plugin.getServer().getWorld(UUID.fromString(split[0])), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5])));
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.returned_to_previous_location"));
								return true;
							}
						}
					}
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_no_previous_location"));
					return false;
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
					return false;
				}
			} else {
				p = Bukkit.getServer().getPlayer(args[0]);
				if (p == null) {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
					return false;
				} else {
					if (sender instanceof Player) {
						if (!(p.getName().equals(((Player) sender).getName()) || sender.hasPermission("pseudoutils.back.others"))) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_back_others"));
							return false;
						}
					}
					Object backLocationO = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "backLocation").join();
					if (backLocationO != null) {
						if (backLocationO instanceof String) {
							String s = (String) backLocationO;
							String[] split = s.split(",");
							if (split.length >= 6) {
								p.teleport(new Location(PseudoUtils.plugin.getServer().getWorld(UUID.fromString(split[0])), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5])));
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(p).getMessage("pseudoutils.returned_player_to_previous_location", p.getDisplayName() + Config.textColor));
								PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.returned_to_previous_location_by", sender instanceof Player ? ((Player) sender).getDisplayName() : LanguageManager.getLanguage(p).getMessage("pseudoutils.console")));
								return true;
							}
						}
					}
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_no_previous_location"));
					return false;
				}
			}
		} else
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_back"));
		return false;
	}

}
