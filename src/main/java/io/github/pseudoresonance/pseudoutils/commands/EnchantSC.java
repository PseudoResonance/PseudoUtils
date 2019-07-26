package io.github.pseudoresonance.pseudoutils.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.WordUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class EnchantSC implements SubCommandExecutor {
	
	Constructor<?> namespacedKeyConstructor = null;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoutils.enchant")) {
				if (args.length == 0) {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify an enchantment and level!");
					return false;
				} else if (args.length == 1) {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a level!");
					return false;
				} else {
					Player p = (Player) sender;
					ItemStack is = p.getInventory().getItemInMainHand();
					if (is.getAmount() == 0) {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "You must be holding an item to enchant!");
						return false;
					} else {
						try {
							Enchantment ench = null;
							String[] split = args[0].toLowerCase().split(":");
							if (split.length == 1) {
								ench = Enchantment.getByKey(NamespacedKey.minecraft(args[0].toLowerCase()));
								if (ench == null) {
									PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a valid enchantment!");
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
											PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a valid enchantment!");
											return false;
										}
									} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
										PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Could not get enchantment: " + args[0] + "! Please contact an administrator!");
										return false;
									}
								} else {
									PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Could not get enchantment: " + args[0] + "! Please contact an administrator!");
									return false;
								}
							} else {
								PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a valid enchantment!");
								return false;
							}
							try {
								int level = Integer.valueOf(args[1]);
								try {
									is.addEnchantment(ench, level);
									PseudoUtils.message.sendPluginMessage(sender, "Enchanted your " + WordUtils.capitalizeFully(is.getType().toString().replace('_', ' ')) + " with " + WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')) + " " + level);
									return true;
								} catch (IllegalArgumentException e) {
									if (p.hasPermission("pseudoutils.enchant.unsafe")) {
										is.addUnsafeEnchantment(ench, level);
										PseudoUtils.message.sendPluginMessage(sender, "Enchanted your " + WordUtils.capitalizeFully(is.getType().toString().replace('_', ' ')) + " with " + WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')) + " " + level);
										return true;
									} else {
										if (level > ench.getMaxLevel()) {
											PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, ench.getMaxLevel() + " is the maximum level for " + WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')));
											return false;
										} else {
											PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, WordUtils.capitalizeFully(ench.getKey().getKey().replace('_', ' ')) + " is incompatible with your " + WordUtils.capitalizeFully(is.getType().toString().replace('_', ' ')));
											return false;
										}
									}
								}
							} catch (NumberFormatException e) {
								PseudoUtils.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
								return false;
							}
						} catch (IllegalArgumentException e) {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a valid enchantment!");
							return false;
						}
					}
				}
			} else {
				PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "use enchant!");
				return false;
			}
		} else {
			PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Only players can use showitem!");
			return false;
		}
	}

}
