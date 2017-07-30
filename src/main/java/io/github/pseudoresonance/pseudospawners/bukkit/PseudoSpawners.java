package io.github.pseudoresonance.pseudospawners.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.pseudoresonance.pseudospawners.bukkit.commands.ReloadSC;
import io.github.pseudoresonance.pseudospawners.bukkit.commands.ResetSC;
import io.github.pseudoresonance.pseudospawners.bukkit.commands.EditSpawnerSC;
import io.github.pseudoresonance.pseudospawners.bukkit.commands.SpawnerSC;
import io.github.pseudoresonance.pseudospawners.bukkit.completers.EditSpawnerTC;
import io.github.pseudoresonance.pseudospawners.bukkit.completers.SpawnerTC;
import io.github.pseudoresonance.pseudospawners.bukkit.completers.WolfSpawnersTC;
import io.github.pseudoresonance.pseudospawners.bukkit.events.BlockBreakEH;
import io.github.pseudoresonance.pseudospawners.bukkit.events.BlockPlaceEH;
import io.github.pseudoresonance.pseudospawners.bukkit.events.InventoryClickEH;
import io.github.pseudoresonance.pseudospawners.bukkit.events.PlayerInteractEH;
import io.github.wolfleader116.wolfapi.bukkit.CommandDescription;
import io.github.wolfleader116.wolfapi.bukkit.HelpSC;
import io.github.wolfleader116.wolfapi.bukkit.MainCommand;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import io.github.wolfleader116.wolfapi.bukkit.WolfPlugin;

public class PseudoSpawners extends WolfPlugin {

	public static WolfPlugin plugin;
	public static Message message;
	
	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	
	private static ConfigOptions configOptions;
	
	private static Map<String, Integer> page = new HashMap<String, Integer>();
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		plugin = this;
		configOptions = new ConfigOptions();
		ConfigOptions.updateConfig();
		message = new Message(this);
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		configOptions.reloadConfig();
		WolfAPI.registerConfig(configOptions);
		createRecipes();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	public static ConfigOptions getConfigOptions() {
		return PseudoSpawners.configOptions;
	}

	private void initializeCommands() {
		this.getCommand("pseudospawners").setExecutor(mainCommand);
		this.getCommand("spawner").setExecutor(new SpawnerSC());
		this.getCommand("editspawner").setExecutor(new EditSpawnerSC());
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reset", new ResetSC());
		subCommands.put("spawner", new SpawnerSC());
		subCommands.put("editspawner", new EditSpawnerSC());
	}

	private void initializeTabcompleters() {
		this.getCommand("pseudospawners").setTabCompleter(new WolfSpawnersTC());
		this.getCommand("spawner").setTabCompleter(new SpawnerTC());
		this.getCommand("editspawner").setTabCompleter(new EditSpawnerTC());
	}
	
	private void initializeListeners() {
		getServer().getPluginManager().registerEvents(new InventoryClickEH(), this);
		getServer().getPluginManager().registerEvents(new BlockPlaceEH(), this);
		getServer().getPluginManager().registerEvents(new BlockBreakEH(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractEH(), this);
	}

	private void setCommandDescriptions() {
		commandDescriptions.add(new CommandDescription("pseudospawners", "Shows WolfSpawners information", ""));
		commandDescriptions.add(new CommandDescription("pseudospawners help", "Shows WolfSpawners commands", ""));
		commandDescriptions.add(new CommandDescription("pseudospawners reload", "Reloads WolfSpawners config", "pseudospawners.reload"));
		commandDescriptions.add(new CommandDescription("pseudospawners reset", "Resets WolfSpawners config", "pseudospawners.reset"));
		commandDescriptions.add(new CommandDescription("pseudospawners spawner", "Sets the type of the spawner you are looking at", "pseudospawners.spawner"));
		commandDescriptions.add(new CommandDescription("pseudospawners editspawner", "Edits the data of the spawner you are looking at", "pseudospawners.edit"));
	}
	
	public static Map<String, Integer> getPages() {
		return page;
	}
	
	public static int getPage(String p) {
		return page.get(p);
	}
	
	public static void removePage(String p) {
		page.remove(p);
	}
	
	public static void setPage(String p, int i) {
		page.put(p, i);
	}
	
	protected static ItemStack newSpawner(String name) {
		ItemStack is = new ItemStack(Material.MOB_SPAWNER, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	private static void createRecipes() {
		NamespacedKey key = new NamespacedKey(plugin, "spawner");
		ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1);
		ShapedRecipe rec = new ShapedRecipe(key, spawner);
		rec.shape("***", "*%*", "***");
		rec.setIngredient('*', Material.IRON_FENCE);
		rec.setIngredient('%', Material.MONSTER_EGG);
		Bukkit.getServer().addRecipe(rec);
	}

}