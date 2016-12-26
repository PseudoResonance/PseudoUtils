package io.github.wolfleader116.wolfmusic.bukkit.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.wolfleader116.wolfmusic.bukkit.ConfigOptions;
import io.github.wolfleader116.wolfmusic.bukkit.GUISetPage;
import io.github.wolfleader116.wolfmusic.bukkit.JukeboxController;
import io.github.wolfleader116.wolfmusic.bukkit.SongFile;
import io.github.wolfleader116.wolfmusic.bukkit.WolfMusic;

public class InventoryClickEH implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory i = e.getClickedInventory();
		String t = i.getTitle();
		if (t.equalsIgnoreCase(ConfigOptions.interfaceName)) {
			Player p = (Player) e.getWhoClicked();
			ItemStack is = e.getCurrentItem();
			ItemMeta im = is.getItemMeta();
			if (im.hasDisplayName()) {
				String name = im.getDisplayName();
				int page = WolfMusic.getPage(p.getName());
				if (name.startsWith("§1§f")) {
					JukeboxController.lastSong(p);
					p.closeInventory();
					GUISetPage.setPage(p, page);
					e.setCancelled(true);
				} else if (name.startsWith("§2§f")) {
					JukeboxController.nextSong(p);
					p.closeInventory();
					GUISetPage.setPage(p, page);
					e.setCancelled(true);
				} else if (name.startsWith("§3§f")) {
					JukeboxController.stopSong(p);
					p.closeInventory();
					GUISetPage.setPage(p, page);
					e.setCancelled(true);
				} else if (name.startsWith("§4§f")) {
					int o = page - 1;
					p.closeInventory();
					GUISetPage.setPage(p, o);
					e.setCancelled(true);
				} else if (name.startsWith("§5§f")) {
					int o = page + 1;
					p.closeInventory();
					GUISetPage.setPage(p, o);
					e.setCancelled(true);
				} else if (isRecord(is)) {
					String songName = ChatColor.stripColor(name);
					for (SongFile sf : WolfMusic.getSongs()) {
						if (sf.getName().equalsIgnoreCase(songName)) {
							JukeboxController.setSong(p, sf);
							p.closeInventory();
							GUISetPage.setPage(p, page);
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	private static boolean isRecord(ItemStack is) {
		String m = is.getType().name();
		switch(m) {
		case "GOLD_RECORD":
			return true;
		case "GREEN_RECORD":
			return true;
		case "RECORD_3":
			return true;
		case "RECORD_4":
			return true;
		case "RECORD_5":
			return true;
		case "RECORD_6":
			return true;
		case "RECORD_7":
			return true;
		case "RECORD_8":
			return true;
		case "RECORD_9":
			return true;
		case "RECORD_10":
			return true;
		case "RECORD_11":
			return true;
		case "RECORD_12":
			return true;
		default:
			return false;
		}
	}

}
