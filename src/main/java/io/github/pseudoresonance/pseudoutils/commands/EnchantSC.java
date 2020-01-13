package io.github.pseudoresonance.pseudoutils.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.ItemUtils;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class EnchantSC implements SubCommandExecutor {

	Constructor<?> namespacedKeyConstructor = null;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player || sender.hasPermission("pseudoutils.enchant")) {
			if (args.length == 0) {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_enchantment_level"));
				return false;
			} else if (args.length == 1) {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_level"));
				return false;
			} else if (args.length == 2 && !(sender instanceof Player)) {
				PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_player"));
				return false;
			} else {
				Player p = null;
				if (sender instanceof Player)
					p = (Player) sender;
				else {
					p = Bukkit.getPlayer(args[2]);
					if (p == null) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[2]);
						return false;
					}
				}
				ItemStack is = p.getInventory().getItemInMainHand();
				if (is.getAmount() == 0) {
					PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_no_item"));
					return false;
				} else {
					try {
						Enchantment ench = null;
						String[] split = args[0].toLowerCase().split(":");
						if (split.length == 1) {
							ench = Enchantment.getByKey(NamespacedKey.minecraft(args[0].toLowerCase()));
							if (ench == null) {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_enchantment"));
								return false;
							}
						} else if (split.length == 2) {
							if (namespacedKeyConstructor == null) {
								for (Constructor<?> con : NamespacedKey.class.getDeclaredConstructors()) {
									if (con.getParameterCount() == 2) {
										Class<?>[] parameters = con.getParameterTypes();
										if (parameters[0].equals(String.class) && parameters[1].equals(String.class)) {
											con.setAccessible(true);
											namespacedKeyConstructor = con;
											break;
										}
									}
								}
							}
							if (namespacedKeyConstructor != null) {
								try {
									NamespacedKey key = (NamespacedKey) namespacedKeyConstructor.newInstance(split[0], split[1]);
									ench = Enchantment.getByKey(key);
									if (ench == null) {
										PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_enchantment"));
										return false;
									}
								} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
									PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.GENERIC);
									return false;
								}
							} else {
								PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.GENERIC);
								return false;
							}
						} else {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_enchantment"));
							return false;
						}
						try {
							int level = Integer.valueOf(args[1]);
							try {
								if (level == 0) {
									is.removeEnchantment(ench);
									if (PseudoUtils.pseudoEnchantsLoaded) {
										if (ench instanceof PseudoEnchantment) {
											is = PseudoEnchantment.stripLoreEnchantment(is, (PseudoEnchantment) ench);
										}
									}
									try {
										BaseComponent[] item = ItemUtils.getAsTextComponent(is);
										String[] splitEnch = LanguageManager.getLanguage(p).getMessage("pseudoutils.removed_enchantment", WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' '))).split("\\{\\$2\\$\\}");
										TextComponent end = new TextComponent(splitEnch.length > 1 ? Config.textColor + splitEnch[1] : "");
										p.spigot().sendMessage(new BaseComponent[] { new TextComponent(Config.textColor + splitEnch[0]), item[0], item[1], item[2], end });
										if (sender != p) {
											String[] splitEnchOther = LanguageManager.getLanguage(sender).getMessage("pseudoutils.removed_enchantment_others", WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')), p.getDisplayName() + Config.textColor).split("\\{\\$3\\$\\}");
											TextComponent endOther = new TextComponent(splitEnchOther.length > 1 ? Config.textColor + splitEnchOther[1] : "");
											sender.spigot().sendMessage(new BaseComponent[] { new TextComponent(Config.textColor + splitEnchOther[0]), item[0], item[1], item[2], endOther });
										}
									} catch (IllegalStateException e) {
										PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.GENERIC);
										return false;
									}
									return true;
								} else {
									is.addEnchantment(ench, level);
									if (PseudoUtils.pseudoEnchantsLoaded) {
										if (ench instanceof PseudoEnchantment) {
											is = PseudoEnchantment.stripLoreEnchantment(is, (PseudoEnchantment) ench);
											is = PseudoEnchantment.addLoreEnchantment(is, (PseudoEnchantment) ench, level);
										}
									}
									try {
										BaseComponent[] item = ItemUtils.getAsTextComponent(is);
										String[] splitEnch = LanguageManager.getLanguage(p).getMessage("pseudoutils.enchanted_with", WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')), level).split("\\{\\$3\\$\\}");
										TextComponent end = new TextComponent(splitEnch.length > 1 ? Config.textColor + splitEnch[1] : "");
										p.spigot().sendMessage(new BaseComponent[] { new TextComponent(Config.textColor + splitEnch[0]), item[0], item[1], item[2], end });
										if (sender != p) {
											String[] splitEnchOther = LanguageManager.getLanguage(sender).getMessage("pseudoutils.enchanted_with_others", p.getDisplayName() + Config.textColor, WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')), level).split("\\{\\$4\\$\\}");
											TextComponent endOther = new TextComponent(splitEnchOther.length > 1 ? Config.textColor + splitEnchOther[1] : "");
											sender.spigot().sendMessage(new BaseComponent[] { new TextComponent(Config.textColor + splitEnchOther[0]), item[0], item[1], item[2], endOther });
										}
									} catch (IllegalStateException e) {
										PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.GENERIC);
										return false;
									}
									return true;
								}
							} catch (IllegalArgumentException e) {
								if (p.hasPermission("pseudoutils.enchant.unsafe")) {
									is.addUnsafeEnchantment(ench, level);
									if (PseudoUtils.pseudoEnchantsLoaded) {
										if (ench instanceof PseudoEnchantment) {
											is = PseudoEnchantment.stripLoreEnchantment(is, (PseudoEnchantment) ench);
											is = PseudoEnchantment.addLoreEnchantment(is, (PseudoEnchantment) ench, level);
										}
									}
									try {
										BaseComponent[] item = ItemUtils.getAsTextComponent(is);
										String[] splitEnch = LanguageManager.getLanguage(p).getMessage("pseudoutils.enchanted_with", WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')), level).split("\\{\\$3\\$\\}");
										TextComponent end = new TextComponent(splitEnch.length > 1 ? Config.textColor + splitEnch[1] : "");
										p.spigot().sendMessage(new BaseComponent[] { new TextComponent(Config.textColor + splitEnch[0]), item[0], item[1], item[2], end });
										if (sender != p) {
											String[] splitEnchOther = LanguageManager.getLanguage(sender).getMessage("pseudoutils.enchanted_with_others", p.getDisplayName() + Config.textColor, WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')), level).split("\\{\\$4\\$\\}");
											TextComponent endOther = new TextComponent(splitEnchOther.length > 1 ? Config.textColor + splitEnchOther[1] : "");
											sender.spigot().sendMessage(new BaseComponent[] { new TextComponent(Config.textColor + splitEnchOther[0]), item[0], item[1], item[2], endOther });
										}
									} catch (IllegalStateException ex) {
										PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.GENERIC);
										return false;
									}
									return true;
								} else {
									if (level > ench.getMaxLevel()) {
										PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_max_level", ench.getMaxLevel(), WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' '))));
										return false;
									} else {
										PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_incompatible_enchantment", WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' '))));
										return false;
									}
								}
							}
						} catch (NumberFormatException e) {
							PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
							return false;
						}
					} catch (IllegalArgumentException e) {
						PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudoutils.error_specify_enchantment"));
						return false;
					}
				}
			}
		} else {
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_enchant"));
			return false;
		}
	}

}
