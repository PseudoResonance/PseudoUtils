package io.github.pseudoresonance.pseudoplayers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.configuration.file.YamlConfiguration;
import io.github.pseudoresonance.pseudoapi.bukkit.ConfigOption;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import net.md_5.bungee.api.ChatColor;

public class ConfigOptions implements ConfigOption {

	public static String firstJoinTimeFormat = "MM-dd-yy h:mm:ss a";
	public static int firstJoinTimeDifference = 7;
	public static String joinLeaveTimeFormat = "MM-dd-yy h:mm:ss a";
	public static int joinLeaveTimeDifference = 30;
	
	public static boolean updateConfig() {
		boolean error = false;
		InputStream configin = PseudoPlayers.plugin.getClass().getResourceAsStream("/config.yml"); 
		BufferedReader configreader = new BufferedReader(new InputStreamReader(configin));
		YamlConfiguration configc = YamlConfiguration.loadConfiguration(configreader);
		int configcj = configc.getInt("Version");
		try {
			configreader.close();
			configin.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (PseudoPlayers.plugin.getConfig().getInt("Version") != configcj) {
			try {
				String oldFile = "";
				File conf = new File(PseudoPlayers.plugin.getDataFolder(), "config.yml");
				if (new File(PseudoPlayers.plugin.getDataFolder(), "config.yml.old").exists()) {
					for (int i = 1; i > 0; i++) {
						if (!(new File(PseudoPlayers.plugin.getDataFolder(), "config.yml.old" + i).exists())) {
							conf.renameTo(new File(PseudoPlayers.plugin.getDataFolder(), "config.yml.old" + i));
							oldFile = "config.yml.old" + i;
							break;
						}
					}
				} else {
					conf.renameTo(new File(PseudoPlayers.plugin.getDataFolder(), "config.yml.old"));
					oldFile = "config.yml.old";
				}
				PseudoPlayers.plugin.saveDefaultConfig();
				PseudoPlayers.plugin.reloadConfig();
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
		firstJoinTimeFormat = "MM-dd-yy h:mm:ss a";
		firstJoinTimeDifference = 7;
		joinLeaveTimeFormat = "MM-dd-yy h:mm:ss a";
		if (PseudoAPI.plugin.getConfig().getString("FirstJoinTimeFormat") != "") {
			firstJoinTimeFormat = PseudoAPI.plugin.getConfig().getString("FirstJoinTimeFormat");
		} else {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for FirstJoinTimeFormat!");
		}
		String firstJoinTime = PseudoAPI.plugin.getConfig().getString("FirstJoinTimeDifference");
		if (isInteger(firstJoinTime)) {
			firstJoinTimeDifference = Integer.valueOf(firstJoinTime);
		} else {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for FirstJoinTimeDifference!");
		}
		String joinLeaveTime = PseudoAPI.plugin.getConfig().getString("JoinLeaveTimeDifference");
		if (isInteger(joinLeaveTime)) {
			joinLeaveTimeDifference = Integer.valueOf(joinLeaveTime);
		} else {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for JoinLeaveTimeDifference!");
		}
		if (PseudoAPI.plugin.getConfig().getString("JoinLeaveTimeFormat") != "") {
			joinLeaveTimeFormat = PseudoAPI.plugin.getConfig().getString("JoinLeaveTimeFormat");
		} else {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for JoinLeaveTimeFormat!");
		}
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