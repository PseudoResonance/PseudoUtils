package io.github.wolfleader116.wolfmusic.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.xxmicloxx.noteblockapi.RadioSongPlayer;
import com.xxmicloxx.noteblockapi.SongPlayer;

public class Jukebox {
	
	private Player player;
	private SongFile songFile;
	private SongPlayer songPlayer;
	private boolean playing = false;
	private BossBar bossBar;
	
	Jukebox(Player player) {
		this.player = player;
		nextSong();
	}

	public void setSong(SongFile sf) {
		kill();
		songFile = sf;
		songPlayer = new RadioSongPlayer(songFile.getSong());
		songPlayer.setAutoDestroy(true);
		songPlayer.addPlayer(player);
		songPlayer.setPlaying(true);
		playing = true;
		if (ConfigOptions.bossBar) {
			String barMessage = ConfigOptions.barMessage;
			barMessage = barMessage.replace("{name}", songFile.getName());
			barMessage = barMessage.replace("{cname}", songFile.getColor() + songFile.getName());
			bossBar = Bukkit.getServer().createBossBar(barMessage, songFile.getBarColor(), BarStyle.SOLID);
			bossBar.addPlayer(player);
			bossBar.setVisible(true);
			if (ConfigOptions.barVisibility != 0) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(WolfMusic.plugin, new Runnable() {
					public void run() {
						bossBar.setVisible(false);
					}
				}, ConfigOptions.barVisibility);
			}
		}
		if (ConfigOptions.title) {
			String titleMessage = ConfigOptions.titleMessage;
			titleMessage = titleMessage.replace("{name}", songFile.getName());
			titleMessage = titleMessage.replace("{cname}", songFile.getColor() + songFile.getName());
			player.sendTitle("", titleMessage, ConfigOptions.titleFade, ConfigOptions.titleVisibility, ConfigOptions.titleFade);
		}
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public SongFile getSong() {
		return songFile;
	}
	
	public void kill() {
		if (ConfigOptions.bossBar) {
			if (bossBar != null) {
				bossBar.setVisible(false);
			}
		}
		if (songPlayer != null) {
			songPlayer.destroy();
		}
		songPlayer = null;
		songFile = null;
		playing = false;
	}
	
	public String nextSongName() {
		if (WolfMusic.songs.size() >= 1) {
			int i = 0;
			if (songFile != null) {
				i = WolfMusic.songs.indexOf(songFile);
				i++;
				if (i >= WolfMusic.songs.size()) {
					i = 0;
				}
			}
			if (WolfMusic.songs.size() == 1) {
				i = 0;
			}
			SongFile sf = WolfMusic.getSongs()[i];
			return sf.getName();
		}
		return "None";
	}
	
	public String lastSongName() {
		if (WolfMusic.songs.size() >= 1) {
			int i = WolfMusic.songs.size() - 1;
			if (songFile != null) {
				i = WolfMusic.songs.indexOf(songFile);
				i--;
				if (i < 0) {
					i = WolfMusic.songs.size() - 1;
				}
			}
			if (WolfMusic.songs.size() == 1) {
				i = 0;
			}
			SongFile sf = WolfMusic.getSongs()[i];
			return sf.getName();
		}
		return "None";
	}
	
	public void nextSong() {
		if (WolfMusic.songs.size() >= 1) {
			if (songPlayer != null) {
				songPlayer.destroy();
			}
			if (bossBar != null) {
				bossBar.setVisible(false);
			}
			int i = 0;
			if (songFile != null) {
				i = WolfMusic.songs.indexOf(songFile);
				i++;
				if (i >= WolfMusic.songs.size()) {
					i = 0;
				}
			}
			if (WolfMusic.songs.size() == 1) {
				i = 0;
			}
			songFile = WolfMusic.getSongs()[i];
			songPlayer = new RadioSongPlayer(songFile.getSong());
			songPlayer.setAutoDestroy(true);
			songPlayer.addPlayer(player);
			songPlayer.setPlaying(true);
			playing = true;
			if (ConfigOptions.bossBar) {
				String barMessage = ConfigOptions.barMessage;
				barMessage = barMessage.replace("{name}", songFile.getName());
				barMessage = barMessage.replace("{cname}", songFile.getColor() + songFile.getName());
				bossBar = Bukkit.getServer().createBossBar(barMessage, songFile.getBarColor(), BarStyle.SOLID);
				bossBar.addPlayer(player);
				bossBar.setVisible(true);
			}
			if (ConfigOptions.title) {
				String titleMessage = ConfigOptions.titleMessage;
				titleMessage = titleMessage.replace("{name}", songFile.getName());
				titleMessage = titleMessage.replace("{cname}", songFile.getColor() + songFile.getName());
				player.sendTitle("", titleMessage, ConfigOptions.titleFade, ConfigOptions.titleVisibility, ConfigOptions.titleFade);
			}
		}
	}
	
	public void lastSong() {
		if (WolfMusic.songs.size() >= 1) {
			if (songPlayer != null) {
				songPlayer.destroy();
			}
			if (bossBar != null) {
				bossBar.setVisible(false);
			}
			int i = WolfMusic.songs.size() - 1;
			if (songFile != null) {
				i = WolfMusic.songs.indexOf(songFile);
				i--;
				if (i < 0) {
					i = WolfMusic.songs.size() - 1;
				}
			}
			if (WolfMusic.songs.size() == 1) {
				i = 0;
			}
			songFile = WolfMusic.getSongs()[i];
			songPlayer = new RadioSongPlayer(songFile.getSong());
			songPlayer.setAutoDestroy(true);
			songPlayer.addPlayer(player);
			songPlayer.setPlaying(true);
			playing = true;
			if (ConfigOptions.bossBar) {
				String barMessage = ConfigOptions.barMessage;
				barMessage = barMessage.replace("{name}", songFile.getName());
				barMessage = barMessage.replace("{cname}", songFile.getColor() + songFile.getName());
				bossBar = Bukkit.getServer().createBossBar(barMessage, songFile.getBarColor(), BarStyle.SOLID);
				bossBar.addPlayer(player);
				bossBar.setVisible(true);
			}
			if (ConfigOptions.title) {
				String titleMessage = ConfigOptions.titleMessage;
				titleMessage = titleMessage.replace("{name}", songFile.getName());
				titleMessage = titleMessage.replace("{cname}", songFile.getColor() + songFile.getName());
				player.sendTitle("", titleMessage, ConfigOptions.titleFade, ConfigOptions.titleVisibility, ConfigOptions.titleFade);
			}
		}
	}
	
	public void done() {
		if (ConfigOptions.bossBar) {
			if (bossBar != null) {
				bossBar.setVisible(false);
			}
		}
		if (ConfigOptions.playlist) {
			long d = new Long((int) Math.round(ConfigOptions.playlistDelay * 20));
			if (ConfigOptions.autoStart) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(WolfMusic.plugin, new Runnable() {
					public void run() {
						nextSong();
					}
				}, d);
			}
		}
	}
	
	public SongPlayer getSongPlayer() {
		return this.songPlayer;
	}

}
