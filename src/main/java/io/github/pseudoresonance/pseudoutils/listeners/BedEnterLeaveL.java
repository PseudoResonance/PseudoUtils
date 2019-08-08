package io.github.pseudoresonance.pseudoutils.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.pseudoresonance.pseudoutils.Config;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

public class BedEnterLeaveL implements Listener {
	
	private static HashMap<UUID, ArrayList<UUID>> playersInBed = new HashMap<UUID, ArrayList<UUID>>();
	private static HashMap<UUID, Integer> sleepTimers = new HashMap<UUID, Integer>();
	private static ArrayList<UUID> justSlept = new ArrayList<UUID>();

	@EventHandler
	public void onEnterBed(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		if (e.useBed() == Result.ALLOW || e.getBedEnterResult() == BedEnterResult.OK) {
			if (!playersInBed.containsKey(p.getWorld().getUID())) {
				playersInBed.put(p.getWorld().getUID(), new ArrayList<UUID>());
			}
			playersInBed.get(p.getWorld().getUID()).add(p.getUniqueId());
			if (Config.sleepEnable) {
				int playersInWorld = p.getWorld().getPlayers().size();
				int requiredPlayers = (int) Math.ceil(playersInWorld * (Config.requiredSleepPercentage / 100.0));
				int playersLeft = requiredPlayers - playersInBed.get(p.getWorld().getUID()).size();
				String msg = e.getPlayer().getDisplayName() + io.github.pseudoresonance.pseudoapi.bukkit.Config.textColor + " is sleeping. " + playersInBed.get(p.getWorld().getUID()).size() + "/" + playersInWorld;
				if (playersLeft > 0) {
					msg += " " + playersLeft + " more must sleep to skip! (" + Config.requiredSleepPercentage + "% Minimum)";
				} else {
					msg += " (" + Config.requiredSleepPercentage + "% Minimum)";
				}
				for (Player pl : p.getWorld().getPlayers()) {
					PseudoUtils.message.sendPluginMessage(pl, msg);
				}
				if (playersInBed.get(p.getWorld().getUID()).size() >= requiredPlayers) {
					if (!sleepTimers.containsKey(p.getWorld().getUID())) {
						sleepTimers.put(p.getWorld().getUID(), new SleepTimer(p.getWorld().getUID()).runTaskLater(PseudoUtils.plugin, 100).getTaskId());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onLeaveBed(PlayerBedLeaveEvent e) {
		Player p = e.getPlayer();
		if (!justSlept.contains(e.getPlayer().getWorld().getUID())) {
			if (!playersInBed.containsKey(p.getWorld().getUID())) {
				playersInBed.put(p.getWorld().getUID(), new ArrayList<UUID>());
			}
			playersInBed.get(p.getWorld().getUID()).remove(p.getUniqueId());
			if (Config.sleepEnable && !(p.getWorld().getTime() >= 23450 && p.getWorld().getTime() <= 23500)) {
				int playersInWorld = p.getWorld().getPlayers().size();
				int requiredPlayers = (int) Math.ceil(playersInWorld * (Config.requiredSleepPercentage / 100.0));
				int playersLeft = requiredPlayers - playersInBed.get(p.getWorld().getUID()).size();
				String msg = e.getPlayer().getDisplayName() + io.github.pseudoresonance.pseudoapi.bukkit.Config.textColor + " left bed. " + playersInBed.get(p.getWorld().getUID()).size() + "/" + playersInWorld;
				if (playersLeft > 0) {
					msg += " " + playersLeft + " more must sleep to skip! (" + Config.requiredSleepPercentage + "% Minimum)";
				} else {
					msg += " (" + Config.requiredSleepPercentage + "% Minimum)";
				}
				for (Player pl : p.getWorld().getPlayers()) {
					PseudoUtils.message.sendPluginMessage(pl, msg);
				}
				if (!(playersInBed.get(p.getWorld().getUID()).size() >= requiredPlayers)) {
					if (sleepTimers.containsKey(p.getWorld().getUID())) {
						PseudoUtils.plugin.getServer().getScheduler().cancelTask(sleepTimers.get(p.getWorld().getUID()));
						sleepTimers.remove(p.getWorld().getUID());
						return;
					}
				}
			}
		}
		if (e.getPlayer().getWorld().getTime() >= 0 && e.getPlayer().getWorld().getTime() < 12541 && !e.getPlayer().getWorld().isThundering() && Config.sleepEnable) {
			if (sleepTimers.containsKey(p.getWorld().getUID())) {
				PseudoUtils.plugin.getServer().getScheduler().cancelTask(sleepTimers.get(p.getWorld().getUID()));
				sleepTimers.remove(p.getWorld().getUID());
			}
		}
	}
	
	public void sleepWorld(World w) {
		if (Config.sleepEnable) {
			for (Player p : w.getPlayers()) {
				PseudoUtils.message.sendPluginMessage(p, "Skipped to morning!");
			}
			if (w.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)) {
				w.setTime(24000);
			}
			if (w.getGameRuleValue(GameRule.DO_WEATHER_CYCLE)) {
				w.setStorm(false);
				if (w.hasStorm())
					w.setWeatherDuration(0);
				w.setThundering(false);
				if (w.isThundering())
					w.setThunderDuration(0);
			}
			justSlept.add(w.getUID());
			for (UUID uuid : playersInBed.get(w.getUID())) {
				Player p = Bukkit.getPlayer(uuid);
				p.wakeup(true);
			}
			playersInBed.get(w.getUID()).clear();
			PseudoUtils.plugin.getServer().getScheduler().scheduleSyncDelayedTask(PseudoUtils.plugin, new Runnable() {
				@Override
				public void run() {
					justSlept.remove(w.getUID());
				}
			}, 1);
		}
	}
	
	public class SleepTimer extends BukkitRunnable {
		
		private UUID w;
		
		public SleepTimer(UUID w) {
			this.w = w;
		}

		@Override
		public void run() {
			sleepWorld(PseudoUtils.plugin.getServer().getWorld(w));
		}
		
	}

}
