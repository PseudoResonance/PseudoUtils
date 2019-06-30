package io.github.pseudoresonance.pseudoutils;

import io.github.pseudoresonance.pseudoapi.bukkit.CommandDescription;
import io.github.pseudoresonance.pseudoapi.bukkit.HelpSC;
import io.github.pseudoresonance.pseudoapi.bukkit.MainCommand;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoUpdater;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.Column;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoutils.commands.BrandSC;
import io.github.pseudoresonance.pseudoutils.commands.FlySC;
import io.github.pseudoresonance.pseudoutils.commands.GodSC;
import io.github.pseudoresonance.pseudoutils.commands.MetricsSC;
import io.github.pseudoresonance.pseudoutils.commands.ReloadSC;
import io.github.pseudoresonance.pseudoutils.commands.ResetSC;
import io.github.pseudoresonance.pseudoutils.completers.PseudoUtilsTC;
import io.github.pseudoresonance.pseudoutils.listeners.ClientBrandL;
import io.github.pseudoresonance.pseudoutils.listeners.EntityDamageL;
import io.github.pseudoresonance.pseudoutils.listeners.FoodChangeL;
import io.github.pseudoresonance.pseudoutils.listeners.PlayerJoinLeaveL;

public class PseudoUtils extends PseudoPlugin {

	public static PseudoPlugin plugin;
	public static Message message;

	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	private static MetricsSC metricsSubCommand;
	private static BrandSC brandSubCommand;

	private static ConfigOptions configOptions;
	
	public void onLoad() {
		PseudoUpdater.registerPlugin(this);
	}

	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		plugin = this;
		PlayerDataController.addColumn(new Column("godMode", "BIT", "0"));
		PlayerDataController.addColumn(new Column("ip", "VARCHAR(15)", "'0.0.0.0'"));
		TPS.startTps();
		configOptions = new ConfigOptions();
		ConfigOptions.updateConfig();
		configOptions.reloadConfig();
		message = new Message(this);
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		metricsSubCommand = new MetricsSC();
		brandSubCommand = new BrandSC();
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		PseudoAPI.registerConfig(configOptions);
	}

	public static ConfigOptions getConfigOptions() {
		return PseudoUtils.configOptions;
	}

	private void initializeCommands() {
		this.getCommand("pseudoutils").setExecutor(mainCommand);
		this.getCommand("metrics").setExecutor(metricsSubCommand);
		this.getCommand("brand").setExecutor(brandSubCommand);
		this.getCommand("god").setExecutor(new GodSC());
		this.getCommand("fly").setExecutor(new FlySC());
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
	}

	private void initializeListeners() {
		getServer().getPluginManager().registerEvents(new PlayerJoinLeaveL(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageL(), this);
		getServer().getPluginManager().registerEvents(new FoodChangeL(), this);
		getServer().getMessenger().registerIncomingPluginChannel(PseudoAPI.plugin, "minecraft:brand", new ClientBrandL());
	}

	private void setCommandDescriptions() {
		commandDescriptions.add(new CommandDescription("pseudoutils", "Shows PseudoUtils information", ""));
		commandDescriptions.add(new CommandDescription("pseudoutils help", "Shows PseudoUtils commands", ""));
		commandDescriptions.add(new CommandDescription("pseudoutils reload", "Reloads PseudoUtils config", "pseudoutils.reload"));
		commandDescriptions.add(new CommandDescription("pseudoutils reset", "Resets PseudoUtils config", "pseudoutils.reset"));
		commandDescriptions.add(new CommandDescription("god", "Sets god mode", "pseudoutils.god"));
		commandDescriptions.add(new CommandDescription("fly", "Sets fly mode", "pseudoutils.fly"));
		commandDescriptions.add(new CommandDescription("metrics", "Shows server metrics", "pseudoutils.metrics"));
		commandDescriptions.add(new CommandDescription("brand <player>", "Shows user client brand", "pseudoutils.brand", false));
	}

}