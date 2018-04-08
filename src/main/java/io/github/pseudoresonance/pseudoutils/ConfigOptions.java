package io.github.pseudoresonance.pseudoutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.configuration.file.YamlConfiguration;
import io.github.pseudoresonance.pseudoapi.bukkit.ConfigOption;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class ConfigOptions implements ConfigOption {
	
	public static String timeFormat = "HH:mm:ss";
	public static String dateFormat = "yyyy-MM-dd";
	
	public static int tpsUpdateFrequency = 20;
	
	public static boolean updateConfig() {
		boolean error = false;
		InputStream configin = PseudoUtils.plugin.getClass().getResourceAsStream("/config.yml"); 
		BufferedReader configreader = new BufferedReader(new InputStreamReader(configin));
		YamlConfiguration configc = YamlConfiguration.loadConfiguration(configreader);
		int configcj = configc.getInt("Version");
		try {
			configreader.close();
			configin.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (PseudoUtils.plugin.getConfig().getInt("Version") != configcj) {
			try {
				String oldFile = "";
				File conf = new File(PseudoUtils.plugin.getDataFolder(), "config.yml");
				if (new File(PseudoUtils.plugin.getDataFolder(), "config.yml.old").exists()) {
					for (int i = 1; i > 0; i++) {
						if (!(new File(PseudoUtils.plugin.getDataFolder(), "config.yml.old" + i).exists())) {
							conf.renameTo(new File(PseudoUtils.plugin.getDataFolder(), "config.yml.old" + i));
							oldFile = "config.yml.old" + i;
							break;
						}
					}
				} else {
					conf.renameTo(new File(PseudoUtils.plugin.getDataFolder(), "config.yml.old"));
					oldFile = "config.yml.old";
				}
				PseudoUtils.plugin.saveDefaultConfig();
				PseudoUtils.plugin.reloadConfig();
				Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date! Old config file renamed to " + oldFile + ".");
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Error while updating config!");
				error = true;
			}
		}
		if (!error) {
			Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date!");
		} else {
			return false;
		}
		return true;
	}
	
	public void reloadConfig() {
		if (PseudoUtils.plugin.getConfig().getString("TimeFormat") != "") {
			timeFormat = PseudoUtils.plugin.getConfig().getString("TimeFormat");
		} else {
			timeFormat = "HH:mm:ss";
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for TimeFormat!");
		}
		if (PseudoUtils.plugin.getConfig().getString("DateFormat") != "") {
			dateFormat = PseudoUtils.plugin.getConfig().getString("DateFormat");
		} else {
			dateFormat = "yyyy-MM-dd";
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for DateFormat!");
		}
		String tpsUpdate = PseudoUtils.plugin.getConfig().getString("TPSUpdateFrequency");
		if (isInteger(tpsUpdate)) {
			tpsUpdateFrequency = Integer.valueOf(tpsUpdate);
		} else {
			tpsUpdateFrequency = 20;
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for TPSUpdateFrequency!");
		}
		TPS.stopTps();
		TPS.startTps();
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