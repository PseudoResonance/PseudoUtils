package io.github.pseudoresonance.pseudoutils.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PseudoUtilsTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			possible.add("help");
			if (sender.hasPermission("pseudoutils.reload")) {
				possible.add("reload");
			}
			if (sender.hasPermission("pseudoutils.reset")) {
				possible.add("reset");
			}
			if (sender.hasPermission("pseudoutils.brand")) {
				possible.add("brand");
			}
			if (sender.hasPermission("pseudoutils.metrics")) {
				possible.add("metrics");
			}
			if (args[0].equalsIgnoreCase("")) {
				return possible;
			} else {
				List<String> checked = new ArrayList<String>();
				for (String check : possible) {
					if (check.toLowerCase().startsWith(args[0].toLowerCase())) {
						checked.add(check);
					}
				}
				return checked;
			}
		}
		return null;
	}

}
