package io.github.pseudoresonance.pseudoutils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;
import io.github.pseudoresonance.pseudoutils.Config;
import io.github.pseudoresonance.pseudoutils.PlayerBrand;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoinLeaveL implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Object o = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "flyMode").join();
		if (o != null && o instanceof Boolean && (Boolean) o == true) {
			p.setAllowFlight(true);
			if (!p.isOnGround())
				p.setFlying(true);
		}
		if (Config.enableJoinLeave) {
			if (Config.joinFormat.equals("")) {
				e.setJoinMessage("");
			} else {
				String format = Config.joinFormat;
				format = format.replace("{name}", p.getName());
				format = format.replace("{nickname}", p.getDisplayName());
				format = format.replace("{uuid}", p.getUniqueId().toString());
				format = ChatColor.translateAlternateColorCodes('&', format);
				e.setJoinMessage(format);
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		String name = p.getName();
		PlayerBrand.playerLogout(name);
		ServerPlayerDataController.setPlayerSetting(p.getUniqueId().toString(), "flyMode", p.getAllowFlight()).join();
		if (Config.enableJoinLeave) {
			if (Config.leaveFormat.equals("")) {
				e.setQuitMessage("");
			} else {
				String format = Config.leaveFormat;
				format = format.replace("{name}", p.getName());
				format = format.replace("{nickname}", p.getDisplayName());
				format = format.replace("{uuid}", p.getUniqueId().toString());
				format = ChatColor.translateAlternateColorCodes('&', format);
				e.setQuitMessage(format);
			}
		}
	}

}
