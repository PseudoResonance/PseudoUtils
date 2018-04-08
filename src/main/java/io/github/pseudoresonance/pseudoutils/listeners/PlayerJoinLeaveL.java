package io.github.pseudoresonance.pseudoutils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.pseudoresonance.pseudoutils.PlayerBrand;

public class PlayerJoinLeaveL implements Listener {

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		String name = p.getName();
		PlayerBrand.playerLogout(name);
	}

}
