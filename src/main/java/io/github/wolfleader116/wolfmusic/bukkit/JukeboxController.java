package io.github.wolfleader116.wolfmusic.bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class JukeboxController {
	
	private static Map<Player, Jukebox> jukeboxes = new HashMap<Player, Jukebox>();
	
	public static void connect(Player p) {
		Jukebox j = new Jukebox(p);
		jukeboxes.put(p, j);
	}
	
	public static void disconnect(Player p) {
		jukeboxes.get(p).kill();
		jukeboxes.remove(p);
	}
	
	public static void kill() {
		for (Jukebox j : jukeboxes.values()) {
			j.kill();
		}
	}
	
	public static void start() {
		for (Jukebox j : jukeboxes.values()) {
			j.nextSong();
		}
	}
	
	public static void nextSong(Player p) {
		jukeboxes.get(p).nextSong();
	}
	
	public static void setSong(Player p, SongFile sf) {
		jukeboxes.get(p).setSong(sf);
	}
	
	public static void lastSong(Player p) {
		jukeboxes.get(p).lastSong();
	}
	
	public static String getSong(Player p) {
		SongFile sf = jukeboxes.get(p).getSong();
		if (sf != null) {
			return sf.getName();
		} else {
			return "";
		}
	}
	
	public static String getNextSong(Player p) {
		return jukeboxes.get(p).nextSongName();
	}
	
	public static String getLastSong(Player p) {
		return jukeboxes.get(p).lastSongName();
	}
	
	public static void stopSong(Player p) {
		jukeboxes.get(p).kill();
	}
	
	public static boolean isPlaying(Player p) {
		return jukeboxes.get(p).isPlaying();
	}
	
	public static Jukebox[] getJukeboxes() {
		List<Jukebox> jbs = new ArrayList<Jukebox>();
		for (Jukebox j : jukeboxes.values()) {
			jbs.add(j);
		}
		return jbs.toArray(new Jukebox[jbs.size()]);
	}

}
