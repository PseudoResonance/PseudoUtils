package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class HealSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.heal"))) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					p.setFoodLevel(20);
					p.setExhaustion(0);
					p.setSaturation(20);
					p.setRemainingAir(p.getMaximumAir());
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.heal"));
					return true;
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
						if (!(p.getName().equals(((Player) sender).getName()) || sender.hasPermission("pseudoutils.heal.others"))) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_heal_others"));
							return false;
						}
					}
					p.setFoodLevel(20);
					p.setExhaustion(0);
					p.setSaturation(20);
					p.setRemainingAir(p.getMaximumAir());
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.heal_others", p.getDisplayName() + Config.textColor));
					PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.god_mode_by", sender instanceof Player ? ((Player) sender).getDisplayName() + Config.textColor : LanguageManager.getLanguage(p).getMessage("pseudoutils.console")));
					return true;
				}
			}
		} else
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_heal"));
		return false;
	}

}
