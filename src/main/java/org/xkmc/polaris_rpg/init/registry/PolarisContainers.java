package org.xkmc.polaris_rpg.init.registry;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;

public class PolarisContainers {

	public static void register() {
	}

	private static String contLang(ContainerType<?> type) {
		ResourceLocation id = type.getRegistryName();
		return "container." + id.getNamespace() + "." + id.getPath();
	}

}
