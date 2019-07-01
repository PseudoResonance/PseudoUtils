package io.github.pseudoresonance.pseudoutils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoutils.PlayerBrand;

public class PlayerJoinLeaveL implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Object o = PlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "flyMode");
		if (o != null && o instanceof Boolean && (Boolean) o == true) {
			p.setAllowFlight(true);
			if (!p.isOnGround())
				p.setFlying(true);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		String name = p.getName();
		PlayerBrand.playerLogout(name);
	}

}
