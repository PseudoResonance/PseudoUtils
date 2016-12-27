package io.github.wolfleader116.wolfmusic.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfmusic.bukkit.JukeboxController;
import io.github.wolfleader116.wolfmusic.bukkit.SongFile;
import io.github.wolfleader116.wolfmusic.bukkit.WolfMusic;

public class StopSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("wolfmusic.stop")) {
				SongFile[] songs = WolfMusic.getSongs();
				if (songs.length == 0) {
					WolfMusic.message.sendPluginError(sender, Errors.CUSTOM, "There are no songs on the server!");
				} else {
					Player p = (Player) sender;
					if (JukeboxController.isPlaying(p)) {
						JukeboxController.stopSong(p);
					} else {
						WolfMusic.message.sendPluginMessage(sender, "There isn't music playing!");
					}
				}
			} else {
				WolfMusic.message.sendPluginError(sender, Errors.NO_PERMISSION, "stop playing music!");
				return false;
			}
		} else {
			WolfMusic.message.sendPluginError(sender, Errors.CUSTOM, "This command is for players only!");
			return false;
		}
		return true;
	}

}
