package io.github.wolfleader116.wolfspawners.bukkit.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfspawners.bukkit.ConfigOptions;
import io.github.wolfleader116.wolfspawners.bukkit.GUISetPage;
import io.github.wolfleader116.wolfspawners.bukkit.WolfSpawners;

public class SpawnerSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (sender.hasPermission("wolfspawners.spawner")) {
				List<EntityType> entities = ConfigOptions.allow;
				if (entities.size() >= 1) {
					if (args.length == 0) {
						WolfSpawners.setPage(p.getName(), 1);
						GUISetPage.setPage(p, 1);
					} else {
						String build = "";
						for (int i = 0; i < args.length; i++) {
							if (i == 0) {
								build = args[i];
							} else {
								build = build + " " + args[i];
							}
						}
						EntityType entity = ConfigOptions.getEntity(build.toUpperCase());
						if (entity == null) {
							try {
								entity = EntityType.valueOf(build.toUpperCase());
							} catch (IllegalArgumentException e) {
								WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "That is an invalid entity type!");
								return true;
							}
						}
						if (p.hasPermission("wolfspawners.override")) {
							if (p.getInventory().getItemInMainHand().getType() == Material.MOB_SPAWNER) {
								ItemStack item = p.getInventory().getItemInMainHand();
								ItemMeta meta = item.getItemMeta();
								meta.setDisplayName(ConfigOptions.color + ConfigOptions.getName(entity) + " Spawner");
								item.setItemMeta(meta);
								p.getInventory().setItemInMainHand(item);
							} else if (p.getInventory().getItemInOffHand().getType() == Material.MOB_SPAWNER) {
								ItemStack item = p.getInventory().getItemInOffHand();
								ItemMeta meta = item.getItemMeta();
								meta.setDisplayName(ConfigOptions.color + ConfigOptions.getName(entity) + " Spawner");
								item.setItemMeta(meta);
								p.getInventory().setItemInOffHand(item);
							} else {
								try {
									Set<Material> set = new HashSet<Material>();
									set = null;
									Block b = p.getTargetBlock(set, 5);
									if (b.getType() == Material.MOB_SPAWNER) {
										CreatureSpawner s = (CreatureSpawner) b.getState();
										s.setSpawnedType(entity);
										s.update();
									} else {
										WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "You are not holding or looking at a spawner!");
									}
								} catch (Exception e) {
									WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "Minecraft disallows that entity type!");
								}
							}
							p.closeInventory();
							return true;
						} else {
							for (EntityType et : ConfigOptions.allow) {
								if (et == entity) {
									if (p.getInventory().getItemInMainHand().getType() == Material.MOB_SPAWNER) {
										ItemStack item = p.getInventory().getItemInMainHand();
										ItemMeta meta = item.getItemMeta();
										meta.setDisplayName(ConfigOptions.color + ConfigOptions.getName(entity) + " Spawner");
										item.setItemMeta(meta);
										p.getInventory().setItemInMainHand(item);
									} else if (p.getInventory().getItemInOffHand().getType() == Material.MOB_SPAWNER) {
										ItemStack item = p.getInventory().getItemInOffHand();
										ItemMeta meta = item.getItemMeta();
										meta.setDisplayName(ConfigOptions.color + ConfigOptions.getName(entity) + " Spawner");
										item.setItemMeta(meta);
										p.getInventory().setItemInOffHand(item);
									} else {
										try {
											Set<Material> set = new HashSet<Material>();
											set = null;
											Block b = p.getTargetBlock(set, 5);
											if (b.getType() == Material.MOB_SPAWNER) {
												CreatureSpawner s = (CreatureSpawner) b.getState();
												s.setSpawnedType(entity);
												s.update();
											} else {
												WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "You are not holding or looking at a spawner!");
											}
										} catch (Exception e) {
											WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "Minecraft disallows that entity type!");
										}
									}
									p.closeInventory();
									return true;
								}
							}
						}
						WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "That entity type is disabled!");
					}
				} else {
					WolfSpawners.message.sendPluginError(sender, Errors.CUSTOM, "There are no entities enabled on the server!");
				}
			} else {
				WolfSpawners.message.sendPluginError(p, Errors.NO_PERMISSION, "set a spawner type without an egg!");
				return true;
			}
		} else {
			WolfSpawners.message.sendPluginError(sender, Errors.CUSTOM, "This command is for players only!");
			return true;
		}
		return true;
	}

}
