package io.github.pseudoresonance.pseudoutils;

import org.bukkit.configuration.file.FileConfiguration;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.data.PluginConfig;

public class Config extends PluginConfig {
	
	public static boolean newAtSpawn = true;
	public static boolean respawnAtSpawn = true;
	public static boolean overrideBeds = true;
	public static boolean globalSpawn = true;
	
	public static boolean enableJoinLeave = true;
	public static String joinFormat = "&e{name} joined the game";
	public static String leaveFormat = "&e{name} left the game";

	public static String timeFormat = "HH:mm:ss";
	public static String dateFormat = "yyyy-MM-dd";
	
	public static boolean sleepEnable = true;
	public static int requiredSleepPercentage = 35;

	public static int tpsUpdateFrequency = 20;
	public static int tpsHistorySize = 15;
	public static double maxFlySpeed = 5;
	public static double maxWalkSpeed = 5;
	public static boolean allowMendingInfinity = false;
	
	public void firstInit() {
		FileConfiguration fc = PseudoUtils.plugin.getConfig();
		allowMendingInfinity = PluginConfig.getBoolean(fc, "AllowMendingInfinity", allowMendingInfinity);
	}
	
	public void reloadConfig() {
		FileConfiguration fc = PseudoUtils.plugin.getConfig();

		newAtSpawn = PluginConfig.getBoolean(fc, "NewAtSpawn", newAtSpawn);
		respawnAtSpawn = PluginConfig.getBoolean(fc, "RespawnAtSpawn", respawnAtSpawn);
		overrideBeds = PluginConfig.getBoolean(fc, "OverrideBeds", overrideBeds);
		globalSpawn = PluginConfig.getBoolean(fc, "GlobalSpawn", globalSpawn);
		
		enableJoinLeave = PluginConfig.getBoolean(fc, "EnableJoinLeave", enableJoinLeave);
		joinFormat = PluginConfig.getString(fc, "JoinFormat", joinFormat);
		leaveFormat = PluginConfig.getString(fc, "LeaveFormat", leaveFormat);
		
		timeFormat = PluginConfig.getString(fc, "TimeFormat", timeFormat);
		dateFormat = PluginConfig.getString(fc, "DateFormat", dateFormat);

		sleepEnable = PluginConfig.getBoolean(fc, "SleepEnable", sleepEnable);
		requiredSleepPercentage = PluginConfig.getInt(fc, "RequiredSleepPercentage", requiredSleepPercentage);

		tpsUpdateFrequency = PluginConfig.getInt(fc, "TPSUpdateFrequency", tpsUpdateFrequency);
		tpsHistorySize = PluginConfig.getInt(fc, "TPSHistorySize", tpsHistorySize);
		maxFlySpeed = PluginConfig.getDouble(fc, "MaxFlySpeed", maxFlySpeed);
		maxWalkSpeed = PluginConfig.getDouble(fc, "MaxWalkSpeed", maxWalkSpeed);
		
		TPS.stopTps();
		TPS.startTps();
	}
	
	public Config(PseudoPlugin plugin) {
		super(plugin);
	}

}