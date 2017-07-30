package io.github.pseudoresonance.pseudospawners.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import io.github.wolfleader116.wolfapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class SpawnerSettings {

	private static String version = getVersion();

	public static ArrayList<String> getSettings(Block b) {
		ArrayList<String> list = new ArrayList<String>();
		if (b.getType() == Material.MOB_SPAWNER) {
			BlockState bs = b.getState();
			short maxNearbyEntities = 6;
			short requiredPlayerRange = 16;
			short spawnCount = 4;
			String spawnData = "";
			short maxSpawnDelay = 800;
			short spawnRange = 4;
			short minSpawnDelay = 200;
			String spawnPotentials = "";
			try {
				Class<?> ccs = Class.forName("org.bukkit.craftbukkit." + version + ".block.CraftCreatureSpawner");
				Object data = ccs.cast(bs);
				Method mTE = ccs.getMethod("getTileEntity", null);
				Class<?> tems = Class.forName("net.minecraft.server." + version + ".TileEntityMobSpawner");
				Class<?> msa = Class.forName("net.minecraft.server." + version + ".MobSpawnerAbstract");
				Class<?> nbttc = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
				Object tile = mTE.invoke(data);
				Method mGS = tems.getMethod("getSpawner", null);
				Object spawner = mGS.invoke(tile);
				Method mB = msa.getMethod("b", nbttc);
				Object nbt = mB.invoke(spawner, nbttc.newInstance());
				Method get = nbttc.getMethod("get", String.class);
				Method getShort = nbttc.getMethod("getShort", String.class);
				Object mne = getShort.invoke(nbt, "MaxNearbyEntities");
				if (mne != null) {
					maxNearbyEntities = (short) mne;
				}
				Object rpr = getShort.invoke(nbt, "RequiredPlayerRange");
				if (rpr != null) {
					requiredPlayerRange = (short) rpr;
				}
				Object sc = getShort.invoke(nbt, "SpawnCount");
				if (sc != null) {
					spawnCount = (short) sc;
				}
				Object sd = get.invoke(nbt, "SpawnData");
				if (sd != null) {
					spawnData = sd.toString();
				}
				Object msd = getShort.invoke(nbt, "MaxSpawnDelay");
				if (msd != null) {
					maxSpawnDelay = (short) msd;
				}
				Object sr = getShort.invoke(nbt, "SpawnRange");
				if (sr != null) {
					spawnRange = (short) sr;
				}
				Object misd = getShort.invoke(nbt, "MinSpawnDelay");
				if (misd != null) {
					minSpawnDelay = (short) misd;
				}
				Object sp = get.invoke(nbt, "SpawnPotentials");
				if (sp != null) {
					spawnPotentials = sp.toString();
				}
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
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
			String eSpawnData = spawnData.replaceFirst("\\{id:\"(.*)\"\\}", "");
			if (!eSpawnData.equals("")) {
				list.add(ChatColor.GRAY + "SpawnData: " + spawnData);
			}
			if (maxSpawnDelay != 800) {
				list.add(ChatColor.GRAY + "MaxSpawnDelay: " + maxSpawnDelay);
			}
			if (spawnRange != 4) {
				list.add(ChatColor.GRAY + "SpawnRange: " + spawnRange);
			}
			if (minSpawnDelay != 200) {
				list.add(ChatColor.GRAY + "MinSpawnDelay: " + minSpawnDelay);
			}
			String eSpawnPotentials = spawnPotentials.replaceFirst("\\[\\{Entity:\\{id:\"(.*)\"\\},Weight:1\\}\\]", "");
			if (!eSpawnPotentials.equals("")) {
				list.add(ChatColor.GRAY + "SpawnPotentials: " + spawnPotentials);
			}
		}
		return list;
	}

	public static void setSettings(Block b, HashMap<String, String> metadata) {
		if (b.getType() == Material.MOB_SPAWNER) {
			BlockState bs = b.getState();
			try {
				Class<?> ccs = Class.forName("org.bukkit.craftbukkit." + version + ".block.CraftCreatureSpawner");
				Object data = ccs.cast(bs);
				Method mTE = ccs.getMethod("getTileEntity", null);
				Class<?> tems = Class.forName("net.minecraft.server." + version + ".TileEntityMobSpawner");
				Class<?> msa = Class.forName("net.minecraft.server." + version + ".MobSpawnerAbstract");
				Class<?> nbttc = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
				Class<?> nbtb = Class.forName("net.minecraft.server." + version + ".NBTBase");
				Class<?> mp = Class.forName("net.minecraft.server." + version + ".MojangsonParser");
				Object tile = mTE.invoke(data);
				Method mGS = tems.getMethod("getSpawner", null);
				Method parse = mp.getMethod("parse", String.class);
				Object spawner = mGS.invoke(tile);
				Method mA = msa.getMethod("a", nbttc);
				Method mB = msa.getMethod("b", nbttc);
				Object nbt = mB.invoke(spawner, nbttc.newInstance());
				Method set = nbttc.getMethod("set", String.class, nbtb);
				Method setShort = nbttc.getMethod("setShort", String.class, short.class);
				for (String name : metadata.keySet()) {
					if (name.equals("MaxNearbyEntities") || name.equals("RequiredPlayerRange") || name.equals("SpawnCount") || name.equals("MaxSpawnDelay") || name.equals("SpawnRange") || name.equals("MinSpawnDelay")) {
						try {
							setShort.invoke(nbt, name, Short.valueOf(metadata.get(name)));
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: " + name + " of: " + metadata.get(name));
						}
					} else {
						try {
							Object nbtpart = parse.invoke(mp, metadata.get(name));
							set.invoke(nbt, name, nbtpart);
						} catch (Exception e) {
							Message.sendConsoleMessage("Spawner data error for: " + name + " of: " + metadata.get(name));
						}
					}
				}
				mA.invoke(spawner, nbt);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
			bs.update(true);
		}
	}

	private static String getVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		return version;
	}

}
