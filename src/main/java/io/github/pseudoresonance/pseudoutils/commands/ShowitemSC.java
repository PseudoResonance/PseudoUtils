package io.github.pseudoresonance.pseudoutils.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.ItemUtils;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ShowitemSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("pseudoutils.showitem")) {
				if (args.length == 0) {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
					return false;
				} else {
					ItemStack is = p.getInventory().getItemInMainHand();
					if (is.getAmount() > 0) {
						ArrayList<Player> players = new ArrayList<Player>();
						boolean showAll = false;
						if (p.hasPermission("pseudoutils.showitem.multiple")) {
							for (String arg : args) {
								if (arg.length() > 0) {
									if (arg.equalsIgnoreCase("@a")) {
										if (p.hasPermission("pseudoutils.showitem.all")) {
											showAll = true;
											break;
										} else {
											PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_showitem_all"));
											return false;
										}
									}
									Player test = Bukkit.getPlayer(arg);
									if (test != null)
										players.add(test);
									else {
										PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, arg);
										if (args.length == 1)
											return false;
									}
								}
							}
						} else {
							Player test = Bukkit.getPlayer(args[0]);
							if (test != null)
								players.add(test);
							else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
								return false;
							}
						}
						if (players.isEmpty() && !showAll) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
							return false;
						} else {
							try {
								BaseComponent[] item = ItemUtils.getAsTextComponent(is);
								if (showAll) {
									for (Player pl : Bukkit.getOnlinePlayers()) {
										if (!pl.equals(p)) {
											String[] split = LanguageManager.getLanguage(pl).getMessage("pseudoutils.player_is_showing", p.getDisplayName() + Config.textColor).split("\\{\\$2\\$\\}");
											TextComponent end = new TextComponent(split.length > 1 ? Config.textColor + split[1] : "");
											if (is.getAmount() > 1) {
												String[] splitMultiple = LanguageManager.getLanguage(pl).getMessage("pseudoutils.multiple", is.getAmount()).split("\\{\\$2\\$\\}");
												TextComponent multipleEnd = new TextComponent(splitMultiple.length > 1 ? Config.textColor + splitMultiple[1] : "");
												pl.spigot().sendMessage(new BaseComponent[] {new TextComponent(Config.textColor + split[0] + splitMultiple[0]), item[0], item[1], item[2], multipleEnd, end});
											} else {
												pl.spigot().sendMessage(new BaseComponent[] {new TextComponent(Config.textColor + split[0]), item[0], item[1], item[2], end});
											}
										}
									}
								} else {
									for (Player pl : players) {
										String[] split = LanguageManager.getLanguage(pl).getMessage("pseudoutils.player_is_showing", p.getDisplayName() + Config.textColor).split("\\{\\$2\\$\\}");
										TextComponent end = new TextComponent(split.length > 1 ? Config.textColor + split[1] : "");
										if (is.getAmount() > 1) {
											String[] splitMultiple = LanguageManager.getLanguage(pl).getMessage("pseudoutils.multiple", is.getAmount()).split("\\{\\$2\\$\\}");
											TextComponent multipleEnd = new TextComponent(splitMultiple.length > 1 ? Config.textColor + splitMultiple[1] : "");
											pl.spigot().sendMessage(new BaseComponent[] {new TextComponent(Config.textColor + split[0] + splitMultiple[0]), item[0], item[1], item[2], multipleEnd, end});
										} else {
											pl.spigot().sendMessage(new BaseComponent[] {new TextComponent(Config.textColor + split[0]), item[0], item[1], item[2], end});
										}
									}
								}
								String[] split = LanguageManager.getLanguage(p).getMessage("pseudoutils.you_are_showing").split("\\{\\$1\\$\\}");
								TextComponent end = new TextComponent(split.length > 1 ? Config.textColor + split[1] : "");
								if (is.getAmount() > 1) {
									String[] splitMultiple = LanguageManager.getLanguage(p).getMessage("pseudoutils.multiple", is.getAmount()).split("\\{\\$2\\$\\}");
									TextComponent multipleEnd = new TextComponent(splitMultiple.length > 1 ? Config.textColor + splitMultiple[1] : "");
									p.spigot().sendMessage(new BaseComponent[] {new TextComponent(Config.textColor + split[0] + splitMultiple[0]), item[0], item[1], item[2], multipleEnd, end});
								} else {
									p.spigot().sendMessage(new BaseComponent[] {new TextComponent(Config.textColor + split[0]), item[0], item[1], item[2], end});
								}
								return true;
							} catch (IllegalStateException e) {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.GENERIC);
								return false;
							}
						}
					} else {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_hold_item"));
						return false;
					}
				}
			} else {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_showitem"));
				return false;
			}
		} else {
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_players_only_showitem"));
			return false;
		}
	}

}
