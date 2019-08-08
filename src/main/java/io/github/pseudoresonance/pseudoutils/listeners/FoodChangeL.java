package io.github.pseudoresonance.pseudoutils.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;

public class FoodChangeL implements Listener {

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		Entity ent = e.getEntity();
		if (ent instanceof Player) {
			Player p = (Player) ent;
			Object o = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "godMode").join();
			if (o instanceof Boolean) {
				boolean b = (Boolean) o;
				if (b) {
					p.setFoodLevel(20);
					p.setSaturation(20);
					e.setCancelled(true);
				}
			}
		}
	}

}
