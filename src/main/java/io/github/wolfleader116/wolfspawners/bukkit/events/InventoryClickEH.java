package io.github.wolfleader116.wolfspawners.bukkit.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfspawners.bukkit.ConfigOptions;
import io.github.wolfleader116.wolfspawners.bukkit.GUISetPage;
import io.github.wolfleader116.wolfspawners.bukkit.WolfSpawners;

public class InventoryClickEH implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory i = e.getClickedInventory();
		Player p = (Player) e.getWhoClicked();
		if (!(i instanceof DoubleChestInventory)) {
			String t = i.getTitle();
			if (t.equalsIgnoreCase(ConfigOptions.interfaceName)) {
				ItemStack is = e.getCurrentItem();
				ItemMeta im = is.getItemMeta();
				if (im.hasDisplayName()) {
					String name = im.getDisplayName();
					int page = WolfSpawners.getPage(p.getName());
					if (name.startsWith("§1§f")) {
						int o = page + 1;
						p.closeInventory();
						GUISetPage.setPage(p, o);
						e.setCancelled(true);
					} else if (name.startsWith("§2§f")) {
						int o = page - 1;
						p.closeInventory();
						GUISetPage.setPage(p, o);
						e.setCancelled(true);
					} else if (isEgg(is)) {
						String entityName = ChatColor.stripColor(name);
						EntityType entity = ConfigOptions.getEntity(entityName);
						for (EntityType et : ConfigOptions.allow) {
							if (et == entity) {
								if (p.hasPermission("wolfspawners.spawner")) {
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
										Set<Material> set = new HashSet<Material>();
										set = null;
										Block b = p.getTargetBlock(set, 5);
										if (b.getType() == Material.MOB_SPAWNER) {
											CreatureSpawner s = (CreatureSpawner) b.getState();
											s.setSpawnedType(entity);
											s.update();
										} else {
											if (p.getGameMode() == GameMode.CREATIVE) {
												HashMap<Integer, ItemStack> drop = p.getInventory().addItem(newSpawner(ConfigOptions.color + ConfigOptions.getName(entity) + " Spawner"));
												if (drop.containsKey(0)) {
													p.getWorld().dropItem(p.getLocation(), drop.get(0));
												}
											} else {
												WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "You are not holding or looking at a spawner!");
											}
										}
									}
									p.closeInventory();
								} else {
									WolfSpawners.message.sendPluginError(p, Errors.NO_PERMISSION, "set a spawner type without an egg!");
								}
								e.setCancelled(true);
							}
						}
					}
				}
			}
		} else if (i instanceof AnvilInventory) {
			if (e.getCurrentItem().getType() == Material.MOB_SPAWNER) {
				e.setCancelled(true);
				WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "You can't put a spawner in an anvil!");
			}
		}
	}
	
	public boolean isEgg(ItemStack is) {
		Material m = is.getType();
		if (m == Material.MONSTER_EGG) {
			return true;
		} else {
			return false;
		}
	}
	
	private static ItemStack newSpawner(String name) {
		ItemStack is = new ItemStack(Material.MOB_SPAWNER, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}

}
