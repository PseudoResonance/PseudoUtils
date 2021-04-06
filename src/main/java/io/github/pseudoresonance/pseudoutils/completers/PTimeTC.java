package io.github.pseudoresonance.pseudoutils.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PTimeTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (sender.hasPermission("pseudoutils.ptime")) {
			if (args.length == 1) {
				possible.add("add");
				possible.add("query");
				possible.add("reset");
				possible.add("set");
				possible.add("toggleDaylightCycle");
			} else if (args.length == 2) {
				switch (args[0].toLowerCase()) {
				case "add":
					if (args[1].length() > 0) {
						char end = Character.toLowerCase(args[1].charAt(args[1].length() - 1));
						if ('0' <= end && end <= '9') {
							possible.add(args[1] + "s");
							possible.add(args[1] + "t");
						}
					}
					break;
				case "query":
					possible.add("day");
					possible.add("daytime");
					possible.add("gametime");
					break;
				case "set":
					possible.add("day");
					possible.add("midnight");
					possible.add("night");
					possible.add("noon");
					break;
				case "reset":
				case "toggleDaylightCycle":
					for (Player p : Bukkit.getOnlinePlayers()) {
						possible.add(p.getName());
					}
					break;
				}
			} else if (args.length == 3) {
				switch (args[0].toLowerCase()) {
				case "add":
				case "query":
				case "set":
					for (Player p : Bukkit.getOnlinePlayers()) {
						possible.add(p.getName());
					}
					break;
				}
			}
		}
		if (args.length > 0) {
			if (args[args.length - 1].equalsIgnoreCase("")) {
				return possible;
			} else {
				List<String> checked = new ArrayList<String>();
				for (String check : possible) {
					if (check.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
						checked.add(check);
					}
				}
				return checked;
			}
		}
		return possible;
	}

}
