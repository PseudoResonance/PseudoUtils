package io.github.wolfleader116.wolfspawners.bukkit.events;

import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.wolfleader116.wolfspawners.bukkit.ConfigOptions;

public class BlockBreakEH implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
		Player p = e.getPlayer();
		if (e.getBlock().getType() == Material.MOB_SPAWNER) {
			Block b = e.getBlock();
			ItemMeta im = is.getItemMeta();
			boolean silk = false;
			if (im.hasEnchants()) {
				Map<Enchantment, Integer> enchs = im.getEnchants();
				for (Enchantment ench : enchs.keySet()) {
					if (ench == Enchantment.SILK_TOUCH) {
						silk = true;
					}
				}
			}
			if (p.hasPermission("wolfspawners.collect.nosilk")) {
				silk = true;
			}
			if (silk && p.getGameMode() != GameMode.CREATIVE) {
				CreatureSpawner cs = (CreatureSpawner) b.getState();
				EntityType entity = cs.getSpawnedType();
				if (p.hasPermission("wolfspawners.override")) {
					String name = ConfigOptions.getName(entity);
					String full = ConfigOptions.color + name + " Spawner";
					ItemStack spawner = newSpawner(full);
					b.getWorld().dropItem(b.getLocation(), spawner);
					e.setExpToDrop(0);
				} else {
					for (EntityType et : ConfigOptions.allow) {
						if (et == entity) {
							if (p.hasPermission("wolfspawners.spawner." + entity.toString().toLowerCase())) {
								String name = ConfigOptions.getName(entity);
								String full = ConfigOptions.color + name + " Spawner";
								ItemStack spawner = newSpawner(full);
								b.getWorld().dropItem(b.getLocation(), spawner);
								e.setExpToDrop(0);
							}
						}
					}
				}
			}
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
