package io.github.wolfleader116.wolfmusic.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.wolfleader116.wolfmusic.bukkit.JukeboxController;
import io.github.wolfleader116.wolfmusic.bukkit.WolfMusic;

public class PlayerJoinLeaveEH implements Listener {
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		JukeboxController.connect(p);
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		JukeboxController.disconnect(p);
		WolfMusic.removePage(p.getName());
	}
}
