package io.github.wolfleader116.wolfspawners.bukkit.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfspawners.bukkit.ConfigOptions;
import io.github.wolfleader116.wolfspawners.bukkit.WolfSpawners;

public class BlockPlaceEH implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		ItemStack is = e.getItemInHand();
		Player p = e.getPlayer();
		if (is.getType() == Material.MOB_SPAWNER) {
			Block b = e.getBlockPlaced();
			ItemMeta im = is.getItemMeta();
			if (im.hasDisplayName()) {
				String name = im.getDisplayName();
				name = name.replace(" Spawner", "");
				String entityName = ChatColor.stripColor(name);
				EntityType entity;
				try {
					entity = ConfigOptions.getEntity(entityName);
				} catch (IllegalArgumentException ex) {
					WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "That spawner is invalid!");
					e.setCancelled(true);
					return;
				}
				if (p.hasPermission("wolfspawners.override")) {
					try {
						if (b.getType() == Material.MOB_SPAWNER) {
							CreatureSpawner s = (CreatureSpawner) b.getState();
							s.setSpawnedType(entity);
							s.update();
							return;
						} else {
							WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "You are not looking at a spawner!");
							return;
						}
					} catch (Exception ex) {
						WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "Minecraft disallows that entity type!");
						return;
					}
				} else {
					for (EntityType et : ConfigOptions.allow) {
						if (et == entity) {
							try {
								if (b.getType() == Material.MOB_SPAWNER) {
									CreatureSpawner s = (CreatureSpawner) b.getState();
									s.setSpawnedType(entity);
									s.update();
									return;
								} else {
									WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "You are not looking at a spawner!");
									return;
								}
							} catch (Exception ex) {
								WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "Minecraft disallows that entity type!");
								return;
							}
						}
					}
				}
				WolfSpawners.message.sendPluginError(p, Errors.CUSTOM, "That spawner is disabled!");
				e.setCancelled(true);
			}
		}
	}
	
	public boolean isEgg(ItemStack is) {
		Material m = is.getType();
		if (m == Material.MONSTER_EGGS) {
			return true;
		} else {
			return false;
		}
	}

}
