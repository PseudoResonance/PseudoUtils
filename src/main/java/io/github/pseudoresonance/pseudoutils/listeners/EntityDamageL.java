package io.github.pseudoresonance.pseudoutils.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.ServerPlayerDataController;

public class EntityDamageL implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Entity ent = e.getEntity();
		if (ent instanceof Player) {
			Player p = (Player) ent;
			Object o = ServerPlayerDataController.getPlayerSetting(p.getUniqueId().toString(), "godMode").join();
			if (o instanceof Boolean) {
				boolean b = (Boolean) o;
				if (b)
					e.setCancelled(true);
			}
		}
	}

}
