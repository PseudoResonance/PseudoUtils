package io.github.wolfleader116.wolfmusic.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import io.github.wolfleader116.wolfmusic.bukkit.ConfigOptions;
import io.github.wolfleader116.wolfmusic.bukkit.JukeboxController;
import io.github.wolfleader116.wolfmusic.bukkit.SongFile;
import io.github.wolfleader116.wolfmusic.bukkit.WolfMusic;
import net.md_5.bungee.api.ChatColor;

public class BrowseSC implements SubCommandExecutor {
	
	Message message = new Message(WolfMusic.plugin);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("wolfmusic.browse")) {
				SongFile[] songs = WolfMusic.getSongs();
				if (songs.length >= 1) {
					Player p = (Player) sender;
					String lastSong = JukeboxController.getLastSong(p);
					String nextSong = JukeboxController.getNextSong(p);
					String currentSong = JukeboxController.getSong(p);
					Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', ConfigOptions.interfaceName));
					if (args.length == 0) {
						WolfMusic.setPage(p.getName(), 1);
						if (WolfMusic.getSongs().length > 1) {
		 					inv.setItem(ConfigOptions.lastSongInt, newStack(ConfigOptions.lastSongMaterial, 1, "§1§f" + ConfigOptions.lastSongName.replace("{name}", lastSong)));
							inv.setItem(ConfigOptions.nextSongInt, newStack(ConfigOptions.nextSongMaterial, 1, "§2§f" + ConfigOptions.nextSongName.replace("{name}", nextSong)));
						}
						inv.setItem(ConfigOptions.stopInt, newStack(ConfigOptions.stopMaterial, 1, "§3§f" + ConfigOptions.stopName.replace("{name}", currentSong)));
						if (songs.length >= 45) {
							inv.setItem(ConfigOptions.nextPageInt, newStack(ConfigOptions.nextPageMaterial, 1, "§5§f" + ConfigOptions.nextPageName.replace("{page}", "2")));
						}
						for (int i = 0; i <= 44; i++) {
							if (i < songs.length) {
								SongFile sf = songs[i];
								inv.setItem(i + 9, newStack(sf.getDisk(), 1, sf.getColor() + sf.getName()));
							}
						}
					} else {
						if (isInteger(args[0])) {
							int page = Integer.parseInt(args[0]);
							int total = (int) Math.ceil((double) songs.length / 45);
							if (page > total) {
								message.sendPluginError(sender, Errors.CUSTOM, "Only " + total + " pages available!");
								return false;
							} else if (page <= 0) {
								message.sendPluginError(sender, Errors.CUSTOM, "Please select a page.");
								return false;
							} else if (page == 1) {
								WolfMusic.setPage(p.getName(), 1);
								if (WolfMusic.getSongs().length > 1) {
				 					inv.setItem(ConfigOptions.lastSongInt, newStack(ConfigOptions.lastSongMaterial, 1, "§1§f" + ConfigOptions.lastSongName.replace("{name}", lastSong)));
									inv.setItem(ConfigOptions.nextSongInt, newStack(ConfigOptions.nextSongMaterial, 1, "§2§f" + ConfigOptions.nextSongName.replace("{name}", nextSong)));
								}
								inv.setItem(ConfigOptions.stopInt, newStack(ConfigOptions.stopMaterial, 1, "§3§f" + ConfigOptions.stopName.replace("{name}", currentSong)));
								if (songs.length >= 45) {
									inv.setItem(ConfigOptions.nextPageInt, newStack(ConfigOptions.nextPageMaterial, 1, "§5§f" + ConfigOptions.nextPageName.replace("{page}", "2")));
								}
								for (int i = 0; i <= 44; i++) {
									if (i < songs.length) {
										SongFile sf = songs[i];
										inv.setItem(i + 9, newStack(sf.getDisk(), 1, sf.getColor() + sf.getName()));
									}
								}
							} else if (page == total) {
								WolfMusic.setPage(p.getName(), total);
								if (WolfMusic.getSongs().length > 1) {
				 					inv.setItem(ConfigOptions.lastSongInt, newStack(ConfigOptions.lastSongMaterial, 1, "§1§f" + ConfigOptions.lastSongName.replace("{name}", lastSong)));
									inv.setItem(ConfigOptions.nextSongInt, newStack(ConfigOptions.nextSongMaterial, 1, "§2§f" + ConfigOptions.nextSongName.replace("{name}", nextSong)));
								}
								inv.setItem(ConfigOptions.stopInt, newStack(ConfigOptions.stopMaterial, 1, "§3§f" + ConfigOptions.stopName.replace("{name}", currentSong)));
								if (songs.length >= 45) {
									inv.setItem(ConfigOptions.lastPageInt, newStack(ConfigOptions.lastPageMaterial, 1, "§4§f" + ConfigOptions.lastPageName.replace("{page}", Integer.toString(page - 1))));
								}
								int loc = 8;
								for (int i = (page - 1) * 45; i <= ((page - 1) * 45) + 44; i++) {
									loc++;
									if (i < songs.length) {
										SongFile sf = songs[i];
										inv.setItem(loc, newStack(sf.getDisk(), 1, sf.getColor() + sf.getName()));
									}
								}
							} else {
								WolfMusic.setPage(p.getName(), page);
								if (WolfMusic.getSongs().length > 1) {
				 					inv.setItem(ConfigOptions.lastSongInt, newStack(ConfigOptions.lastSongMaterial, 1, "§1§f" + ConfigOptions.lastSongName.replace("{name}", lastSong)));
									inv.setItem(ConfigOptions.nextSongInt, newStack(ConfigOptions.nextSongMaterial, 1, "§2§f" + ConfigOptions.nextSongName.replace("{name}", nextSong)));
								}
								inv.setItem(ConfigOptions.stopInt, newStack(ConfigOptions.stopMaterial, 1, "§3§f" + ConfigOptions.stopName.replace("{name}", currentSong)));
								if (songs.length >= 45) {
									inv.setItem(ConfigOptions.lastPageInt, newStack(ConfigOptions.lastPageMaterial, 1, "§4§f" + ConfigOptions.lastPageName.replace("{page}", Integer.toString(page - 1))));
									inv.setItem(ConfigOptions.nextPageInt, newStack(ConfigOptions.nextPageMaterial, 1, "§5§f" + ConfigOptions.nextPageName.replace("{page}", Integer.toString(page + 1))));
								}
								int loc = 8;
								for (int i = (page - 1) * 45; i <= ((page - 1) * 45) + 44; i++) {
									loc++;
									if (i < songs.length) {
										SongFile sf = songs[i];
										inv.setItem(loc, newStack(sf.getDisk(), 1, sf.getColor() + sf.getName()));
									}
								}
							}
						} else {
							message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[0]);
							return false;
						}
					}
					p.openInventory(inv);
				} else {
					message.sendPluginError(sender, Errors.CUSTOM, "There are no songs on the server!");
				}
			} else {
				WolfAPI.message.sendPluginError(sender, Errors.NO_PERMISSION, "browse the music!");
				return false;
			}
		} else {
			message.sendPluginError(sender, Errors.CUSTOM, "This command is for players only!");
			return false;
		}
		return true;
	}
	
	protected static ItemStack newStack(Material material, int quantity, String name) {
		ItemStack is = new ItemStack(material, quantity);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

}
