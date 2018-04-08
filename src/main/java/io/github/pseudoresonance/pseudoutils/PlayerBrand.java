package io.github.pseudoresonance.pseudoutils;

import java.util.HashMap;

public class PlayerBrand {
	
	private static HashMap<String, String> brands = new HashMap<String, String>();
	
	public static void playerLogin(String name, String brand) {
		brands.put(name.toLowerCase(), brand);
	}
	
	public static void playerLogout(String name) {
		brands.remove(name.toLowerCase());
	}
	
	public static String getBrand(String name) {
		return brands.get(name.toLowerCase());
	}

}
