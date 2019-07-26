package io.github.pseudoresonance.pseudoutils;

import io.github.pseudoresonance.pseudoapi.bukkit.CommandDescription;
import io.github.pseudoresonance.pseudoapi.bukkit.HelpSC;
import io.github.pseudoresonance.pseudoapi.bukkit.MainCommand;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoUpdater;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.Column;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;
import io.github.pseudoresonance.pseudoutils.commands.BackSC;
import io.github.pseudoresonance.pseudoutils.commands.BrandSC;
import io.github.pseudoresonance.pseudoutils.commands.EnchantSC;
import io.github.pseudoresonance.pseudoutils.commands.FlySC;
import io.github.pseudoresonance.pseudoutils.commands.FlySpeedSC;
import io.github.pseudoresonance.pseudoutils.commands.GodSC;
import io.github.pseudoresonance.pseudoutils.commands.HealSC;
import io.github.pseudoresonance.pseudoutils.commands.MetricsSC;
import io.github.pseudoresonance.pseudoutils.commands.MoonPhaseSC;
import io.github.pseudoresonance.pseudoutils.commands.ReloadSC;
import io.github.pseudoresonance.pseudoutils.commands.ResetSC;
import io.github.pseudoresonance.pseudoutils.commands.ShowitemSC;
import io.github.pseudoresonance.pseudoutils.commands.SpeedSC;
import io.github.pseudoresonance.pseudoutils.commands.TpSC;
import io.github.pseudoresonance.pseudoutils.commands.WalkSpeedSC;
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

	public static PseudoPlugin plugin;
	public static Message message;

	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	private static MetricsSC metricsSubCommand;
	private static BrandSC brandSubCommand;
	private static SpeedTC speedTabCompleter;

	private static Config config;
	
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
		message = new Message(this);
		if (Config.allowMendingInfinity) {
			InfinityMendingReplacement.replaceDefaultInfinityClass();
		}
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		metricsSubCommand = new MetricsSC();
		brandSubCommand = new BrandSC();
		speedTabCompleter = new SpeedTC();
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		PseudoAPI.registerConfig(config);
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
		this.getCommand("tp").setExecutor(new TpSC());
		this.getCommand("showitem").setExecutor(new ShowitemSC());
		this.getCommand("enchant").setExecutor(new EnchantSC());
		this.getCommand("moonphase").setExecutor(new MoonPhaseSC());
		this.getCommand("speed").setExecutor(new SpeedSC());
		this.getCommand("flyspeed").setExecutor(new FlySpeedSC());
		this.getCommand("walkspeed").setExecutor(new WalkSpeedSC());
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reset", new ResetSC());
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
		commandDescriptions.add(new CommandDescription("pseudoutils", "Shows PseudoUtils information", ""));
		commandDescriptions.add(new CommandDescription("pseudoutils help", "Shows PseudoUtils commands", ""));
		commandDescriptions.add(new CommandDescription("pseudoutils reload", "Reloads PseudoUtils config", "pseudoutils.reload"));
		commandDescriptions.add(new CommandDescription("pseudoutils reset", "Resets PseudoUtils config", "pseudoutils.reset"));
		commandDescriptions.add(new CommandDescription("back", "Return to previous location", "pseudoutils.back"));
		commandDescriptions.add(new CommandDescription("brand <player>", "Shows user client brand", "pseudoutils.brand", false));
		commandDescriptions.add(new CommandDescription("enchant <enchantment> <level>", "Enchants an item", "pseudoutils.enchant", false));
		commandDescriptions.add(new CommandDescription("fly", "Sets fly mode", "pseudoutils.fly"));
		commandDescriptions.add(new CommandDescription("flyspeed (player) <speed>", "Sets a player's fly speed", "pseudoutils.speed", false));
		commandDescriptions.add(new CommandDescription("god", "Sets god mode", "pseudoutils.god"));
		commandDescriptions.add(new CommandDescription("heal", "Heals player", "pseudoutils.heal"));
		commandDescriptions.add(new CommandDescription("metrics", "Shows server metrics", "pseudoutils.metrics"));
		commandDescriptions.add(new CommandDescription("moonphase", "Shows the current moon phase", "pseudoutils.moonphase"));
		commandDescriptions.add(new CommandDescription("showitem <player>", "Shows an item to a player", "pseudoutils.showitem", false));
		commandDescriptions.add(new CommandDescription("speed (player) <speed>", "Sets a player's fly or walk speed", "pseudoutils.speed", false));
		commandDescriptions.add(new CommandDescription("tp (player) <x> <y> <z> (yaw) (pitch)", "Teleports player", "pseudoutils.tp", false));
		commandDescriptions.add(new CommandDescription("walkspeed (player) <speed>", "Sets a player's walk speed", "pseudoutils.speed", false));
	}

}