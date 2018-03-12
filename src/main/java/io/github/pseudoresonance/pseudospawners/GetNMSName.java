package io.github.pseudoresonance.pseudospawners;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class GetNMSName {
	
	private static HashMap<Integer, String> nameMap = new HashMap<Integer, String>();
	
	public static void getNames() {
		try {
			Class<?> entityTypes = Class.forName("net.minecraft.server." + PseudoSpawners.getBukkitVersion() + ".EntityTypes");
			Object registry = entityTypes.getField("b").get(null);
			Method getNameMap = registry.getClass().getMethod("b", Object.class);
			Method getID = registry.getClass().getMethod("a", Object.class);
			if (registry instanceof Iterable) {
				@SuppressWarnings("unchecked")
				Iterable<Object> iterate = (Iterable<Object>) registry;
				for (Object entityClass : iterate) {
					Object nameMapObject = getNameMap.invoke(registry, entityClass);
					Object idObject = getID.invoke(registry, entityClass);
					Field keyField = nameMapObject.getClass().getDeclaredField("b");
					keyField.setAccessible(true);
					if (idObject instanceof Integer) {
						int id = (Integer) idObject;
						Object keyObject = keyField.get(nameMapObject);
						if (keyObject instanceof String) {
							String name = (String) keyObject;
							System.out.println("ID: " + id + " NAME: " + name);
							nameMap.put(id, name);
						}
					}
				}
			}
		} catch (ClassNotFoundException | IllegalArgumentException | SecurityException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
