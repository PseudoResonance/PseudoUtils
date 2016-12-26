package io.github.wolfleader116.wolfmusic.bukkit.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class WolfMusicTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			possible.add("help");
			possible.add("reload");
			possible.add("reset");
			possible.add("browse");
			possible.add("play");
			if (args[0].equalsIgnoreCase("")) {
				return possible;
			} else {
				List<String> checked = new ArrayList<String>();
				for (String check : possible) {
					if (check.startsWith(args[0])) {
						checked.add(check);
					}
				}
				return checked;
			}
		}
		return null;
	}

}
