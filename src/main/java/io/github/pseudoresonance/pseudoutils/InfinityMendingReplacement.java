package io.github.pseudoresonance.pseudoutils;

import org.bukkit.Bukkit;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.Utils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class InfinityMendingReplacement {
	
	public static void replaceDefaultInfinityClass() {
		int ver = Integer.valueOf(Utils.getBukkitVersion().split("_")[1]);
		PseudoUtils.message.sendPluginMessage(Bukkit.getConsoleSender(), "Loading in Minecraft 1." + ver);
		if (ver >= 11) {
			PseudoUtils.message.sendPluginMessage(Bukkit.getConsoleSender(), "Loading ByteBuddy!");
			ByteBuddyAgent.install();
			try {
				PseudoUtils.message.sendPluginMessage(Bukkit.getConsoleSender(), "ByteBuddy loaded! Beginning injection of custom mending/infinity code!");
				Class<?> enchantmentInfiniteArrowsClass = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".EnchantmentInfiniteArrows");
				Class<?> enchantmentClass = Class.forName("net.minecraft.server." + Utils.getBukkitVersion() + ".Enchantment");
				PseudoUtils.message.sendPluginMessage(Bukkit.getConsoleSender(), "Found necessary classes!");
				new ByteBuddy()
				.redefine(enchantmentInfiniteArrowsClass)
				.method(ElementMatchers.takesArguments(enchantmentClass))
				.intercept(SuperMethodCall.INSTANCE)
				.make()
				.load(enchantmentInfiniteArrowsClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
				PseudoUtils.message.sendPluginMessage(Bukkit.getConsoleSender(), "Injection complete!");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				PseudoUtils.message.sendPluginError(Bukkit.getConsoleSender(), Errors.CUSTOM, "There was an error injecting code to allow infinity and mending!");
			}
		} else {
			PseudoUtils.message.sendPluginError(Bukkit.getConsoleSender(), Errors.CUSTOM, "Infinity and mending were not exclusive before Minecraft 1.11!");
		}
	}

}
