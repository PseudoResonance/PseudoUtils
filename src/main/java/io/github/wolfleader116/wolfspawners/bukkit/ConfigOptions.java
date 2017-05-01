package io.github.wolfleader116.wolfspawners.bukkit;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import io.github.wolfleader116.wolfapi.bukkit.ConfigOption;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class ConfigOptions implements ConfigOption {

	public static List<EntityType> disallow = new ArrayList<EntityType>();
	public static List<EntityType> allow = new ArrayList<EntityType>();
	public static String interfaceName = "&c&lWolfSpawners";
	public static Material lastPageMaterial = Material.PAPER;
	public static String lastPageName = "Page {page}";
	public static int lastPageInt = 1;
	public static Material nextPageMaterial = Material.PAPER;
	public static String nextPageName = "Page {page}";
	public static int nextPageInt = 7;
	public static ChatColor color = ChatColor.YELLOW;
	public static Map<EntityType, String> names = new HashMap<EntityType, String>();
	public static Map<String, EntityType> namesReverse = new HashMap<String, EntityType>();
	
	public static boolean updateConfig() {
		if (WolfSpawners.plugin.getConfig().getInt("Version") == 1) {
			Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date!");
		} else {
			try {
				String oldFile = "";
				File conf = new File(WolfSpawners.plugin.getDataFolder(), "config.yml");
				if (new File(WolfSpawners.plugin.getDataFolder(), "config.yml.old").exists()) {
					for (int i = 1; i > 0; i++) {
						if (!(new File(WolfSpawners.plugin.getDataFolder(), "config.yml.old" + i).exists())) {
							conf.renameTo(new File(WolfSpawners.plugin.getDataFolder(), "config.yml.old" + i));
							oldFile = "config.yml.old" + i;
							break;
						}
					}
				} else {
					conf.renameTo(new File(WolfSpawners.plugin.getDataFolder(), "config.yml.old"));
					oldFile = "config.yml.old";
				}
				WolfSpawners.plugin.saveDefaultConfig();
				WolfSpawners.plugin.reloadConfig();
				Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date! Old config file renamed to " + oldFile + ".");
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Error while updating config!");
				return false;
			}
		}
		return true;
	}
	
	public void reloadConfig() {
		String config = "";
		try {
			List<String> l = WolfSpawners.plugin.getConfig().getStringList("Disallow");
			Collections.sort(l, Collator.getInstance());
			for (String s : l) {
				String u = s.toUpperCase();
				config = u;
				if (EntityType.valueOf(u) != null && EntityType.valueOf(u) != EntityType.PLAYER) {
					disallow.add(EntityType.valueOf(u));
				}
			}
			disallow.add(EntityType.PLAYER);
		} catch (Exception e) {
			e.printStackTrace();
			Message.sendConsoleMessage(ChatColor.RED + "Invalid configuration for disallowed mob types at " + config + "!");
		}
		List<String> ent = new ArrayList<String>();
		for (EntityType et : EntityType.values()) {
			ent.add(et.toString());
		}
		Collections.sort(ent, Collator.getInstance());
		for (String st : ent) {
			if (!disallow.contains(EntityType.valueOf(st))) {
				allow.add(EntityType.valueOf(st));
			}
		}
		interfaceName = ChatColor.translateAlternateColorCodes('&', WolfSpawners.plugin.getConfig().getString("InterfaceName"));
		try {
			lastPageMaterial = Enum.valueOf(Material.class, WolfSpawners.plugin.getConfig().getString("LastPageItem").toUpperCase());
		} catch (IllegalArgumentException e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid item in config for LastPage!");
		}
		lastPageName = ChatColor.translateAlternateColorCodes('&', WolfSpawners.plugin.getConfig().getString("LastPageName"));
		int lastPageLocation = WolfSpawners.plugin.getConfig().getInt("LastPageLocation");
		if (lastPageLocation >= 1 && lastPageLocation <= 9) {
			lastPageInt = lastPageLocation - 1;
		} else {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid location in config for LastPage!");
		}
		try {
			nextPageMaterial = Enum.valueOf(Material.class, WolfSpawners.plugin.getConfig().getString("NextPageItem").toUpperCase());
		} catch (IllegalArgumentException e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid item in config for NextPage!");
		}
		nextPageName = ChatColor.translateAlternateColorCodes('&', WolfSpawners.plugin.getConfig().getString("NextPageName"));
		int nextPageLocation = WolfSpawners.plugin.getConfig().getInt("NextPageLocation");
		if (nextPageLocation >= 1 && nextPageLocation <= 9 && nextPageLocation != lastPageLocation) {
			nextPageInt = nextPageLocation - 1;
		} else {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid location in config for NextPage!");
		}
		try {
			String c = WolfSpawners.plugin.getConfig().getString("Color");
			if (c.length() == 1) {
				char ch = c.charAt(0);
				color = ChatColor.getByChar(ch);
			} else {
				color = ChatColor.valueOf(c.replace(" ", "_").toUpperCase());
			}
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid color in config for Color!");
		}
		try {
			Map<EntityType, String> nm = new HashMap<EntityType, String>();
			Map<String, EntityType> nrm = new HashMap<String, EntityType>();
			Set<String> l = WolfSpawners.plugin.getConfig().getConfigurationSection("Names").getKeys(false);
			for (String s : l) {
				String u = s.toUpperCase();
				if (EntityType.valueOf(u) != null && EntityType.valueOf(u) != EntityType.PLAYER) {
					String n = WolfSpawners.plugin.getConfig().getString("Names." + s);
					nm.put(EntityType.valueOf(u), n);
					nrm.put(n, EntityType.valueOf(u));
				}
			}
			names = nm;
			namesReverse = nrm;
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid configuration for mob type names!");
		}
	}
	
	public static String getName(EntityType et) {
		if (names.containsKey(et)) {
			return names.get(et);
		} else {
			return et.toString();
		}
	}
	
	public static EntityType getEntity(String name) throws IllegalArgumentException {
		for (String n : namesReverse.keySet()) {
			if (n.equalsIgnoreCase(name)) {
				try {
					return namesReverse.get(n);
				} catch (IllegalArgumentException e) {
					throw e;
				}
			}
		}
		try {
			return EntityType.valueOf(name);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

}