package io.github.pseudoresonance.pseudospawners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class SpawnerSettings {

	private static String version = getVersion();

	public static ArrayList<String> getSettings(Block b) {
		ArrayList<String> list = new ArrayList<String>();
		if (b.getType() == Material.MOB_SPAWNER) {
			BlockState bs = b.getState();
			int maxNearbyEntities = 6;
			int requiredPlayerRange = 16;
			int spawnCount = 4;
			int minSpawnDelay = 200;
			int maxSpawnDelay = 800;
			int spawnRange = 4;
			try {
				Class<?> ccs = Class.forName("org.bukkit.craftbukkit." + version + ".block.CraftCreatureSpawner");
				Object data = ccs.cast(bs);
				Method mMaxNearbyEntities = ccs.getMethod("getMaxNearbyEntities", (Class<?>[]) null);
				Object mne = mMaxNearbyEntities.invoke(data);
				if (mne != null) {
					maxNearbyEntities = (int) mne;
				}
				Method mRequiredPlayerRange = ccs.getMethod("getRequiredPlayerRange", (Class<?>[]) null);
				Object rpr = mRequiredPlayerRange.invoke(data);
				if (rpr != null) {
					requiredPlayerRange = (int) rpr;
				}
				Method mSpawnCount = ccs.getMethod("getSpawnCount", (Class<?>[]) null);
				Object sc = mSpawnCount.invoke(data);
				if (sc != null) {
					spawnCount = (int) sc;
				}
				Method mMinSpawnDelay = ccs.getMethod("getMinSpawnDelay", (Class<?>[]) null);
				Object misd = mMinSpawnDelay.invoke(data);
				if (misd != null) {
					minSpawnDelay = (int) misd;
				}
				Method mMaxSpawnDelay = ccs.getMethod("getMaxSpawnDelay", (Class<?>[]) null);
				Object msd = mMaxSpawnDelay.invoke(data);
				if (msd != null) {
					maxSpawnDelay = (int) msd;
				}
				Method mSpawnRange = ccs.getMethod("getSpawnRange", (Class<?>[]) null);
				Object sr = mSpawnRange.invoke(data);
				if (sr != null) {
					spawnRange = (int) sr;
				}
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			if (maxNearbyEntities != 6) {
				list.add(ChatColor.GRAY + "MaxNearbyEntities: " + maxNearbyEntities);
			}
			if (requiredPlayerRange != 16) {
				list.add(ChatColor.GRAY + "RequiredPlayerRange: " + requiredPlayerRange);
			}
			if (spawnCount != 4) {
				list.add(ChatColor.GRAY + "SpawnCount: " + spawnCount);
			}
			if (minSpawnDelay != 200) {
				list.add(ChatColor.GRAY + "MinSpawnDelay: " + minSpawnDelay);
			}
			if (maxSpawnDelay != 800) {
				list.add(ChatColor.GRAY + "MaxSpawnDelay: " + maxSpawnDelay);
			}
			if (spawnRange != 4) {
				list.add(ChatColor.GRAY + "SpawnRange: " + spawnRange);
			}
		}
		return list;
	}

	public static String setSettings(Block b, HashMap<String, String> metadata) {
		if (b.getType() == Material.MOB_SPAWNER) {
			BlockState bs = b.getState();
			try {
				Class<?> ccs = Class.forName("org.bukkit.craftbukkit." + version + ".block.CraftCreatureSpawner");
				Object data = ccs.cast(bs);
				int maxNearbyEntities = 6;
				int requiredPlayerRange = 16;
				int spawnCount = 4;
				int minSpawnDelay = 200;
				int maxSpawnDelay = 800;
				int spawnRange = 4;
				for (String name : metadata.keySet()) {
					if (name.equals("MaxNearbyEntities")) {
						maxNearbyEntities = Integer.valueOf(metadata.get(name));
					} else if (name.equals("RequiredPlayerRange")) {
						requiredPlayerRange = Integer.valueOf(metadata.get(name));
					} else if (name.equals("SpawnCount")) {
						spawnCount = Integer.valueOf(metadata.get(name));
					} else if (name.equals("MinSpawnDelay")) {
						minSpawnDelay = Integer.valueOf(metadata.get(name));
					} else if (name.equals("MaxSpawnDelay")) {
						maxSpawnDelay = Integer.valueOf(metadata.get(name));
					} else if (name.equals("SpawnRange")) {
						spawnRange = Integer.valueOf(metadata.get(name));
					}
				}
				if (maxNearbyEntities != 6) {
					Method mMaxNearbyEntities = ccs.getMethod("setMaxNearbyEntities", int.class);
					try {
						mMaxNearbyEntities.invoke(data, maxNearbyEntities);
					} catch (NumberFormatException e) {
						Message.sendConsoleMessage("Spawner data error for: MaxNearbyEntities of: " + maxNearbyEntities);
					} catch (InvocationTargetException e) {
						return e.getCause().getMessage();
					}
				}
				if (requiredPlayerRange != 16) {
					Method mRequiredPlayerRange = ccs.getMethod("setRequiredPlayerRange", int.class);
					try {
						mRequiredPlayerRange.invoke(data, requiredPlayerRange);
					} catch (NumberFormatException e) {
						Message.sendConsoleMessage("Spawner data error for: RequiredPlayerRange of: " + requiredPlayerRange);
					} catch (InvocationTargetException e) {
						return e.getCause().getMessage();
					}
				}
				if (spawnCount != 4) {
					Method mSpawnCount = ccs.getMethod("setSpawnCount", int.class);
					try {
						mSpawnCount.invoke(data, spawnCount);
					} catch (NumberFormatException e) {
						Message.sendConsoleMessage("Spawner data error for: SpawnCount of: " + spawnCount);
					} catch (InvocationTargetException e) {
						return e.getCause().getMessage();
					}
				}
				if (minSpawnDelay != 200) {
					Method mMinSpawnDelay = ccs.getMethod("setMinSpawnDelay", int.class);
					try {
						mMinSpawnDelay.invoke(data, minSpawnDelay);
					} catch (NumberFormatException e) {
						Message.sendConsoleMessage("Spawner data error for: MinSpawnDelay of: " + minSpawnDelay);
					} catch (InvocationTargetException e) {
						return e.getCause().getMessage();
					}
				}
				if (maxSpawnDelay != 800) {
					Method mMaxSpawnDelay = ccs.getMethod("setMaxSpawnDelay", int.class);
					try {
						mMaxSpawnDelay.invoke(data, maxSpawnDelay);
					} catch (NumberFormatException e) {
						Message.sendConsoleMessage("Spawner data error for: MaxSpawnDelay of: " + maxSpawnDelay);
					} catch (InvocationTargetException e) {
						return e.getCause().getMessage();
					}
				}
				if (spawnRange != 4) {
					Method mSpawnRange = ccs.getMethod("setSpawnRange", int.class);
					try {
						mSpawnRange.invoke(data, spawnRange);
					} catch (NumberFormatException e) {
						Message.sendConsoleMessage("Spawner data error for: SpawnRange of: " + spawnRange);
					} catch (InvocationTargetException e) {
						return e.getCause().getMessage();
					}
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
				return "Error setting spawner data! Please contact an administrator!";
			}
			bs.update(true);
			return "";
		}
		return "Error setting spawner data! Please contact an administrator!";
	}

	private static String getVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		return version;
	}

}
