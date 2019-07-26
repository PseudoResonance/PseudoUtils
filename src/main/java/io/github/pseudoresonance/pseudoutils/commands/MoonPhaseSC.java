package io.github.pseudoresonance.pseudoutils.commands;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class MoonPhaseSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoutils.moonphase")) {
				Player p = (Player) sender;
				World w = p.getWorld();
				if (w.getEnvironment() == Environment.NORMAL) {
					long day = w.getFullTime()/24000;
					int phase = (int) (day % 8);
					String phaseName = "Full Moon";
					switch (phase) {
						case 1:
							phaseName = "Waning Gibbous";
							break;
						case 2:
							phaseName = "Last Quarter";
							break;
						case 3:
							phaseName = "Waning Crescent";
							break;
						case 4:
							phaseName = "New Moon";
							break;
						case 5:
							phaseName = "Waxing Crescent";
							break;
						case 6:
							phaseName = "First Quarter";
							break;
						case 7:
							phaseName = "Waxing Gibbous";
					}
					PseudoUtils.message.sendPluginMessage(sender, "The current phase is: " + phaseName);
					return true;
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "You can only run this command in the overworld!");
					return false;
				}
			} else {
				PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "get current moon phase!");
				return false;
			}
		} else {
			PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Only players can run this command!");
			return false;
		}
	}

}
