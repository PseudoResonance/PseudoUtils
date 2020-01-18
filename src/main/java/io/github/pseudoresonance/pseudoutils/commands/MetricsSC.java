package io.github.pseudoresonance.pseudoutils.commands;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.Utils;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;
import io.github.pseudoresonance.pseudoutils.TPS;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class MetricsSC implements SubCommandExecutor {
	
	private static SystemInfo info = new SystemInfo();
	
	private static final boolean useSi = false;

	private static final DecimalFormat df = new DecimalFormat("#.##");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics"))) {
			boolean other = !(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.other"));
			boolean java = !(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.java"));
			boolean tps = !(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.tps"));
			boolean storage = !(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.storage"));
			boolean cpuPerm = !(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.cpu"));
			boolean memory = !(sender instanceof Player) || (sender.hasPermission("pseudoutils.metrics.memory"));
			PseudoUtils.plugin.doAsync(() -> {
				List<Object> messages = new ArrayList<Object>();
				CentralProcessor cpu = null;
				messages.add(Config.borderColor + "===---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.server_metrics") + Config.borderColor + "---===");
				if (other) {
					messages.add(Config.borderColor + "---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.other") + Config.borderColor + "---");
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.unique_players", Config.commandColor + PlayerDataController.getUniquePlayers()));
				}
				if (java) {
					OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
					OperatingSystem os = info.getOperatingSystem();
					messages.add(Config.borderColor + "---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.java_os") + Config.borderColor + "---");
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.java_version", Config.commandColor + System.getProperty("java.version")));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.java_vendor", Config.commandColor + System.getProperty("java.vendor")));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.os_manufacturer", Config.commandColor + os.getManufacturer()));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.os", Config.commandColor + os.getFamily() + " " + os.getVersionInfo().getVersion()));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.os_arch", Config.commandColor + osb.getArch(), Config.commandColor + os.getBitness()));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.system_time", Config.commandColor + LanguageManager.getLanguage(sender).formatTime(LocalTime.now())));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.system_date", Config.commandColor + LanguageManager.getLanguage(sender).formatDate(LocalDate.now())));
				}
				if (tps) {
					if (cpu == null)
						cpu = info.getHardware().getProcessor();
					RuntimeMXBean rsb = ManagementFactory.getRuntimeMXBean();
					messages.add(Config.borderColor + "---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.tps_time") + Config.borderColor + "---");
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.tps", Config.commandColor + TPS.getTps()));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.uptime", Config.commandColor + LanguageManager.getLanguage(sender).formatTimeAgo(new Timestamp(System.currentTimeMillis() - rsb.getUptime()), false, ChronoUnit.SECONDS, ChronoUnit.YEARS)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.system_uptime", Config.commandColor + LanguageManager.getLanguage(sender).formatTimeAgo(new Timestamp(System.currentTimeMillis() - (info.getOperatingSystem().getSystemUptime() * 1000)), false, ChronoUnit.SECONDS, ChronoUnit.YEARS)));
				}
				if (storage) {
					long totalBytes = 0;
					long freeBytes = 0;
					long usedBytes = 0;
					for (OSFileStore fs : info.getOperatingSystem().getFileSystem().getFileStores()) {
						totalBytes += fs.getTotalSpace();
						freeBytes += fs.getUsableSpace();
					}
					usedBytes = totalBytes - freeBytes;
					messages.add(Config.borderColor + "---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.storage") + Config.borderColor + "---");
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.used_space", Config.commandColor + Utils.bytesToHumanFormat(usedBytes, useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.free_space", Config.commandColor + Utils.bytesToHumanFormat(freeBytes, useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.total_space", Config.commandColor + Utils.bytesToHumanFormat(totalBytes, useSi)));
				}
				if (cpuPerm) {
					if (cpu == null)
						cpu = info.getHardware().getProcessor();
					messages.add(Config.borderColor + "---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.cpu") + Config.borderColor + "---");
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.model", Config.commandColor + cpu.getProcessorIdentifier().getName()));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.cores", Config.commandColor + cpu.getPhysicalProcessorCount()));
					if (cpu.getPhysicalPackageCount() > 1) {
						messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.sockets", Config.commandColor + cpu.getPhysicalPackageCount()));
						messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.cores_per_socket", Config.commandColor + ((Integer) (cpu.getPhysicalProcessorCount() / cpu.getPhysicalPackageCount()))));
					}
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.threads", Config.commandColor + cpu.getLogicalProcessorCount()));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.clock", Config.commandColor + (cpu.getMaxFreq() >= 0 ? FormatUtil.formatHertz(average(cpu.getCurrentFreq())) : LanguageManager.getLanguage(sender).getMessage("pseudoutils.unknown"))));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.load", Config.commandColor + (cpu.getSystemLoadAverage(1)[0] >= 0 ? LanguageManager.getLanguage(sender).getMessage("pseudoutils.percent", Double.valueOf(df.format(cpu.getSystemLoadAverage(1)[0]))) : LanguageManager.getLanguage(sender).getMessage("pseudoutils.unknown"))));
				}
				if (memory) {
					GlobalMemory ram = info.getHardware().getMemory();
					messages.add(Config.borderColor + "---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.memory") + Config.borderColor + "---");
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.used_memory", Config.commandColor + Utils.bytesToHumanFormat(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.free_jvm_memory", Config.commandColor + Utils.bytesToHumanFormat(Runtime.getRuntime().freeMemory(), useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.total_jvm_memory", Config.commandColor + Utils.bytesToHumanFormat(Runtime.getRuntime().totalMemory(), useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.max_jvm_memory", Config.commandColor + (Runtime.getRuntime().maxMemory() == Long.MAX_VALUE ? LanguageManager.getLanguage(sender).getMessage("pseudoutils.unlimited") : Utils.bytesToHumanFormat(Runtime.getRuntime().maxMemory(), useSi))));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.available_memory", Config.commandColor + Utils.bytesToHumanFormat(ram.getAvailable(), useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.total_memory", Config.commandColor + Utils.bytesToHumanFormat(ram.getTotal(), useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.used_swap", Config.commandColor + Utils.bytesToHumanFormat(ram.getVirtualMemory().getSwapUsed(), useSi)));
					messages.add(Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudoutils.total_swap", Config.commandColor + Utils.bytesToHumanFormat(ram.getVirtualMemory().getSwapTotal(), useSi)));
				}
				PseudoUtils.plugin.doSync(() -> {
					Chat.sendMessage(sender, messages);
				});
			});
			return true;
		} else {
			PseudoUtils.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudoutils.permission_metrics"));
		}
		return false;
	}
	
	private static long average(long[] nums) {
		long avg = 0;
		for (long l : nums)
			avg += l;
		avg /= nums.length;
		return avg;
	}

}
