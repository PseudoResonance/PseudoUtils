package io.github.pseudoresonance.pseudoutils.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class GamemodeTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			if (sender.hasPermission("pseudoutils.gamemode")) {
				possible.add("survival");
				possible.add("creative");
				possible.add("adventure");
				possible.add("spectator");
			}
		} else if (args.length == 2)
			if (sender.hasPermission("pseudoutils.gamemode.others"))
				for (Player p : Bukkit.getOnlinePlayers())
					possible.add(p.getName().toLowerCase());
		else
			return possible;
		if (args[args.length - 1].equalsIgnoreCase(""))
			return possible;
		else {
			List<String> checked = new ArrayList<String>();
			for (String check : possible) {
				if (check.startsWith(args[args.length - 1].toLowerCase())) {
					checked.add(check);
				}
			}
			return checked;
		}
	}

}
