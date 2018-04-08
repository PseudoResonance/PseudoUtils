package io.github.pseudoresonance.pseudoplayers.listeners;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;

public class PlayerJoinLeaveL implements Listener {

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Location l = p.getLocation();
		String location = l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
		UUID u = p.getUniqueId();
		String uuid = u.toString();
		PlayerDataController.setPlayerSetting(uuid, "logoutLocation", location);
	}

}
