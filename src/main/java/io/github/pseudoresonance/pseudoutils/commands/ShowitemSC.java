package io.github.pseudoresonance.pseudoutils.commands;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.Utils;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ShowitemSC implements SubCommandExecutor {
	
	private static Class<?> craftItemStack = null;
	private static Method asNMSCopyMethod = null;
	private static Class<?> nmsItemStackClass = null;
	private static Class<?> nbtTagCompound = null;
	private static Method saveItemStack = null;
	private static Method getItemRarity = null;
	private static Class<?> enumItemRarityClass = null;
	private static Field enumChatFormatField = null;
	private static Class<?> enumChatFormatClass = null;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("pseudoutils.showitem")) {
				if (args.length == 0) {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a player!");
					return false;
				} else {
					ItemStack is = p.getInventory().getItemInMainHand();
					if (is.getAmount() > 0) {
						ArrayList<Player> players = new ArrayList<Player>();
						boolean showAll = false;
						if (p.hasPermission("pseudoutils.showitem.multiple")) {
							for (String arg : args) {
								if (arg.equalsIgnoreCase("@a")) {
									if (p.hasPermission("pseudoutils.showitem.all")) {
										showAll = true;
										break;
									} else {
										PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "show items to everyone!");
										return false;
									}
								}
								Player test = Bukkit.getPlayer(arg);
								if (test != null)
									players.add(test);
								else
									PseudoUtils.message.sendPluginError(sender, Errors.NOT_ONLINE, arg);
							}
						} else {
							Player test = Bukkit.getPlayer(args[0]);
							if (test != null)
								players.add(test);
							else {
								PseudoUtils.message.sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
								return false;
							}
						}
						if (players.isEmpty() && !showAll) {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a valid player!");
							return false;
						} else {
							try {
								if (craftItemStack == null) {
									craftItemStack = Class.forName("org.bukkit.craftbukkit." + Utils.getBukkitVersion() + ".inventory.CraftItemStack");
									asNMSCopyMethod = craftItemStack.getMethod("asNMSCopy", ItemStack.class);
									nmsItemStackClass = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".ItemStack");
									nbtTagCompound = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".NBTTagCompound");
									saveItemStack = nmsItemStackClass.getMethod("save", nbtTagCompound);
									enumItemRarityClass = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".EnumItemRarity");
									enumChatFormatClass = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".EnumChatFormat");
									for (Method m : nmsItemStackClass.getMethods()) {
										if (m.getReturnType().equals(enumItemRarityClass)) {
											getItemRarity = m;
											break;
										}
									}
									for (Field f : enumItemRarityClass.getFields()) {
										if (f.getType().equals(enumChatFormatClass)) {
											enumChatFormatField = f;
											break;
										}
									}
								}
								Object nmsItemStackObj = asNMSCopyMethod.invoke(null, is);
								Object rarity = getItemRarity.invoke(nmsItemStackObj);
								Object chatFormat = enumChatFormatField.get(rarity);
								String color = chatFormat.toString();
								Object nbtTagCompoundObj = nbtTagCompound.newInstance();
								Object itemAsJson = saveItemStack.invoke(nmsItemStackObj, nbtTagCompoundObj);
								String json = itemAsJson.toString();
								ItemMeta im = is.getItemMeta();
								String name = "";
								if (im.hasDisplayName()) {
									name = im.getDisplayName();
									if (!name.startsWith("§")) {
										name = ChatColor.ITALIC + name;
									}
								} else {
									name = WordUtils.capitalizeFully(is.getType().toString().replace('_', ' '));
								}
								name = color + "[" + name + color + "]";
								TextComponent itemTC = new TextComponent(name);
								itemTC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[] {new TextComponent(json)}));
								String suffix = "";
								if (is.getAmount() > 1)
									suffix = is.getAmount() + "x ";
								TextComponent pre = new TextComponent(p.getDisplayName() + Config.textColor + " is showing " + suffix);
								TextComponent prePersonal = new TextComponent(Config.textColor + "You have showed " + suffix);
								if (showAll) {
									for (Player pl : Bukkit.getOnlinePlayers()) {
										if (!pl.equals(p)) {
											pl.spigot().sendMessage(new BaseComponent[] {pre, itemTC});
										}
									}
								} else {
									for (Player pl : players) {
										pl.spigot().sendMessage(new BaseComponent[] {pre, itemTC});
									}
								}
								p.spigot().sendMessage(new BaseComponent[] {prePersonal, itemTC});
								return true;
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException e) {
								e.printStackTrace();
								PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "An error has occurred! Please contact an administrator!");
								return false;
							}
						}
					} else {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please hold an item to show!");
						return false;
					}
				}
			} else {
				PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "show items!");
				return false;
			}
		} else {
			PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Only players can use showitem!");
			return false;
		}
	}

}
