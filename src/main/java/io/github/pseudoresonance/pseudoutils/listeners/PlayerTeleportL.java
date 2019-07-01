package io.github.pseudoresonance.pseudoutils.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;

public class PlayerTeleportL implements Listener {

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if (e.getCause() == TeleportCause.COMMAND || e.getCause() == TeleportCause.PLUGIN || e.getCause() == TeleportCause.UNKNOWN) {
			Location l = e.getFrom();
			String location = l.getWorld().getUID().toString() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
			PlayerDataController.setPlayerSetting(p.getUniqueId().toString(), "backLocation", location);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (p.hasPermission("pseudoutils.back.ondeath")) {
			Location l = p.getLocation();
			String location = l.getWorld().getUID().toString() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
			PlayerDataController.setPlayerSetting(p.getUniqueId().toString(), "backLocation", location);
		}
	}

}
