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

import io.github.pseudoresonance.pseudoapi.bukkit.ConfigOptions;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.Utils;
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
			OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
			RuntimeMXBean rsb = ManagementFactory.getRuntimeMXBean();
			CentralProcessor cpu = info.getHardware().getProcessor();
			messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + "Server Metrics" + ConfigOptions.border + "---===");
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.other"))) {
				messages.add(ConfigOptions.border + "---" + ConfigOptions.title + "Other" + ConfigOptions.border + "---");
				messages.add(ConfigOptions.description + "Unique Players Joined: " + ConfigOptions.command + PlayerDataController.getUniquePlayers());
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.java"))) {
				OperatingSystem os = info.getOperatingSystem();
				messages.add(ConfigOptions.border + "---" + ConfigOptions.title + "Java & OS" + ConfigOptions.border + "---");
				messages.add(ConfigOptions.description + "Java Version: " + ConfigOptions.command + System.getProperty("java.version"));
				messages.add(ConfigOptions.description + "Java Vendor: " + ConfigOptions.command + System.getProperty("java.vendor"));
				messages.add(ConfigOptions.description + "OS Manufacturer: " + ConfigOptions.command + os.getManufacturer());
				messages.add(ConfigOptions.description + "OS: " + ConfigOptions.command + os.getFamily() + " " + os.getVersion());
				messages.add(ConfigOptions.description + "OS Arch: " + ConfigOptions.command + osb.getArch() + " " + os.getBitness() + "-bit");
				messages.add(ConfigOptions.description + "System Time: " + ConfigOptions.command + LocalDateTime.now().format(DateTimeFormatter.ofPattern(io.github.pseudoresonance.pseudoutils.ConfigOptions.timeFormat)));
				messages.add(ConfigOptions.description + "System Date: " + ConfigOptions.command + LocalDateTime.now().format(DateTimeFormatter.ofPattern(io.github.pseudoresonance.pseudoutils.ConfigOptions.dateFormat)));
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.tps"))) {
				messages.add(ConfigOptions.border + "---" + ConfigOptions.title + "TPS and Time" + ConfigOptions.border + "---");
				messages.add(ConfigOptions.description + "TPS: " + ConfigOptions.command + TPS.getTps());
				messages.add(ConfigOptions.description + "Uptime: " + ConfigOptions.command + Utils.millisToHumanFormat(rsb.getUptime()));
				messages.add(ConfigOptions.description + "System Uptime: " + ConfigOptions.command + secsToHumanFormat(cpu.getSystemUptime()));
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
				messages.add(ConfigOptions.border + "---" + ConfigOptions.title + "Storage" + ConfigOptions.border + "---");
				messages.add(ConfigOptions.description + "Space Used: " + ConfigOptions.command + bytesToHumanFormat(usedBytes, useSi));
				messages.add(ConfigOptions.description + "Free Space: " + ConfigOptions.command + bytesToHumanFormat(freeBytes, useSi));
				messages.add(ConfigOptions.description + "Total Space: " + ConfigOptions.command + bytesToHumanFormat(totalBytes, useSi));
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.cpu"))) {
				messages.add(ConfigOptions.border + "---" + ConfigOptions.title + "CPU" + ConfigOptions.border + "---");
				messages.add(ConfigOptions.description + "Model: " + ConfigOptions.command + cpu.getName());
				messages.add(ConfigOptions.description + "Cores: " + ConfigOptions.command + cpu.getPhysicalProcessorCount());
				if (cpu.getPhysicalPackageCount() > 1) {
					messages.add(ConfigOptions.description + "Sockets: " + ConfigOptions.command + cpu.getPhysicalPackageCount());
					messages.add(ConfigOptions.description + "Cores per Socket: " + ConfigOptions.command + ((Integer) (cpu.getPhysicalProcessorCount() / cpu.getPhysicalPackageCount())));
				}
				messages.add(ConfigOptions.description + "Threads: " + ConfigOptions.command + cpu.getLogicalProcessorCount());
				messages.add(ConfigOptions.description + "Clock: " + ConfigOptions.command + (cpu.getVendorFreq() >= 0 ? Double.valueOf(df.format(cpu.getVendorFreq() / 1000000000.0)) + "GHz" : "Unknown"));
				messages.add(ConfigOptions.description + "Load: " + ConfigOptions.command + (cpu.getSystemCpuLoad() >= 0 ? Double.valueOf(df.format(cpu.getSystemCpuLoad() * 100.0)) + "%" : "Unknown"));
			}
			if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.memory"))) {
				GlobalMemory ram = info.getHardware().getMemory();
				messages.add(ConfigOptions.border + "---" + ConfigOptions.title + "Memory" + ConfigOptions.border + "---");
				messages.add(ConfigOptions.description + "Used Memory: " + ConfigOptions.command + bytesToHumanFormat(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), useSi));
				messages.add(ConfigOptions.description + "Free JVM Memory: " + ConfigOptions.command + bytesToHumanFormat(Runtime.getRuntime().freeMemory(), useSi));
				messages.add(ConfigOptions.description + "Total JVM Memory: " + ConfigOptions.command + bytesToHumanFormat(Runtime.getRuntime().totalMemory(), useSi));
				messages.add(ConfigOptions.description + "Max JVM Memory: " + ConfigOptions.command + (Runtime.getRuntime().maxMemory() == Long.MAX_VALUE ? "Unlimited" : bytesToHumanFormat(Runtime.getRuntime().maxMemory(), useSi)));
				messages.add(ConfigOptions.description + "Available Memory: " + ConfigOptions.command + bytesToHumanFormat(ram.getAvailable(), useSi));
				messages.add(ConfigOptions.description + "Total Memory: " + ConfigOptions.command + bytesToHumanFormat(ram.getTotal(), useSi));
				messages.add(ConfigOptions.description + "Used Swap File: " + ConfigOptions.command + bytesToHumanFormat(ram.getSwapUsed(), useSi));
				messages.add(ConfigOptions.description + "Total Swap File: " + ConfigOptions.command + bytesToHumanFormat(ram.getSwapTotal(), useSi));
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
