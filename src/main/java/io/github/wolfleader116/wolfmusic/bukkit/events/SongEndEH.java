package io.github.wolfleader116.wolfmusic.bukkit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.xxmicloxx.noteblockapi.SongEndEvent;

import io.github.wolfleader116.wolfmusic.bukkit.Jukebox;
import io.github.wolfleader116.wolfmusic.bukkit.JukeboxController;

public class SongEndEH implements Listener {
	
	@EventHandler
	public void songEnd(SongEndEvent e) {
		for (Jukebox j : JukeboxController.getJukeboxes()) {
			if (e.getSongPlayer() == j.getSongPlayer()) {
				j.done();
				break;
			}
		}
	}
}
