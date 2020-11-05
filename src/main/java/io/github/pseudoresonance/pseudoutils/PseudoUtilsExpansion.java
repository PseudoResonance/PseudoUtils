package io.github.pseudoresonance.pseudoutils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PseudoUtilsExpansion extends PlaceholderExpansion {

	private Plugin plugin;

	public PseudoUtilsExpansion(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public String getIdentifier() {
		return plugin.getName().toLowerCase();
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		switch (identifier) {
		case "tps":
			return LanguageManager.getLanguage(player).getMessage("pseudoutils.placeholder_tps", TPS.getTps());
		case "brand":
			if (player != null)
				return PlayerBrand.getBrand(player.getName());
			else
				return LanguageManager.getLanguage(player).getMessage("pseudoutils.placeholder_not_player");
		case "god_mode":
			if (player != null) {
				Object o = ServerPlayerDataController.getPlayerSetting(player.getUniqueId().toString(), "godMode");
				if (o instanceof Boolean) {
					boolean b = (Boolean) o;
					if (b)
						return LanguageManager.getLanguage(player).getMessage("pseudoutils.placeholder_true");
					else
						return LanguageManager.getLanguage(player).getMessage("pseudoutils.placeholder_false");
				}
			} else
				return LanguageManager.getLanguage(player).getMessage("pseudoutils.placeholder_not_player");
		default:
			return "";
		}
	}

}
