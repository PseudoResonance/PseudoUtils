package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoutils.PlayerBrand;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import net.md_5.bungee.api.ChatColor;

public class BrandSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.brand"))) {
			if (args.length > 0) {
				Player p = Bukkit.getServer().getPlayer(args[0]);
				if (p != null) {
					PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.player_brand", p.getDisplayName() + Config.textColor, ChatColor.RED + PlayerBrand.getBrand(p.getName())));
					return true;
				} else
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
			} else
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
		} else
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_brand"));
		return false;
	}

}
