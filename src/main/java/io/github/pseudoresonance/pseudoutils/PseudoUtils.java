package io.github.pseudoresonance.pseudoutils;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

import io.github.pseudoresonance.pseudoapi.bukkit.CommandDescription;
import io.github.pseudoresonance.pseudoapi.bukkit.HelpSC;
import io.github.pseudoresonance.pseudoapi.bukkit.MainCommand;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoUpdater;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.Column;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;
import io.github.pseudoresonance.pseudoutils.commands.BackSC;
import io.github.pseudoresonance.pseudoutils.commands.BrandSC;
import io.github.pseudoresonance.pseudoutils.commands.EnchantSC;
import io.github.pseudoresonance.pseudoutils.commands.FlySC;
import io.github.pseudoresonance.pseudoutils.commands.GodSC;
import io.github.pseudoresonance.pseudoutils.commands.HealSC;
import io.github.pseudoresonance.pseudoutils.commands.MetricsSC;
import io.github.pseudoresonance.pseudoutils.commands.MoonPhaseSC;
import io.github.pseudoresonance.pseudoutils.commands.ReloadLocalizationSC;
import io.github.pseudoresonance.pseudoutils.commands.ReloadSC;
import io.github.pseudoresonance.pseudoutils.commands.ResetLocalizationSC;
import io.github.pseudoresonance.pseudoutils.commands.ResetSC;
import io.github.pseudoresonance.pseudoutils.commands.ShowitemSC;
import io.github.pseudoresonance.pseudoutils.commands.SpeedSC;
import io.github.pseudoresonance.pseudoutils.completers.EnchantTC;
import io.github.pseudoresonance.pseudoutils.completers.PseudoUtilsTC;
import io.github.pseudoresonance.pseudoutils.completers.SpeedTC;
import io.github.pseudoresonance.pseudoutils.listeners.BedEnterLeaveL;
import io.github.pseudoresonance.pseudoutils.listeners.ClientBrandL;
import io.github.pseudoresonance.pseudoutils.listeners.EntityDamageL;
import io.github.pseudoresonance.pseudoutils.listeners.FoodChangeL;
import io.github.pseudoresonance.pseudoutils.listeners.PlayerJoinLeaveL;
import io.github.pseudoresonance.pseudoutils.listeners.PlayerTeleportL;

public class PseudoUtils extends PseudoPlugin {

	public static PseudoUtils plugin;

	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	private static MetricsSC metricsSubCommand;
	private static BrandSC brandSubCommand;
	private static SpeedSC speedSubCommand;
	private static SpeedTC speedTabCompleter;

	private static Config config;
	
	public static boolean pseudoEnchantsLoaded = false;
	
	@SuppressWarnings("unused")
	private static Metrics metrics = null;
	
	public void onLoad() {
		PseudoUpdater.registerPlugin(this);
	}

	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		plugin = this;
		ServerPlayerDataController.addColumn(new Column("godMode", "BIT", "0"));
		ServerPlayerDataController.addColumn(new Column("flyMode", "BIT", "0"));
		ServerPlayerDataController.addColumn(new Column("backLocation", "VARCHAR(200)", "NULL"));
		TPS.startTps();
		config = new Config(this);
		config.updateConfig();
		config.firstInit();
		config.reloadConfig();
		if (Config.allowMendingInfinity) {
			InfinityMendingReplacement.replaceDefaultInfinityClass();
		}
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		metricsSubCommand = new MetricsSC();
		brandSubCommand = new BrandSC();
		speedSubCommand = new SpeedSC();
		speedTabCompleter = new SpeedTC();
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		PseudoAPI.registerConfig(config);
		pseudoEnchantsLoaded = Bukkit.getPluginManager().getPlugin("PseudoEnchants") != null ? true : false;
		initializeMetrics();
	}

	public void onDisable() {
		super.onDisable();
	}
	
	private void initializeMetrics() {
		metrics = new Metrics(this);
	}

	public static Config getConfigOptions() {
		return PseudoUtils.config;
	}

	private void initializeCommands() {
		this.getCommand("pseudoutils").setExecutor(mainCommand);
		this.getCommand("metrics").setExecutor(metricsSubCommand);
		this.getCommand("brand").setExecutor(brandSubCommand);
		this.getCommand("god").setExecutor(new GodSC());
		this.getCommand("fly").setExecutor(new FlySC());
		this.getCommand("heal").setExecutor(new HealSC());
		this.getCommand("back").setExecutor(new BackSC());
		this.getCommand("showitem").setExecutor(new ShowitemSC());
		this.getCommand("enchant").setExecutor(new EnchantSC());
		this.getCommand("moonphase").setExecutor(new MoonPhaseSC());
		this.getCommand("speed").setExecutor(speedSubCommand);
		this.getCommand("flyspeed").setExecutor(speedSubCommand);
		this.getCommand("walkspeed").setExecutor(speedSubCommand);
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reloadlocalization", new ReloadLocalizationSC());
		subCommands.put("reset", new ResetSC());
		subCommands.put("resetlocalization", new ResetLocalizationSC());
		subCommands.put("metrics", metricsSubCommand);
		subCommands.put("brand", brandSubCommand);
	}

	private void initializeTabcompleters() {
		this.getCommand("pseudoutils").setTabCompleter(new PseudoUtilsTC());
		this.getCommand("enchant").setTabCompleter(new EnchantTC());
		this.getCommand("speed").setTabCompleter(speedTabCompleter);
		this.getCommand("flyspeed").setTabCompleter(speedTabCompleter);
		this.getCommand("walkspeed").setTabCompleter(speedTabCompleter);
	}

	private void initializeListeners() {
		getServer().getPluginManager().registerEvents(new PlayerJoinLeaveL(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageL(), this);
		getServer().getPluginManager().registerEvents(new FoodChangeL(), this);
		getServer().getPluginManager().registerEvents(new PlayerTeleportL(), this);
		getServer().getPluginManager().registerEvents(new BedEnterLeaveL(), this);
		getServer().getMessenger().registerIncomingPluginChannel(PseudoAPI.plugin, "minecraft:brand", new ClientBrandL());
	}

	private void setCommandDescriptions() {
		commandDescriptions.add(new CommandDescription("pseudoutils", "pseudoutils.pseudoutils_help", ""));
		commandDescriptions.add(new CommandDescription("pseudoutils help", "pseudoutils.pseudoutils_help_help", ""));
		commandDescriptions.add(new CommandDescription("pseudoutils reload", "pseudoutils.pseudoutils_reload_help", "pseudoutils.reload"));
		commandDescriptions.add(new CommandDescription("pseudoutils reload", "pseudoutils.pseudoutils_reloadlocalization_help", "pseudoutils.reloadlocalization"));
		commandDescriptions.add(new CommandDescription("pseudoutils reset", "pseudoutils.pseudoutils_reset_help", "pseudoutils.reset"));
		commandDescriptions.add(new CommandDescription("pseudoutils reset", "pseudoutils.pseudoutils_resetlocalization_help", "pseudoutils.resetlocalization"));
		commandDescriptions.add(new CommandDescription("back", "pseudoutils.back_help", "pseudoutils.back"));
		commandDescriptions.add(new CommandDescription("brand <player>", "pseudoutils.brand_help", "pseudoutils.brand", false));
		commandDescriptions.add(new CommandDescription("enchant <enchantment> <level>", "pseudoutils.enchant_help", "pseudoutils.enchant", false));
		commandDescriptions.add(new CommandDescription("fly", "pseudoutils.fly_help", "pseudoutils.fly"));
		commandDescriptions.add(new CommandDescription("flyspeed (player) <speed>", "pseudoutils.flyspeed_help", "pseudoutils.speed", false));
		commandDescriptions.add(new CommandDescription("god", "pseudoutils.god_help", "pseudoutils.god"));
		commandDescriptions.add(new CommandDescription("heal", "pseudoutils.heal_help", "pseudoutils.heal"));
		commandDescriptions.add(new CommandDescription("metrics", "pseudoutils.metrics_help", "pseudoutils.metrics"));
		commandDescriptions.add(new CommandDescription("moonphase", "pseudoutils.moonphase_help", "pseudoutils.moonphase"));
		commandDescriptions.add(new CommandDescription("showitem <player>", "pseudoutils.showitem_help", "pseudoutils.showitem", false));
		commandDescriptions.add(new CommandDescription("speed (player) <speed>", "pseudoutils.speed_help", "pseudoutils.speed", false));
		commandDescriptions.add(new CommandDescription("walkspeed (player) <speed>", "pseudoutils.walkspeed_help", "pseudoutils.speed", false));
	}
	
	public void doSync(Runnable run) {
		Bukkit.getScheduler().runTask(this, run);
	}
	
	public void doAsync(Runnable run) {
		Bukkit.getScheduler().runTaskAsynchronously(this, run);
	}

}