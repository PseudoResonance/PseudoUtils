package io.github.pseudoresonance.pseudoutils.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;

public class EnchantTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			if (sender.hasPermission("pseudoutils.enchant")) {
				for (Enchantment e : Enchantment.values()) {
					possible.add(e.getKey().toString());
				}
			}
			if (args[0].equalsIgnoreCase("")) {
				return possible;
			} else {
				List<String> checked = new ArrayList<String>();
				String[] split = args[0].split(":");
				if (split.length == 1) {
					for (String check : possible) {
						if (check.toLowerCase().split(":")[1].startsWith(args[0].toLowerCase())) {
							checked.add(check);
						}
					}
				}
				for (String check : possible) {
					if (check.toLowerCase().startsWith(args[0].toLowerCase())) {
						checked.add(check);
					}
				}
				return checked;
			}
		} else if (args.length >= 2) {
			return possible;
		}
		return null;
	}

}
