package io.github.pseudoresonance.pseudoutils;

import java.io.File;
import java.lang.reflect.Field;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.bukkit.Bukkit;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.Utils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class InfinityMendingReplacement {
	
	public static void replaceDefaultInfinityClass() {
		int ver = Integer.valueOf(Utils.getBukkitVersion().split("_")[1]);
		PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.loading_in_minecraft", ver));
		if (ver >= 11) {
			PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.minecraft_11_later"));
			if ("9".compareTo(System.getProperty("java.version")) >= 0) {
				PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.java_8_later"));
				JavaCompiler c = ToolProvider.getSystemJavaCompiler();
				if (c == null) {
					PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.java_jre"));
					PseudoUtils.plugin.getDataFolder().mkdirs();
					File tools = new File(PseudoUtils.plugin.getDataFolder(), "tools.jar");
					if (!tools.isFile())
						PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.place_tools_at", tools.getAbsolutePath()));
					if (System.getProperty("net.bytebuddy.agent.toolsjar") == null)
						System.setProperty("net.bytebuddy.agent.toolsjar", tools.getAbsolutePath());
					File attachDll = new File(PseudoUtils.plugin.getDataFolder(), "attach.dll");
					File attachSo = new File(PseudoUtils.plugin.getDataFolder(), "attach.so");
					if (!attachDll.isFile() && !attachSo.isFile())
						PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.place_tools_at", PseudoUtils.plugin.getDataFolder().getAbsolutePath()));
					if (System.getProperty("java.library.path") == null)
						System.setProperty("java.library.path", PseudoUtils.plugin.getDataFolder().getAbsolutePath());
					else
						System.setProperty("java.library.path", PseudoUtils.plugin.getDataFolder().getAbsolutePath() + File.pathSeparator + System.getProperty("java.library.path"));
					if (System.getProperty("jna.library.path") == null)
						System.setProperty("jna.library.path", PseudoUtils.plugin.getDataFolder().getAbsolutePath());
					else
						System.setProperty("jna.library.path", PseudoUtils.plugin.getDataFolder().getAbsolutePath() + File.pathSeparator + System.getProperty("jna.library.path"));
					try {
						Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
						fieldSysPath.setAccessible(true);
						fieldSysPath.set(null, null);
						System.loadLibrary("attach");
						PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.initialized_attach", System.mapLibraryName("attach")));
					} catch (Exception e) {
						PseudoUtils.plugin.getChat().sendPluginError(Bukkit.getConsoleSender(), Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudoutils.error_initialize_attach"));
						PseudoUtils.plugin.getChat().sendPluginError(Bukkit.getConsoleSender(), Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudoutils.current_java_library_path", System.getProperty("java.library.path")));
						PseudoUtils.plugin.getChat().sendPluginError(Bukkit.getConsoleSender(), Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudoutils.current_jna_library_path", System.getProperty("jna.library.path")));
						e.printStackTrace();
					}
				} else {
					PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.java_jdk"));
				}
			}
			PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.loading_bytebuddy"));
			ByteBuddyAgent.install();
			try {
				PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.bytebuddy_loaded"));
				Class<?> enchantmentInfiniteArrowsClass = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".EnchantmentInfiniteArrows");
				Class<?> enchantmentClass = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".Enchantment");
				PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.found_necessary_classes"));
				new ByteBuddy()
				.redefine(enchantmentInfiniteArrowsClass)
				.method(ElementMatchers.takesArguments(enchantmentClass))
				.intercept(SuperMethodCall.INSTANCE)
				.make()
				.load(enchantmentInfiniteArrowsClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
				PseudoUtils.plugin.getChat().sendPluginMessage(Bukkit.getConsoleSender(), LanguageManager.getLanguage().getMessage("pseudoutils.injection_complete"));
			} catch (Exception e) {
				e.printStackTrace();
				PseudoUtils.plugin.getChat().sendPluginError(Bukkit.getConsoleSender(), Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudoutils.error_injecting"));
			}
		} else {
			PseudoUtils.plugin.getChat().sendPluginError(Bukkit.getConsoleSender(), Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudoutils.error_pre_minecraft_11"));
		}
	}

}
