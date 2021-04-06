package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class PTimeSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.ptime"))) {
			Player p = null;
			if (args.length == 0) {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_argument", "add, query, reset, set, toggleDaylightCycle"));
				return false;
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("add")) {
					if (args.length == 1) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_time"));
						return false;
					} else if (args.length >= 2) {
						char type = Character.toLowerCase(args[1].charAt(args[1].length() - 1));
						long diff = 0;
						if (type == 's' || type == 't') {
							if (args[1].length() > 1) {
								try {
									diff = Long.parseLong(args[1].substring(0, args[1].length() - 1));
									if (type == 's')
										diff *= 20;
								} catch (NumberFormatException ex) {
									PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_time"));
									return false;
								}
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_time"));
								return false;
							}
						} else {
							try {
								diff = Long.parseLong(args[1]);
							} catch (NumberFormatException ex) {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_time"));
								return false;
							}
						}
						if (diff < 0) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_time"));
							return false;
						}
						diff %= 24000;
						if (args.length >= 3) {
							if (sender instanceof Player && !sender.hasPermission("pseudoutils.ptime_others")) {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_ptime_others"));
								return false;
							}
							p = Bukkit.getPlayer(args[2]);
							if (p != null) {
								if (p.isPlayerTimeRelative()) {
									p.setPlayerTime((p.getPlayerTimeOffset() % 24000) + diff, true);
								} else {
									p.setPlayerTime((p.getPlayerTime() % 24000) + diff, false);
								}
								String senderName;
								if (sender instanceof Player)
									senderName = ((Player) sender).getDisplayName();
								else
									senderName = "Console";
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_player_time_others", p.getDisplayName() + Config.textColor, p.getPlayerTime() % 24000));
								PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_player_time_by_others", senderName + Config.textColor, p.getPlayerTime() % 24000));
								return true;
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[2]);
								return false;
							}
						} else {
							if (sender instanceof Player) {
								p = (Player) sender;
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
								return false;
							}
							if (p.isPlayerTimeRelative()) {
								p.setPlayerTime((p.getPlayerTimeOffset() % 24000) + diff, true);
							} else {
								p.setPlayerTime((p.getPlayerTime() % 24000) + diff, false);
							}
							PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_player_time", p.getPlayerTime() % 24000));
							return true;
						}
					}
				} else if (args[0].equalsIgnoreCase("query")) {
					if (args.length == 1) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_argument", "day, daytime, gametime"));
						return false;
					} else if (args.length > 1) {
						if (args.length == 2) {
							if (sender instanceof Player) {
								p = (Player) sender;
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
								return false;
							}
							switch (args[1].toLowerCase()) {
							case "day":
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.query_player_time", (p.getPlayerTime() / 24000) % 2147483647));
								break;
							case "daytime":
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.query_player_time", p.getPlayerTime() % 24000));
								break;
							case "gametime":
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.query_player_time", p.getPlayerTime() % 2147483647));
								break;
							default:
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_argument", "day, daytime, gametime"));
								return false;
							}
						} else if (args.length >= 3) {
							if (sender instanceof Player && !sender.hasPermission("pseudoutils.ptime_others")) {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_ptime_others"));
								return false;
							}
							p = Bukkit.getPlayer(args[2]);
							if (p != null) {
								switch (args[1].toLowerCase()) {
								case "day":
									PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.query_player_time_others", p.getDisplayName() + Config.textColor, (p.getPlayerTime() / 24000) % 2147483647));
									break;
								case "daytime":
									PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.query_player_time_others", p.getDisplayName() + Config.textColor, p.getPlayerTime() % 24000));
									break;
								case "gametime":
									PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.query_player_time_others", p.getDisplayName() + Config.textColor, p.getPlayerTime() % 2147483647));
									break;
								default:
									PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_argument", "day, daytime, gametime"));
									return false;
								}
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[2]);
								return false;
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args.length == 1) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_time"));
						return false;
					} else if (args.length >= 2) {
						long time = 0;
						try {
							time = Long.parseLong(args[1]);
						} catch (NumberFormatException ex) {
							switch (args[1].toLowerCase()) {
							case "day":
								time = 1000;
								break;
							case "noon":
								time = 6000;
								break;
							case "night":
								time = 13000;
								break;
							case "midnight":
								time = 18000;
								break;
							default:
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_argument", "day, midnight, night, noon"));
								return false;
							}
						}
						time %= 24000;
						if (args.length >= 3) {
							if (sender instanceof Player && !sender.hasPermission("pseudoutils.ptime_others")) {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_ptime_others"));
								return false;
							}
							p = Bukkit.getPlayer(args[2]);
							if (p != null) {
								if (p.isPlayerTimeRelative()) {
									long worldTime = p.getWorld().getTime();
									if (worldTime > time)
										p.setPlayerTime((24000 - p.getWorld().getTime()) + time, true);
									else
										p.setPlayerTime(time - p.getWorld().getTime(), true);
								} else {
									p.setPlayerTime(time, false);
								}
								String senderName;
								if (sender instanceof Player)
									senderName = ((Player) sender).getDisplayName();
								else
									senderName = "Console";
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_player_time_others", p.getDisplayName() + Config.textColor, p.getPlayerTime() % 24000));
								PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.set_player_time_by_others", senderName + Config.textColor, p.getPlayerTime() % 24000));
								return true;
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[2]);
								return false;
							}
						} else {
							if (sender instanceof Player) {
								p = (Player) sender;
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
								return false;
							}
							if (p.isPlayerTimeRelative()) {
								long worldTime = p.getWorld().getTime();
								if (worldTime > time)
									p.setPlayerTime((24000 - p.getWorld().getTime()) + time, true);
								else
									p.setPlayerTime(time - p.getWorld().getTime(), true);
							} else {
								p.setPlayerTime(time, false);
							}
							PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.set_player_time", p.getPlayerTime() % 24000));
							return true;
						}
					}
				} else if (args[0].equalsIgnoreCase("reset")) {
					if (args.length >= 2) {
						if (sender instanceof Player && !sender.hasPermission("pseudoutils.ptime_others")) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_ptime_others"));
							return false;
						}
						p = Bukkit.getPlayer(args[1]);
						if (p != null) {
							p.resetPlayerTime();
							String senderName;
							if (sender instanceof Player)
								senderName = ((Player) sender).getDisplayName();
							else
								senderName = "Console";
							PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.reset_player_time_others", p.getDisplayName() + Config.textColor));
							PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.reset_player_time_by_others", senderName + Config.textColor));
							return true;
						} else {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[1]);
							return false;
						}
					} else {
						if (sender instanceof Player) {
							p = (Player) sender;
						} else {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
							return false;
						}
						p.resetPlayerTime();
						PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.reset_player_time"));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("toggleDaylightCycle")) {
					if (args.length >= 2) {
						if (sender instanceof Player && !sender.hasPermission("pseudoutils.ptime_others")) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_ptime_others"));
							return false;
						}
						p = Bukkit.getPlayer(args[1]);
						if (p != null) {
							if (p.isPlayerTimeRelative()) {
								p.setPlayerTime(p.getPlayerTime(), false);
							} else {
								long time = p.getPlayerTime() % 24000;
								long worldTime = p.getWorld().getTime();
								if (worldTime > time)
									p.setPlayerTime((24000 - p.getWorld().getTime()) + time, true);
								else
									p.setPlayerTime(time - p.getWorld().getTime(), true);
							}
							String senderName;
							if (sender instanceof Player)
								senderName = ((Player) sender).getDisplayName();
							else
								senderName = "Console";
							if (p.isPlayerTimeRelative()) {
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.relative_player_time_others", p.getDisplayName() + Config.textColor));
								PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.relative_player_time_by_others", senderName + Config.textColor));
							} else {
								PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.fixed_player_time_others", p.getDisplayName() + Config.textColor));
								PseudoUtils.plugin.getChat().sendPluginMessage(p, LanguageManager.getLanguage(p).getMessage("pseudoutils.fixed_player_time_by_others", senderName + Config.textColor));
							}
							return true;
						} else {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[1]);
							return false;
						}
					} else {
						if (sender instanceof Player) {
							p = (Player) sender;
						} else {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
							return false;
						}
						if (p.isPlayerTimeRelative()) {
							p.setPlayerTime(p.getPlayerTime(), false);
						} else {
							long time = p.getPlayerTime() % 24000;
							long worldTime = p.getWorld().getTime();
							if (worldTime > time)
								p.setPlayerTime((24000 - p.getWorld().getTime()) + time, true);
							else
								p.setPlayerTime(time - p.getWorld().getTime(), true);
						}
						if (p.isPlayerTimeRelative()) {
							PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.relative_player_time"));
						} else {
							PseudoUtils.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudoutils.fixed_player_time"));
						}
						return true;
					}
				} else {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_invalid_argument", "add, query, reset, set, toggleDaylightCycle"));
					return false;
				}
			}
		} else
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_ptime"));
		return false;
	}

}
