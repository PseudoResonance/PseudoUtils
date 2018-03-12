package io.github.pseudoresonance.pseudospawners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class SpawnerSettings {

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
			if (Integer.valueOf(PseudoSpawners.getBukkitVersion().split("_")[1]) >= 12) {
				try {
					Class<?> ccs = Class.forName("org.bukkit.craftbukkit." + PseudoSpawners.getBukkitVersion() + ".block.CraftCreatureSpawner");
					Object data = ccs.cast(bs);
					Method mMaxNearbyEntities = ccs.getMethod("getMaxNearbyEntities");
					Object mne = mMaxNearbyEntities.invoke(data);
					if (mne != null) {
						maxNearbyEntities = (int) mne;
					}
					Method mRequiredPlayerRange = ccs.getMethod("getRequiredPlayerRange");
					Object rpr = mRequiredPlayerRange.invoke(data);
					if (rpr != null) {
						requiredPlayerRange = (int) rpr;
					}
					Method mSpawnCount = ccs.getMethod("getSpawnCount");
					Object sc = mSpawnCount.invoke(data);
					if (sc != null) {
						spawnCount = (int) sc;
					}
					Method mMinSpawnDelay = ccs.getMethod("getMinSpawnDelay");
					Object misd = mMinSpawnDelay.invoke(data);
					if (misd != null) {
						minSpawnDelay = (int) misd;
					}
					Method mMaxSpawnDelay = ccs.getMethod("getMaxSpawnDelay");
					Object msd = mMaxSpawnDelay.invoke(data);
					if (msd != null) {
						maxSpawnDelay = (int) msd;
					}
					Method mSpawnRange = ccs.getMethod("getSpawnRange");
					Object sr = mSpawnRange.invoke(data);
					if (sr != null) {
						spawnRange = (int) sr;
					}
				} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Class<?> ccs = Class.forName("org.bukkit.craftbukkit." + PseudoSpawners.getBukkitVersion() + ".block.CraftCreatureSpawner");
					Object data = ccs.cast(bs);
					Method mTE = ccs.getMethod("getTileEntity");
					Class<?> tems = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".TileEntityMobSpawner");
					Class<?> msa = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".MobSpawnerAbstract");
					Class<?> nbttc = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".NBTTagCompound");
					Object tile = mTE.invoke(data);
					Method mGS = tems.getMethod("getSpawner");
					Object spawner = mGS.invoke(tile);
					Method mB = msa.getMethod("b", nbttc);
					Object nbt = mB.invoke(spawner, nbttc.newInstance());
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
					Object misd = getShort.invoke(nbt, "MinSpawnDelay");
					if (misd != null) {
						minSpawnDelay = (short) misd;
					}
					Object msd = getShort.invoke(nbt, "MaxSpawnDelay");
					if (msd != null) {
						maxSpawnDelay = (short) msd;
					}
					Object sr = getShort.invoke(nbt, "SpawnRange");
					if (sr != null) {
						spawnRange = (short) sr;
					}
				} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
					e.printStackTrace();
				}
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
				Class<?> ccs = Class.forName("org.bukkit.craftbukkit." + PseudoSpawners.getBukkitVersion() + ".block.CraftCreatureSpawner");
				Object data = ccs.cast(bs);
				short maxNearbyEntities = -1;
				short requiredPlayerRange = -1;
				short spawnCount = -1;
				short minSpawnDelay = -1;
				short maxSpawnDelay = -1;
				short spawnRange = -1;
				for (String name : metadata.keySet()) {
					if (name.equals("MaxNearbyEntities")) {
						maxNearbyEntities = Short.valueOf(metadata.get(name));
					} else if (name.equals("RequiredPlayerRange")) {
						requiredPlayerRange = Short.valueOf(metadata.get(name));
					} else if (name.equals("SpawnCount")) {
						spawnCount = Short.valueOf(metadata.get(name));
					} else if (name.equals("MinSpawnDelay")) {
						minSpawnDelay = Short.valueOf(metadata.get(name));
					} else if (name.equals("MaxSpawnDelay")) {
						maxSpawnDelay = Short.valueOf(metadata.get(name));
					} else if (name.equals("SpawnRange")) {
						spawnRange = Short.valueOf(metadata.get(name));
					}
				}
				if (Integer.valueOf(PseudoSpawners.getBukkitVersion().split("_")[1]) >= 12) {
					if (maxNearbyEntities != -1) {
						Method mMaxNearbyEntities = ccs.getMethod("setMaxNearbyEntities", int.class);
						try {
							mMaxNearbyEntities.invoke(data, maxNearbyEntities);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: MaxNearbyEntities of: " + maxNearbyEntities);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (requiredPlayerRange != -1) {
						Method mRequiredPlayerRange = ccs.getMethod("setRequiredPlayerRange", int.class);
						try {
							mRequiredPlayerRange.invoke(data, requiredPlayerRange);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: RequiredPlayerRange of: " + requiredPlayerRange);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (spawnCount != -1) {
						Method mSpawnCount = ccs.getMethod("setSpawnCount", int.class);
						try {
							mSpawnCount.invoke(data, spawnCount);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: SpawnCount of: " + spawnCount);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (minSpawnDelay != -1) {
						Method mMinSpawnDelay = ccs.getMethod("setMinSpawnDelay", int.class);
						try {
							mMinSpawnDelay.invoke(data, minSpawnDelay);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: MinSpawnDelay of: " + minSpawnDelay);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (maxSpawnDelay != -1) {
						Method mMaxSpawnDelay = ccs.getMethod("setMaxSpawnDelay", int.class);
						try {
							mMaxSpawnDelay.invoke(data, maxSpawnDelay);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: MaxSpawnDelay of: " + maxSpawnDelay);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (spawnRange != -1) {
						Method mSpawnRange = ccs.getMethod("setSpawnRange", int.class);
						try {
							mSpawnRange.invoke(data, spawnRange);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: SpawnRange of: " + spawnRange);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
				} else {
					Method mTE = ccs.getMethod("getTileEntity");
					Class<?> tems = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".TileEntityMobSpawner");
					Class<?> msa = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".MobSpawnerAbstract");
					Class<?> nbttc = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".NBTTagCompound");
					Object tile = mTE.invoke(data);
					Method mGS = tems.getMethod("getSpawner");
					Object spawner = mGS.invoke(tile);
					Method mA = msa.getMethod("a", nbttc);
					Method mB = msa.getMethod("b", nbttc);
					Object nbt = mB.invoke(spawner, nbttc.newInstance());
					Method setShort = nbttc.getMethod("setShort", String.class, short.class);
					if (maxNearbyEntities != -1) {
						try {
							setShort.invoke(nbt, "MaxNearbyEntities", maxNearbyEntities);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: MaxNearbyEntities of: " + maxNearbyEntities);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (requiredPlayerRange != -1) {
						try {
							setShort.invoke(nbt, "RequiredPlayerRange", requiredPlayerRange);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: RequiredPlayerRange of: " + requiredPlayerRange);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (spawnCount != -1) {
						try {
							setShort.invoke(nbt, "SpawnCount", spawnCount);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: SpawnCount of: " + spawnCount);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (minSpawnDelay != -1) {
						try {
							setShort.invoke(nbt, "MinSpawnDelay", minSpawnDelay);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: MinSpawnDelay of: " + minSpawnDelay);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (maxSpawnDelay != -1) {
						try {
							setShort.invoke(nbt, "MaxSpawnDelay", maxSpawnDelay);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: MaxSpawnDelay of: " + maxSpawnDelay);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					if (spawnRange != -1) {
						try {
							setShort.invoke(nbt, "SpawnRange", spawnRange);
						} catch (NumberFormatException e) {
							Message.sendConsoleMessage("Spawner data error for: SpawnRange of: " + spawnRange);
						} catch (InvocationTargetException e) {
							return e.getCause().getMessage();
						}
					}
					mA.invoke(spawner, nbt);
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
				return "Error setting spawner data! Please contact an administrator!";
			}
			bs.update(true);
			return "";
		}
		return "Error setting spawner data! Please contact an administrator!";
	}

}
