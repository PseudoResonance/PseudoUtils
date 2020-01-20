package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class GamemodeSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || sender.hasPermission("pseudoutils.gamemode")) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					switch (label.toLowerCase()) {
					case "gms":
						p.setGameMode(GameMode.SURVIVAL);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.survival")));
						return true;
					case "gmc":
						p.setGameMode(GameMode.CREATIVE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.creative")));
						return true;
					case "gma":
						p.setGameMode(GameMode.ADVENTURE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.adventure")));
						return true;
					case "gmsp":
						p.setGameMode(GameMode.SPECTATOR);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.spectator")));
						return true;
					default:
					}
					if (p.getGameMode() == GameMode.CREATIVE) {
						p.setGameMode(GameMode.SURVIVAL);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.survival")));
						return true;
					} else {
						p.setGameMode(GameMode.CREATIVE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.creative")));
						return true;
					}
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_gamemode"));
					return false;
				}
			} else if (args.length == 1) {
				if (sender instanceof Player) {
					p = (Player) sender;
					switch (label.toLowerCase()) {
					case "gms":
						p = Bukkit.getPlayer(args[0]);
						if (p == null) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
							return false;
						}
						p.setGameMode(GameMode.SURVIVAL);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.survival")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", ((Player) sender).getDisplayName() + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.survival")));
						return true;
					case "gmc":
						p = Bukkit.getPlayer(args[0]);
						if (p == null) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
							return false;
						}
						p.setGameMode(GameMode.CREATIVE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.creative")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", ((Player) sender).getDisplayName() + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.creative")));
						return true;
					case "gma":
						p = Bukkit.getPlayer(args[0]);
						if (p == null) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
							return false;
						}
						p.setGameMode(GameMode.ADVENTURE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.adventure")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", ((Player) sender).getDisplayName() + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.adventure")));
						return true;
					case "gmsp":
						p = Bukkit.getPlayer(args[0]);
						if (p == null) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
							return false;
						}
						p.setGameMode(GameMode.SPECTATOR);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.spectator")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", ((Player) sender).getDisplayName() + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.spectator")));
						return true;
					default:
					}
					if (args[0].startsWith("sp") || args[0].equals("3")) {
						p.setGameMode(GameMode.SPECTATOR);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.spectator")));
						return true;
					} else if (args[0].startsWith("s") || args[0].equals("0")) {
						p.setGameMode(GameMode.SURVIVAL);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.survival")));
						return true;
					} else if (args[0].startsWith("c") || args[0].equals("1")) {
						p.setGameMode(GameMode.CREATIVE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.creative")));
						return true;
					} else if (args[0].startsWith("a") || args[0].equals("2")) {
						p.setGameMode(GameMode.ADVENTURE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode", LanguageManager.getLanguage(sender).getMessage("pseudoutils.adventure")));
						return true;
					} else {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_gamemode"));
						return false;
					}
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
					return false;
				}
			} else {
				if (!(sender instanceof Player) || sender.hasPermission("pseudoutils.gamemode.others")) {
					p = Bukkit.getServer().getPlayer(args[1]);
					if (p == null) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[1]);
						return false;
					}
					String sent = "";
					if (sender instanceof Player)
						sent = ((Player) sender).getDisplayName();
					else
						sent = LanguageManager.getLanguage(p).getMessage("pseudoutils.console");
					if (args[0].startsWith("sp") || args[0].startsWith("3")) {
						p.setGameMode(GameMode.SPECTATOR);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.spectator")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", sent + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.spectator")));
						return true;
					} else if (args[0].startsWith("s") || args[0].startsWith("0")) {
						p.setGameMode(GameMode.SURVIVAL);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.survival")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", sent + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.survival")));
						return true;
					} else if (args[0].startsWith("c") || args[0].startsWith("1")) {
						p.setGameMode(GameMode.CREATIVE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.creative")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", sent + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.creative")));
						return true;
					} else if (args[0].startsWith("a") || args[0].startsWith("2")) {
						p.setGameMode(GameMode.ADVENTURE);
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_gamemode_others", p.getDisplayName() + Config.textColor, LanguageManager.getLanguage(sender).getMessage("pseudoutils.adventure")));
						PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_gamemode_by", sent + Config.textColor, LanguageManager.getLanguage(p).getMessage("pseudoutils.adventure")));
						return true;
					} else {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_gamemode"));
						return false;
					}
				} else
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_gamemode"));
			}
		} else
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_gamemode"));
		return false;
	}

}
