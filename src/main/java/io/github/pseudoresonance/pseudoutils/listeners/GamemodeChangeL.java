package io.github.pseudoresonance.pseudoutils.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class GamemodeChangeL implements Listener {

	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		Object o = PlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "flyMode");
		if (o instanceof Boolean) {
			boolean b = (Boolean) o;
			boolean fly = p.isFlying();
			if (e.getNewGameMode() != GameMode.SPECTATOR) {
				PseudoUtils.plugin.getServer().getScheduler().scheduleSyncDelayedTask(PseudoUtils.plugin, new Runnable() {
					@Override
					public void run() {
						p.setAllowFlight(b);
						if (b)
							p.setFlying(fly);
					}
				}, 1);
			}
		}
	}

}
