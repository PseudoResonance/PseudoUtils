package io.github.pseudoresonance.pseudoutils.commands;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.Utils;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import io.github.pseudoresonance.pseudoutils.TPS;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

public class MetricsSC implements SubCommandExecutor {
	
	private static SystemInfo info = new SystemInfo();
	
	private static final boolean useSi = false;

	private static final DecimalFormat df = new DecimalFormat("#.##");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics"))) {
			List<Object> messages = new ArrayList<Object>();
			CentralProcessor cpu = null;
			messages.add(Config.borderColor + "===---" + Config.titleColor + "Server Metrics" + Config.borderColor + "---===");
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.other"))) {
				messages.add(Config.borderColor + "---" + Config.titleColor + "Other" + Config.borderColor + "---");
				messages.add(Config.descriptionColor + "Unique Players Joined: " + Config.commandColor + PlayerDataController.getUniquePlayers());
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.java"))) {
				OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
				OperatingSystem os = info.getOperatingSystem();
				messages.add(Config.borderColor + "---" + Config.titleColor + "Java & OS" + Config.borderColor + "---");
				messages.add(Config.descriptionColor + "Java Version: " + Config.commandColor + System.getProperty("java.version"));
				messages.add(Config.descriptionColor + "Java Vendor: " + Config.commandColor + System.getProperty("java.vendor"));
				messages.add(Config.descriptionColor + "OS Manufacturer: " + Config.commandColor + os.getManufacturer());
				messages.add(Config.descriptionColor + "OS: " + Config.commandColor + os.getFamily() + " " + os.getVersion());
				messages.add(Config.descriptionColor + "OS Arch: " + Config.commandColor + osb.getArch() + " " + os.getBitness() + "-bit");
				messages.add(Config.descriptionColor + "System Time: " + Config.commandColor + LocalDateTime.now().format(DateTimeFormatter.ofPattern(io.github.pseudoresonance.pseudoutils.Config.timeFormat)));
				messages.add(Config.descriptionColor + "System Date: " + Config.commandColor + LocalDateTime.now().format(DateTimeFormatter.ofPattern(io.github.pseudoresonance.pseudoutils.Config.dateFormat)));
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.tps"))) {
				if (cpu == null)
					cpu = info.getHardware().getProcessor();
				RuntimeMXBean rsb = ManagementFactory.getRuntimeMXBean();
				messages.add(Config.borderColor + "---" + Config.titleColor + "TPS and Time" + Config.borderColor + "---");
				messages.add(Config.descriptionColor + "TPS: " + Config.commandColor + TPS.getTps());
				messages.add(Config.descriptionColor + "Uptime: " + Config.commandColor + Utils.millisToHumanFormat(rsb.getUptime()));
				messages.add(Config.descriptionColor + "System Uptime: " + Config.commandColor + secsToHumanFormat(cpu.getSystemUptime()));
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.storage"))) {
				long totalBytes = 0;
				long freeBytes = 0;
				long usedBytes = 0;
				for (OSFileStore fs : info.getOperatingSystem().getFileSystem().getFileStores()) {
					totalBytes += fs.getTotalSpace();
					freeBytes += fs.getUsableSpace();
				}
				usedBytes = totalBytes - freeBytes;
				messages.add(Config.borderColor + "---" + Config.titleColor + "Storage" + Config.borderColor + "---");
				messages.add(Config.descriptionColor + "Space Used: " + Config.commandColor + bytesToHumanFormat(usedBytes, useSi));
				messages.add(Config.descriptionColor + "Free Space: " + Config.commandColor + bytesToHumanFormat(freeBytes, useSi));
				messages.add(Config.descriptionColor + "Total Space: " + Config.commandColor + bytesToHumanFormat(totalBytes, useSi));
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.cpu"))) {
				if (cpu == null)
					cpu = info.getHardware().getProcessor();
				messages.add(Config.borderColor + "---" + Config.titleColor + "CPU" + Config.borderColor + "---");
				messages.add(Config.descriptionColor + "Model: " + Config.commandColor + cpu.getName());
				messages.add(Config.descriptionColor + "Cores: " + Config.commandColor + cpu.getPhysicalProcessorCount());
				if (cpu.getPhysicalPackageCount() > 1) {
					messages.add(Config.descriptionColor + "Sockets: " + Config.commandColor + cpu.getPhysicalPackageCount());
					messages.add(Config.descriptionColor + "Cores per Socket: " + Config.commandColor + ((Integer) (cpu.getPhysicalProcessorCount() / cpu.getPhysicalPackageCount())));
				}
				messages.add(Config.descriptionColor + "Threads: " + Config.commandColor + cpu.getLogicalProcessorCount());
				messages.add(Config.descriptionColor + "Clock: " + Config.commandColor + (cpu.getVendorFreq() >= 0 ? Double.valueOf(df.format(cpu.getVendorFreq() / 1000000000.0)) + "GHz" : "Unknown"));
				messages.add(Config.descriptionColor + "Load: " + Config.commandColor + (cpu.getSystemCpuLoad() >= 0 ? Double.valueOf(df.format(cpu.getSystemCpuLoad() * 100.0)) + "%" : "Unknown"));
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.memory"))) {
				GlobalMemory ram = info.getHardware().getMemory();
				messages.add(Config.borderColor + "---" + Config.titleColor + "Memory" + Config.borderColor + "---");
				messages.add(Config.descriptionColor + "Used Memory: " + Config.commandColor + bytesToHumanFormat(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), useSi));
				messages.add(Config.descriptionColor + "Free JVM Memory: " + Config.commandColor + bytesToHumanFormat(Runtime.getRuntime().freeMemory(), useSi));
				messages.add(Config.descriptionColor + "Total JVM Memory: " + Config.commandColor + bytesToHumanFormat(Runtime.getRuntime().totalMemory(), useSi));
				messages.add(Config.descriptionColor + "Max JVM Memory: " + Config.commandColor + (Runtime.getRuntime().maxMemory() == Long.MAX_VALUE ? "Unlimited" : bytesToHumanFormat(Runtime.getRuntime().maxMemory(), useSi)));
				messages.add(Config.descriptionColor + "Available Memory: " + Config.commandColor + bytesToHumanFormat(ram.getAvailable(), useSi));
				messages.add(Config.descriptionColor + "Total Memory: " + Config.commandColor + bytesToHumanFormat(ram.getTotal(), useSi));
				messages.add(Config.descriptionColor + "Used Swap File: " + Config.commandColor + bytesToHumanFormat(ram.getSwapUsed(), useSi));
				messages.add(Config.descriptionColor + "Total Swap File: " + Config.commandColor + bytesToHumanFormat(ram.getSwapTotal(), useSi));
			}
			Message.sendMessage(sender, messages);
			return true;
		} else {
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "view server metrics!");
		}
		return false;
	}

	private static String bytesToHumanFormat(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static String secsToHumanFormat(long secs) {
		if (secs < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.SECONDS.toDays(secs);
		secs -= TimeUnit.DAYS.toSeconds(days);
		long hours = TimeUnit.SECONDS.toHours(secs);
		secs -= TimeUnit.HOURS.toSeconds(hours);
		long minutes = TimeUnit.SECONDS.toMinutes(secs);
		secs -= TimeUnit.MINUTES.toSeconds(minutes);
		long seconds = TimeUnit.SECONDS.toSeconds(secs);

		String day = days + " Day";
		if (days > 1 || days == 0) {
			day += "s";
		}
		String hour = hours + " Hour";
		if (hours > 1 || hours == 0) {
			hour += "s";
		}
		String minute = minutes + " Minute";
		if (minutes > 1 || minutes == 0) {
			minute += "s";
		}
		String second = seconds + " Second";
		if (seconds > 1 || seconds == 0) {
			second += "s";
		}

		String s = "";
		if (days > 0)
			s = day + " " + hour + " " + minute + " " + second;
		else if (hours > 0)
			s = hour + " " + minute + " " + second;
		else if (minutes > 0)
			s = minute + " " + second;
		else
			s = second;
		return s;
	}

}
